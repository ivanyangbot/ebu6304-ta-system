package com.bupt.tarecruitment.util;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for resolving data file paths within the web application.
 *
 * <p>The system stores all entity data (users, jobs, applications) as JSON files
 * inside the {@code WEB-INF/data/} directory of the deployed WAR. This class
 * handles the initialisation of that directory and provides a single entry point
 * for obtaining the {@link Path} to any named data file.</p>
 *
 * <p>Initialisation strategy:</p>
 * <ol>
 *   <li>On the first call to {@link #initializeDataDirectory(ServletContext)},
 *       the {@code WEB-INF/data} directory is located via
 *       {@link ServletContext#getRealPath(String)}.</li>
 *   <li>If {@code getRealPath} returns {@code null} (e.g. when deployed from a
 *       compressed WAR), the servlet temp directory is used as a fallback.</li>
 *   <li>Seed data files bundled in {@code src/main/resources/data/} are copied
 *       to the data directory on first run if they do not already exist.</li>
 *   <li>The resolved directory path is stored as a servlet context attribute
 *       ({@link #DATA_DIR_ATTRIBUTE}) to avoid repeated initialisation.</li>
 * </ol>
 *
 * <p>This class is non-instantiable (utility class pattern).</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     JsonFileUtil
 */
public class PathUtil {

    /**
     * The {@link ServletContext} attribute key under which the resolved data
     * directory path (as a {@code String}) is stored after initialisation.
     */
    public static final String DATA_DIR_ATTRIBUTE = "TA_DATA_DIR";

    /**
     * The set of seed data files that are copied from the classpath to the data
     * directory on first startup.
     */
    private static final String[] DATA_FILES = {"users.json", "jobs.json", "applications.json"};

    /** Private constructor to prevent instantiation. */
    private PathUtil() {
    }

    /**
     * Initialises the data directory for the application.
     *
     * <p>This method is idempotent: if the context attribute
     * {@link #DATA_DIR_ATTRIBUTE} is already set, the method returns immediately
     * without doing any work.</p>
     *
     * @param context the servlet context of the running application
     * @throws RuntimeException if the data directory or any seed file cannot be created
     */
    public static void initializeDataDirectory(ServletContext context) {
        Object existing = context.getAttribute(DATA_DIR_ATTRIBUTE);
        if (existing != null) {
            return;
        }

        try {
            String realPath = context.getRealPath("/WEB-INF/data");
            Path dataDir;
            if (realPath != null) {
                dataDir = Paths.get(realPath);
            } else {
                File tempDir = (File) context.getAttribute("javax.servlet.context.tempdir");
                dataDir = tempDir.toPath().resolve("ta-recruitment-data");
            }

            Files.createDirectories(dataDir);
            for (String fileName : DATA_FILES) {
                Path targetFile = dataDir.resolve(fileName);
                if (!Files.exists(targetFile)) {
                    copySeedFile(fileName, targetFile);
                }
            }

            context.setAttribute(DATA_DIR_ATTRIBUTE, dataDir.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data directory.", e);
        }
    }

    /**
     * Returns the absolute {@link Path} to a named data file inside the data directory.
     *
     * <p>This method ensures the data directory has been initialised before
     * returning the path.</p>
     *
     * @param context  the servlet context
     * @param fileName name of the data file (e.g. {@code "users.json"})
     * @return the absolute path to the requested file
     */
    public static Path getDataFilePath(ServletContext context, String fileName) {
        initializeDataDirectory(context);
        String dir = (String) context.getAttribute(DATA_DIR_ATTRIBUTE);
        return Paths.get(dir, fileName);
    }

    /**
     * Copies a seed data file from the classpath ({@code data/<fileName>}) to
     * the target path. If no classpath resource is found, an empty JSON array
     * file is written instead.
     *
     * @param fileName   name of the seed file (e.g. {@code "users.json"})
     * @param targetFile destination path for the file
     * @throws IOException if the file cannot be copied or created
     */
    private static void copySeedFile(String fileName, Path targetFile) throws IOException {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("data/" + fileName)) {
            if (inputStream == null) {
                Files.writeString(targetFile, "[]", StandardCharsets.UTF_8);
                return;
            }
            Files.copy(inputStream, targetFile);
        }
    }
}
