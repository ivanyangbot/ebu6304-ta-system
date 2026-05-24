package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.MatchResult;
import com.bupt.tarecruitment.model.SkillRecommendation;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.service.AiRecommendationService;
import com.bupt.tarecruitment.service.JobService;
import com.bupt.tarecruitment.service.MatchService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet that renders the AI-powered Skill Learning Path page.
 *
 * <h2>URL</h2>
 * <pre>GET /applicant/skill-recommend?jobId={jobId}</pre>
 *
 * <h2>Responsibility</h2>
 * <ol>
 *   <li>Verifies the requesting user is an {@code APPLICANT}.</li>
 *   <li>Loads the {@link Job} and the current applicant's profile.</li>
 *   <li>Runs {@link MatchService#calculateMatch} to obtain the list of missing skills.</li>
 *   <li>Calls {@link AiRecommendationService#recommend} to generate learning-path
 *       recommendations for each missing skill.</li>
 *   <li>Forwards all data to {@code skill-recommend.jsp} for rendering.</li>
 * </ol>
 *
 * <h2>AI key injection</h2>
 * <p>The Volcano Engine API key is read from the servlet-context init-param
 * {@code volcengine.api.key} (configured in {@code web.xml}). If the init-param
 * is absent or blank, the service falls back to the built-in static resource
 * catalogue without calling the external API.</p>
 *
 * <h2>Error handling</h2>
 * <ul>
 *   <li>Missing or invalid job ID → forwards to the error page.</li>
 *   <li>API failures → {@link AiRecommendationService} handles internally and
 *       returns static fallback data; no error is shown to the user.</li>
 * </ul>
 *
 * @author  Group 71
 * @version 1.0
 * @see     AiRecommendationService
 * @see     SkillRecommendation
 */
public class SkillRecommendServlet extends BaseServlet {

    /**
     * Name of the web.xml context init-param that holds the Volcano Engine API key.
     * The value must be set in {@code web.xml} as:
     * <pre>
     *   &lt;context-param&gt;
     *     &lt;param-name&gt;volcengine.api.key&lt;/param-name&gt;
     *     &lt;param-value&gt;YOUR_KEY_HERE&lt;/param-value&gt;
     *   &lt;/context-param&gt;
     * </pre>
     */
    private static final String API_KEY_PARAM = "volcengine.api.key";

    /**
     * Name of the context init-param (loaded from {@code local.properties}) that
     * holds the model endpoint ID, e.g. {@code glm-4-7-251222}.
     */
    private static final String MODEL_ID_PARAM = "volcengine.model.id";

    /**
     * Handles GET requests to the skill recommendation page.
     *
     * <p>Workflow:</p>
     * <ol>
     *   <li>Check the user is logged in and has the {@code APPLICANT} role.</li>
     *   <li>Retrieve the target job by {@code jobId} parameter.</li>
     *   <li>Calculate match result between the applicant's skills and job requirements.</li>
     *   <li>If there are missing skills, call the AI service.</li>
     *   <li>Set request attributes and forward to {@code skill-recommend.jsp}.</li>
     * </ol>
     *
     * @param request  the HTTP request; must contain a valid {@code jobId} parameter
     * @param response the HTTP response
     * @throws ServletException if the forward fails
     * @throws IOException      on I/O errors
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setUtf8(request, response);

        if (!requireRole(request, response, "APPLICANT")) {
            return;
        }

        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isBlank()) {
            forwardError(request, response,
                    "No job specified. Please select a job from the job list.",
                    request.getContextPath() + "/jobs");
            return;
        }

        // ---- Load job -------------------------------------------------------
        JobService jobService = new JobService(getServletContext());
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            forwardError(request, response,
                    "The selected job does not exist.",
                    request.getContextPath() + "/jobs");
            return;
        }

        // ---- Load applicant profile -----------------------------------------
        UserRepository userRepository = new UserRepository(getServletContext());
        Applicant applicant = userRepository.findApplicantById(getCurrentUser(request).getId());

        // ---- Run skill match ------------------------------------------------
        MatchService matchService = new MatchService();
        MatchResult matchResult = matchService.calculateMatch(
                applicant.getSkills(), job.getRequiredSkills());

        // ---- Call AI recommendation service ---------------------------------
        List<SkillRecommendation> recommendations;
        String aiStatus = "ok"; // tracks whether AI was used or fallback was applied

        if (matchResult.getMissingSkills().isEmpty()) {
            recommendations = java.util.Collections.emptyList();
            aiStatus = "no-missing";
        } else {
            String apiKey = getServletContext().getInitParameter(API_KEY_PARAM);
            if (apiKey == null || apiKey.isBlank()) {
                // API key not configured – use static fallback without logging a warning
                AiRecommendationService staticService = buildFallbackService();
                recommendations = staticService.recommend(
                        matchResult.getMissingSkills(), job.getTitle(), job.getModuleName());
                aiStatus = "fallback";
            } else {
                try {
                    String modelId = getServletContext().getInitParameter(MODEL_ID_PARAM);
                    AiRecommendationService aiService = new AiRecommendationService(apiKey, modelId);
                    recommendations = aiService.recommend(
                            matchResult.getMissingSkills(), job.getTitle(), job.getModuleName());
                    aiStatus = "ai";
                } catch (Exception e) {
                    // AI call failed at construction level; fall back gracefully
                    AiRecommendationService staticService = buildFallbackService();
                    recommendations = staticService.recommend(
                            matchResult.getMissingSkills(), job.getTitle(), job.getModuleName());
                    aiStatus = "fallback";
                }
            }
        }

        // ---- Populate request attributes ------------------------------------
        request.setAttribute("job", job);
        request.setAttribute("matchResult", matchResult);
        request.setAttribute("recommendations", recommendations);
        request.setAttribute("aiStatus", aiStatus);

        forwardView(request, response, "skill-recommend.jsp");
    }

    /**
     * Creates an {@link AiRecommendationService} instance that will always use the
     * static fallback catalogue (by passing a dummy non-blank key that will cause
     * the API call to fail, triggering fallback internally).
     *
     * <p>A dedicated "fallback-only" constructor variant would be cleaner, but to
     * keep the service's public API simple and to avoid exposing internal state,
     * we construct the service with a placeholder key and let the natural fallback
     * mechanism handle the API failure.</p>
     *
     * @return an {@code AiRecommendationService} configured for static-fallback-only mode
     */
    private AiRecommendationService buildFallbackService() {
        // Pass a deliberately invalid key so the first API call fails and the
        // service falls back to its built-in static catalogue.
        return new AiRecommendationService("__static_fallback__");
    }
}
