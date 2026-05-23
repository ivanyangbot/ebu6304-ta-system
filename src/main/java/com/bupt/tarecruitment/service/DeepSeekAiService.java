package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.config.AiQueryConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Calls the DashScope OpenAI-compatible chat completion API.
 *
 * <p>This service sends the administrator's question together with a compact
 * recruitment-data snapshot to the {@code deepseek-v4-flash} model and returns
 * the model's plain-text answer.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     AiQueryConfig
 * @see     RecruitmentDataSnapshotService
 */
public class DeepSeekAiService {

    private final HttpClient httpClient;

    /**
     * Creates a service instance with default HTTP timeouts.
     */
    public DeepSeekAiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(AiQueryConfig.CONNECT_TIMEOUT_SECONDS))
                .build();
    }

    /**
     * Sends a natural-language question to DeepSeek using the supplied data snapshot.
     *
     * @param question     administrator question entered in the UI
     * @param dataSnapshot compact summary of current JSON-backed system data
     * @return model answer text
     * @throws IOException if the HTTP call fails or the response cannot be parsed
     */
    public String ask(String question, String dataSnapshot) throws IOException {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("Question must not be empty.");
        }

        JsonObject requestBody = buildRequestBody(question.trim(), dataSnapshot);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AiQueryConfig.BASE_URL + "/chat/completions"))
                .timeout(Duration.ofSeconds(AiQueryConfig.READ_TIMEOUT_SECONDS))
                .header("Authorization", "Bearer " + AiQueryConfig.API_KEY)
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("AI API request failed with HTTP " + response.statusCode()
                        + ": " + response.body());
            }
            return extractAnswer(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("AI API request was interrupted.", e);
        }
    }

    /**
     * Builds the OpenAI-compatible request payload.
     *
     * @param question     trimmed administrator question
     * @param dataSnapshot recruitment-data snapshot text
     * @return JSON request body
     */
    private JsonObject buildRequestBody(String question, String dataSnapshot) {
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content",
                "You are an assistant for a university TA recruitment system. "
                        + "Answer briefly in the same language as the user's question. "
                        + "Use only the provided data snapshot. "
                        + "If the snapshot does not contain enough information, say so clearly.");

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", "Data snapshot:\n" + dataSnapshot + "\n\nQuestion: " + question);

        JsonArray messages = new JsonArray();
        messages.add(systemMessage);
        messages.add(userMessage);

        JsonObject body = new JsonObject();
        body.addProperty("model", AiQueryConfig.MODEL);
        body.add("messages", messages);
        body.addProperty("stream", false);
        return body;
    }

    /**
     * Extracts the assistant message content from a chat completion response.
     *
     * @param responseBody raw JSON response from the API
     * @return assistant answer text
     * @throws IOException if the expected content field is missing
     */
    private String extractAnswer(String responseBody) throws IOException {
        JsonObject root = JsonParser.parseString(responseBody).getAsJsonObject();
        if (!root.has("choices") || root.getAsJsonArray("choices").size() == 0) {
            throw new IOException("AI API response did not contain any choices.");
        }

        JsonObject message = root.getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("message");

        if (message == null || !message.has("content") || message.get("content").isJsonNull()) {
            throw new IOException("AI API response did not contain message content.");
        }

        String content = message.get("content").getAsString();
        if (content.trim().isEmpty()) {
            throw new IOException("AI API returned an empty answer.");
        }
        return content.trim();
    }
}
