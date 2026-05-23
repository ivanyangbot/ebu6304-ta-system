package com.bupt.tarecruitment.util;

import java.util.UUID;

/**
 * Utility class for generating unique entity identifiers.
 *
 * <p>Generated IDs follow the format {@code "<prefix>-<8-char-hex>"},
 * where the 8-character hex suffix is derived from a random UUID.
 * For example: {@code "job-a3f2c1d4"}, {@code "app-7b0e9f12"}.</p>
 *
 * <p>This class is non-instantiable (utility class pattern).</p>
 *
 * @author  Group 71
 * @version 1.0
 */
public class IdUtil {

    /** Private constructor to prevent instantiation. */
    private IdUtil() {
    }

    /**
     * Generates a unique ID with the given prefix.
     *
     * <p>The ID is formed by appending a hyphen and the first 8 characters
     * of a random UUID (with hyphens stripped) to the provided prefix.</p>
     *
     * @param prefix a short string that identifies the type of entity
     *               (e.g. {@code "job"}, {@code "app"}, {@code "applicant"})
     * @return a unique ID string in the format {@code "<prefix>-<8-char-hex>"}
     */
    public static String generateId(String prefix) {
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return prefix + "-" + randomPart;
    }
}
