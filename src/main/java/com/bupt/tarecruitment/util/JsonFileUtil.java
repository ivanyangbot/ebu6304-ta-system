package com.bupt.tarecruitment.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonFileUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    private JsonFileUtil() {
    }

    public static Gson getGson() {
        return GSON;
    }

    public static JsonArray readJsonArray(Path path) {
        ensureFileExists(path);

        try {
            String content = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) {
                return new JsonArray();
            }

            JsonElement element = JsonParser.parseString(content);
            if (!element.isJsonArray()) {
                throw new RuntimeException("Invalid JSON array format in file: " + path);
            }
            return element.getAsJsonArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + path, e);
        }
    }

    public static <T> List<T> readList(Path path, Type type) {
        ensureFileExists(path);

        try {
            String content = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) {
                return new ArrayList<>();
            }

            List<T> list = GSON.fromJson(content, type);
            return list == null ? new ArrayList<>() : list;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON file: " + path, e);
        }
    }

    public static void writeJson(Path path, Object data) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, GSON.toJson(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON file: " + path, e);
        }
    }

    private static void ensureFileExists(Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            if (!Files.exists(path)) {
                Files.writeString(path, "[]", StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to prepare JSON file: " + path, e);
        }
    }

    private static class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.format(FORMATTER));
            }
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String value = in.nextString();
            if (value == null || value.isEmpty()) {
                return null;
            }
            return LocalDateTime.parse(value, FORMATTER);
        }
    }
}
