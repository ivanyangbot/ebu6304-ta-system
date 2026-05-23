package com.bupt.tarecruitment.config;

/**
 * Configuration constants for the Admin AI data-query feature.
 *
 * <p>The feature calls Alibaba Cloud DashScope through its OpenAI-compatible
 * endpoint and uses the {@code deepseek-v4-flash} model. These values are kept
 * in one place so the Servlet layer does not hard-code API details.</p>
 *
 * <p><strong>Note:</strong> The API key below is intentionally embedded for this
 * coursework demo because the key has no remaining quota. Do not reuse this
 * pattern in production systems.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.DeepSeekAiService
 */
public final class AiQueryConfig {

    /** DashScope OpenAI-compatible base URL. */
    public static final String BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";

    /** DashScope API key supplied for this demo environment. */
    public static final String API_KEY = "sk-426565cb64ac49c3bc588cbd74307c03";

    /** Required model name for AI queries. */
    public static final String MODEL = "deepseek-v4-flash";

    /** HTTP connect timeout in seconds. */
    public static final int CONNECT_TIMEOUT_SECONDS = 15;

    /** HTTP read timeout in seconds. */
    public static final int READ_TIMEOUT_SECONDS = 60;

    private AiQueryConfig() {
    }
}
