package com.bupt.tarecruitment.util;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {
    public static final String DATA_DIR_ATTRIBUTE = "TA_DATA_DIR";
    private static final String[] DATA_FILES = {"users.json", "jobs.json", "applications.json"};

    private PathUtil() {
    }

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

    public static Path getDataFilePath(ServletContext context, String fileName) {
        initializeDataDirectory(context);
        String dir = (String) context.getAttribute(DATA_DIR_ATTRIBUTE);
        return Paths.get(dir, fileName);
    }

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
