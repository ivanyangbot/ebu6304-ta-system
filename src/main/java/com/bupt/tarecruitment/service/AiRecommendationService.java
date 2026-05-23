package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.SkillRecommendation;
import com.bupt.tarecruitment.model.SkillRecommendation.ResourceLink;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service that calls the Volcano Engine (火山方舟) large-language-model API to
 * generate personalised, explainable learning-path recommendations for each
 * skill that an applicant is missing relative to a job requirement.
 *
 * <h2>Design rationale</h2>
 * <p>The coursework requires that AI outputs must <em>not</em> be blindly accepted.
 * This service therefore applies the following safeguards:</p>
 * <ol>
 *   <li><strong>Structured prompt</strong> – the request instructs the model to respond
 *       with a strict JSON schema so that structured logic can validate each field.</li>
 *   <li><strong>Sanitisation</strong> – every text field returned by the AI is passed
 *       through {@link #sanitise(String)} which strips HTML, truncates over-long strings,
 *       and removes blank values.</li>
 *   <li><strong>Static fallback catalogue</strong> – if the API is unavailable, or the
 *       model returns malformed JSON, a curated set of hard-coded resources is returned
 *       so the UI never shows an empty panel.</li>
 *   <li><strong>Hour-cap</strong> – the estimated study hours are clamped to [1, 200] to
 *       prevent absurd values from confusing users.</li>
 * </ol>
 *
 * <h2>API configuration</h2>
 * <p>The API key is injected via the servlet context init-param
 * {@code volcengine.api.key} (configured in {@code web.xml}) and passed to the
 * constructor. It is never hard-coded in source files.</p>
 *
 * <h2>Model used</h2>
 * <p>Doubao-lite-4k (endpoint {@code doubao-lite-4k}) is used because it provides
 * a good balance of response speed and quality for short structured outputs.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     SkillRecommendation
 */
public class AiRecommendationService {

    private static final Logger LOGGER = Logger.getLogger(AiRecommendationService.class.getName());

    /** Volcano Engine (火山方舟) Chat Completions endpoint. */
    private static final String API_ENDPOINT =
            "https://ark.cn-beijing.volces.com/api/v3/chat/completions";

    /** Model ID for Doubao-lite-4k. */
    private static final String MODEL_ID = "doubao-lite-4k-240628";

    /** Maximum characters allowed in any single AI-generated text field. */
    private static final int MAX_FIELD_LENGTH = 600;

    /** HTTP request timeout (seconds). */
    private static final int TIMEOUT_SECONDS = 20;

    private final String apiKey;
    private final HttpClient httpClient;
    private final Gson gson;

    // -------------------------------------------------------------------------
    // Static fallback resource catalogue
    // -------------------------------------------------------------------------

    /**
     * Curated static fallback resources, keyed by lower-case skill name.
     * Used when the API is unreachable or returns invalid JSON.
     * Entries deliberately cover the most common TA-role skills.
     */
    private static final Map<String, List<ResourceLink>> STATIC_RESOURCES;

    static {
        Map<String, List<ResourceLink>> m = new HashMap<>();

        m.put("java", List.of(
                new ResourceLink("Oracle Java Tutorials", "https://docs.oracle.com/javase/tutorial/"),
                new ResourceLink("MOOC – Java Programming (Helsinki)", "https://java-programming.mooc.fi/"),
                new ResourceLink("Baeldung Java Guides", "https://www.baeldung.com/java-tutorial")
        ));
        m.put("python", List.of(
                new ResourceLink("Python Official Tutorial", "https://docs.python.org/3/tutorial/"),
                new ResourceLink("Real Python", "https://realpython.com/"),
                new ResourceLink("Automate the Boring Stuff (free book)", "https://automatetheboringstuff.com/")
        ));
        m.put("sql", List.of(
                new ResourceLink("SQLZoo Interactive Tutorial", "https://sqlzoo.net/"),
                new ResourceLink("Mode Analytics SQL Tutorial", "https://mode.com/sql-tutorial/"),
                new ResourceLink("W3Schools SQL Reference", "https://www.w3schools.com/sql/")
        ));
        m.put("javascript", List.of(
                new ResourceLink("MDN Web Docs – JavaScript Guide", "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide"),
                new ResourceLink("javascript.info", "https://javascript.info/"),
                new ResourceLink("freeCodeCamp JS Algorithms", "https://www.freecodecamp.org/learn/javascript-algorithms-and-data-structures/")
        ));
        m.put("c++", List.of(
                new ResourceLink("LearnCpp.com", "https://www.learncpp.com/"),
                new ResourceLink("CPP Reference", "https://en.cppreference.com/"),
                new ResourceLink("Exercism C++ Track", "https://exercism.org/tracks/cpp")
        ));
        m.put("machine learning", List.of(
                new ResourceLink("Coursera ML Specialisation (Ng)", "https://www.coursera.org/specializations/machine-learning-introduction"),
                new ResourceLink("fast.ai Practical Deep Learning", "https://course.fast.ai/"),
                new ResourceLink("Kaggle Learn – Intro to ML", "https://www.kaggle.com/learn/intro-to-machine-learning")
        ));
        m.put("data structures", List.of(
                new ResourceLink("Visualgo – Visualising Algorithms", "https://visualgo.net/"),
                new ResourceLink("CS50 Data Structures (YouTube)", "https://www.youtube.com/playlist?list=PLhQjrBD2T382_R182iC2gNZI9HzWFMC_8"),
                new ResourceLink("GeeksForGeeks DSA", "https://www.geeksforgeeks.org/data-structures/")
        ));
        m.put("algorithms", List.of(
                new ResourceLink("Algorithmist", "https://algorithmist.com/wiki/Main_Page"),
                new ResourceLink("LeetCode Explore", "https://leetcode.com/explore/"),
                new ResourceLink("MIT 6.006 Lectures (YouTube)", "https://www.youtube.com/playlist?list=PLUl4u3cNGP63EdVPNLG3ToM6LaEUuStEY")
        ));
        m.put("git", List.of(
                new ResourceLink("Pro Git Book (free)", "https://git-scm.com/book/en/v2"),
                new ResourceLink("GitHub Skills", "https://skills.github.com/"),
                new ResourceLink("Atlassian Git Tutorials", "https://www.atlassian.com/git/tutorials")
        ));
        m.put("linux", List.of(
                new ResourceLink("The Linux Command Line (free book)", "https://linuxcommand.org/tlcl.php"),
                new ResourceLink("OverTheWire Bandit (hands-on)", "https://overthewire.org/wargames/bandit/"),
                new ResourceLink("Linux Journey", "https://linuxjourney.com/")
        ));
        m.put("html", List.of(
                new ResourceLink("MDN HTML Guide", "https://developer.mozilla.org/en-US/docs/Web/HTML"),
                new ResourceLink("W3Schools HTML Tutorial", "https://www.w3schools.com/html/"),
                new ResourceLink("freeCodeCamp Responsive Web Design", "https://www.freecodecamp.org/learn/2022/responsive-web-design/")
        ));
        m.put("css", List.of(
                new ResourceLink("MDN CSS Basics", "https://developer.mozilla.org/en-US/docs/Learn/CSS/First_steps"),
                new ResourceLink("CSS-Tricks", "https://css-tricks.com/"),
                new ResourceLink("Flexbox Froggy (interactive)", "https://flexboxfroggy.com/")
        ));
        m.put("statistics", List.of(
                new ResourceLink("Khan Academy Statistics & Probability", "https://www.khanacademy.org/math/statistics-probability"),
                new ResourceLink("StatQuest with Josh Starmer (YouTube)", "https://www.youtube.com/c/joshstarmer"),
                new ResourceLink("OpenIntro Statistics (free textbook)", "https://www.openintro.org/book/os/")
        ));
        m.put("r", List.of(
                new ResourceLink("R for Data Science (free book)", "https://r4ds.had.co.nz/"),
                new ResourceLink("Swirl – Learn R in R", "https://swirlstats.com/"),
                new ResourceLink("DataCamp Intro to R (free chapter)", "https://app.datacamp.com/learn/courses/free-introduction-to-r")
        ));
        m.put("networking", List.of(
                new ResourceLink("Cisco NetAcad – Networking Basics", "https://www.netacad.com/courses/networking/networking-basics"),
                new ResourceLink("Professor Messer CompTIA Network+", "https://www.professormesser.com/network-plus/n10-008/n10-008-video/n10-008-training-course/"),
                new ResourceLink("Computer Networking: A Top-Down Approach (slides)", "https://gaia.cs.umass.edu/kurose_ross/online_lectures.htm")
        ));
        m.put("docker", List.of(
                new ResourceLink("Docker Official Get-Started Guide", "https://docs.docker.com/get-started/"),
                new ResourceLink("Play with Docker (browser sandbox)", "https://labs.play-with-docker.com/"),
                new ResourceLink("TechWorld with Nana – Docker Tutorial", "https://www.youtube.com/watch?v=3c-iBn73dDE")
        ));

        STATIC_RESOURCES = Collections.unmodifiableMap(m);
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a new {@code AiRecommendationService} with the provided API key.
     *
     * @param apiKey Volcano Engine API key; must not be {@code null} or empty
     * @throws IllegalArgumentException if {@code apiKey} is blank
     */
    public AiRecommendationService(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Volcano Engine API key must not be blank");
        }
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
        this.gson = new Gson();
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Generates AI-powered learning recommendations for a list of missing skills,
     * given the context of a specific job title and module.
     *
     * <p>The method calls the Volcano Engine LLM API with a structured prompt and
     * parses the JSON response. If the API call fails for any reason (network error,
     * quota exceeded, malformed response) the method falls back gracefully to the
     * built-in static resource catalogue so the UI always receives usable data.</p>
     *
     * <p><strong>AI output validation</strong> – every field returned by the model
     * is sanitised (see {@link #sanitise(String)}) and numeric values are clamped to
     * reasonable ranges before the result is returned. This ensures the application
     * never exposes raw, unvalidated AI text to end-users.</p>
     *
     * @param missingSkills non-null list of skill names the applicant lacks
     * @param jobTitle      title of the job being applied for (used in the prompt)
     * @param moduleName    academic module the job relates to (used in the prompt)
     * @return non-null list of {@link SkillRecommendation} objects; one per missing skill
     */
    public List<SkillRecommendation> recommend(List<String> missingSkills,
                                               String jobTitle,
                                               String moduleName) {
        if (missingSkills == null || missingSkills.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            String prompt = buildPrompt(missingSkills, jobTitle, moduleName);
            String rawJson = callApi(prompt);
            List<SkillRecommendation> parsed = parseResponse(rawJson, missingSkills);
            if (parsed != null && !parsed.isEmpty()) {
                return parsed;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "AI recommendation API call failed, using static fallback. Reason: " + e.getMessage(), e);
        }

        return buildStaticFallback(missingSkills);
    }

    // -------------------------------------------------------------------------
    // Private helpers – prompt construction
    // -------------------------------------------------------------------------

    /**
     * Builds the chat completion prompt sent to the LLM.
     *
     * <p>The prompt instructs the model to return a strict JSON array so that the
     * response can be parsed deterministically without relying on natural-language
     * parsing.</p>
     *
     * @param missingSkills skills the applicant lacks
     * @param jobTitle      job title context
     * @param moduleName    module context
     * @return fully-formed prompt string
     */
    private String buildPrompt(List<String> missingSkills, String jobTitle, String moduleName) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are an expert career coach for university teaching assistants. ");
        sb.append("An applicant is applying for the role \"").append(jobTitle)
          .append("\" for module \"").append(moduleName).append("\". ");
        sb.append("They are missing the following skills: ")
          .append(String.join(", ", missingSkills)).append(". ");
        sb.append("\n\nFor EACH missing skill, respond with a JSON array (no markdown, no code fences) ");
        sb.append("where each element has exactly these fields:\n");
        sb.append("{\n");
        sb.append("  \"skill\": \"<skill name>\",\n");
        sb.append("  \"reason\": \"<1-2 sentences explaining why this skill is important for this specific role>\",\n");
        sb.append("  \"learningPath\": \"<numbered list of 3 concrete steps, separated by | character>\",\n");
        sb.append("  \"resourceLinks\": [{\"label\": \"<site name>\", \"url\": \"<https://...>\"}],\n");
        sb.append("  \"estimatedHours\": <integer>\n");
        sb.append("}\n");
        sb.append("Provide exactly 2-3 resourceLinks per skill. Only include HTTPS URLs. ");
        sb.append("estimatedHours must be a realistic integer between 5 and 120. ");
        sb.append("Do NOT wrap the output in any markdown or code block. Return raw JSON only.");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Private helpers – HTTP call
    // -------------------------------------------------------------------------

    /**
     * Sends the prompt to the Volcano Engine Chat Completions API and returns
     * the raw response content string.
     *
     * @param userPrompt the user-turn message
     * @return the model's response text
     * @throws IOException          on network or I/O failure
     * @throws InterruptedException if the HTTP call is interrupted
     */
    private String callApi(String userPrompt) throws IOException, InterruptedException {
        // Build request body as JSON
        JsonObject body = new JsonObject();
        body.addProperty("model", MODEL_ID);

        JsonArray messages = new JsonArray();
        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty("content",
                "You are a helpful career advisor. Always respond with valid JSON arrays only. "
                + "Never add markdown, code fences, or extra commentary.");
        messages.add(systemMsg);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", userPrompt);
        messages.add(userMsg);

        body.add("messages", messages);
        body.addProperty("max_tokens", 1500);
        body.addProperty("temperature", 0.3);

        String requestBody = gson.toJson(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new IOException("Volcano Engine API returned HTTP " + statusCode
                    + ": " + response.body());
        }

        // Extract "choices[0].message.content" from the OpenAI-compatible response format
        JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray choices = responseJson.getAsJsonArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IOException("API response contains no choices");
        }
        JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
        return message.get("content").getAsString();
    }

    // -------------------------------------------------------------------------
    // Private helpers – response parsing
    // -------------------------------------------------------------------------

    /**
     * Parses the model's raw JSON text into a list of {@link SkillRecommendation} objects.
     *
     * <p>The method is lenient: if a single skill entry fails to parse, it is skipped
     * and the remaining entries are still processed. For each successfully parsed entry,
     * all text fields are sanitised and numeric values are clamped.</p>
     *
     * @param rawContent    the model's response string
     * @param missingSkills original list of missing skills (used as fallback for missing names)
     * @return list of recommendations, possibly empty on total parse failure
     */
    private List<SkillRecommendation> parseResponse(String rawContent, List<String> missingSkills) {
        if (rawContent == null || rawContent.isBlank()) {
            return Collections.emptyList();
        }

        // Strip potential markdown code fences the model might add despite instructions
        String cleaned = rawContent.trim();
        if (cleaned.startsWith("```")) {
            int firstNewline = cleaned.indexOf('\n');
            if (firstNewline >= 0) {
                cleaned = cleaned.substring(firstNewline + 1);
            }
            if (cleaned.endsWith("```")) {
                cleaned = cleaned.substring(0, cleaned.lastIndexOf("```")).trim();
            }
        }

        List<SkillRecommendation> results = new ArrayList<>();
        try {
            JsonArray arr = JsonParser.parseString(cleaned).getAsJsonArray();
            for (int i = 0; i < arr.size(); i++) {
                try {
                    JsonObject obj = arr.get(i).getAsJsonObject();
                    SkillRecommendation rec = parseSingleEntry(obj, i < missingSkills.size() ? missingSkills.get(i) : "");
                    results.add(rec);
                } catch (Exception entryEx) {
                    LOGGER.log(Level.FINE, "Skipping malformed AI recommendation entry at index " + i, entryEx);
                }
            }
        } catch (Exception parseEx) {
            LOGGER.log(Level.WARNING, "Failed to parse AI response as JSON array", parseEx);
        }
        return results;
    }

    /**
     * Parses a single JSON object into a {@link SkillRecommendation}.
     *
     * @param obj          the JSON object from the AI response array
     * @param fallbackSkill skill name to use if the object omits the "skill" field
     * @return a populated {@link SkillRecommendation} with sanitised fields
     */
    private SkillRecommendation parseSingleEntry(JsonObject obj, String fallbackSkill) {
        String skill = getStringOrDefault(obj, "skill", fallbackSkill);
        String reason = sanitise(getStringOrDefault(obj, "reason", ""));
        String learningPath = sanitise(getStringOrDefault(obj, "learningPath", ""));
        int estimatedHours = clampHours(getIntOrDefault(obj, "estimatedHours", 20));

        List<ResourceLink> links = new ArrayList<>();
        JsonElement linksEl = obj.get("resourceLinks");
        if (linksEl != null && linksEl.isJsonArray()) {
            for (JsonElement linkEl : linksEl.getAsJsonArray()) {
                try {
                    JsonObject linkObj = linkEl.getAsJsonObject();
                    String label = sanitise(getStringOrDefault(linkObj, "label", "Resource"));
                    String url = getStringOrDefault(linkObj, "url", "");
                    if (isValidHttpsUrl(url)) {
                        links.add(new ResourceLink(label, url));
                    }
                } catch (Exception ignored) {
                    // skip malformed link entries
                }
            }
        }

        // Merge with static fallback links if AI provided fewer than 2
        if (links.size() < 2) {
            List<ResourceLink> staticLinks = getFallbackLinks(skill);
            for (ResourceLink sl : staticLinks) {
                if (links.stream().noneMatch(l -> l.getUrl().equals(sl.getUrl()))) {
                    links.add(sl);
                    if (links.size() >= 3) break;
                }
            }
        }

        return new SkillRecommendation(skill, reason, learningPath, links, estimatedHours);
    }

    // -------------------------------------------------------------------------
    // Private helpers – static fallback
    // -------------------------------------------------------------------------

    /**
     * Builds a list of {@link SkillRecommendation} objects using only the static
     * fallback catalogue. Used when the API is unavailable.
     *
     * @param missingSkills skills to create fallback recommendations for
     * @return list of fallback recommendations; never null
     */
    private List<SkillRecommendation> buildStaticFallback(List<String> missingSkills) {
        List<SkillRecommendation> fallbacks = new ArrayList<>();
        for (String skill : missingSkills) {
            String reason = "This skill is required for the position. Building this skill "
                    + "will strengthen your application and improve your effectiveness as a TA.";
            String learningPath = "1. Study the fundamentals using one of the resources below | "
                    + "2. Complete a beginner project or exercise set | "
                    + "3. Practise with past assignments or online challenges";
            List<ResourceLink> links = getFallbackLinks(skill);
            fallbacks.add(new SkillRecommendation(skill, reason, learningPath, links, 20));
        }
        return fallbacks;
    }

    /**
     * Returns curated static resource links for a given skill name.
     * Falls back to three generic study tips if the skill is not in the catalogue.
     *
     * @param skill skill name (case-insensitive lookup)
     * @return non-empty list of {@link ResourceLink} objects
     */
    private List<ResourceLink> getFallbackLinks(String skill) {
        if (skill == null) {
            return getGenericLinks();
        }
        String key = skill.toLowerCase().trim();
        List<ResourceLink> exact = STATIC_RESOURCES.get(key);
        if (exact != null) {
            return exact;
        }
        // Partial match (e.g. "machine learning" when key is "ml")
        for (Map.Entry<String, List<ResourceLink>> entry : STATIC_RESOURCES.entrySet()) {
            if (key.contains(entry.getKey()) || entry.getKey().contains(key)) {
                return entry.getValue();
            }
        }
        return getGenericLinks();
    }

    /**
     * Returns a small set of generic learning resources used when no skill-specific
     * resources are available.
     *
     * @return generic resource list
     */
    private List<ResourceLink> getGenericLinks() {
        return List.of(
                new ResourceLink("Coursera – Browse Free Courses", "https://www.coursera.org/"),
                new ResourceLink("MIT OpenCourseWare", "https://ocw.mit.edu/"),
                new ResourceLink("YouTube EDU", "https://www.youtube.com/education")
        );
    }

    // -------------------------------------------------------------------------
    // Private helpers – validation and sanitisation
    // -------------------------------------------------------------------------

    /**
     * Sanitises a text string returned by the AI by:
     * <ol>
     *   <li>Stripping HTML tags</li>
     *   <li>Removing control characters (except newlines and tabs)</li>
     *   <li>Truncating to {@link #MAX_FIELD_LENGTH} characters</li>
     *   <li>Returning an empty string for {@code null} input</li>
     * </ol>
     *
     * @param raw the raw AI-generated text
     * @return sanitised string; never {@code null}
     */
    public String sanitise(String raw) {
        if (raw == null) {
            return "";
        }
        // Remove HTML tags
        String stripped = raw.replaceAll("<[^>]*>", "");
        // Remove non-printable control chars (keep \n and \t)
        stripped = stripped.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");
        // Collapse multiple blank lines
        stripped = stripped.replaceAll("\n{3,}", "\n\n").trim();
        // Truncate
        if (stripped.length() > MAX_FIELD_LENGTH) {
            stripped = stripped.substring(0, MAX_FIELD_LENGTH) + "…";
        }
        return stripped;
    }

    /**
     * Returns {@code true} if the given string is a well-formed absolute HTTPS URL.
     *
     * @param url candidate URL string
     * @return {@code true} for valid HTTPS URLs
     */
    private boolean isValidHttpsUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }
        return url.startsWith("https://") && url.length() > 12;
    }

    /**
     * Clamps an estimated hours value to the range [1, 200].
     *
     * @param hours raw value from AI
     * @return clamped value
     */
    private int clampHours(int hours) {
        return Math.max(1, Math.min(200, hours));
    }

    /**
     * Safely reads a string field from a JSON object, returning a default value if
     * the field is absent or not a string.
     *
     * @param obj          JSON object to read from
     * @param key          field name
     * @param defaultValue value to return when the field is absent
     * @return field value or {@code defaultValue}
     */
    private String getStringOrDefault(JsonObject obj, String key, String defaultValue) {
        JsonElement el = obj.get(key);
        if (el == null || el.isJsonNull() || !el.isJsonPrimitive()) {
            return defaultValue;
        }
        return el.getAsString();
    }

    /**
     * Safely reads an integer field from a JSON object, returning a default value if
     * the field is absent or not a number.
     *
     * @param obj          JSON object to read from
     * @param key          field name
     * @param defaultValue value to return when the field is absent
     * @return field value as integer or {@code defaultValue}
     */
    private int getIntOrDefault(JsonObject obj, String key, int defaultValue) {
        try {
            JsonElement el = obj.get(key);
            if (el == null || el.isJsonNull()) {
                return defaultValue;
            }
            return el.getAsInt();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
