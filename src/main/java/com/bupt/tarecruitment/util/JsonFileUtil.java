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

/**
 * Utility class providing JSON file read/write operations for data persistence.
 *
 * <p>This class is the primary I/O layer of the system. All entities (users, jobs,
 * applications, notifications, activity logs) are stored as JSON arrays in plain
 * {@code .json} files. {@code JsonFileUtil} wraps Gson and provides convenience
 * methods for reading/writing these files safely.</p>
 *
 * <p>A custom {@link LocalDateTimeTypeAdapter} is registered to serialise and
 * deserialise {@link LocalDateTime} values using the ISO-8601 format.</p>
 *
 * <p>Key behaviours:</p>
 * <ul>
 *   <li>If a target file does not exist, it is created as an empty JSON array ({@code []}).</li>
 *   <li>Parent directories are created automatically if they do not exist.</li>
 *   <li>All files are read and written using UTF-8 encoding.</li>
 * </ul>
 *
 * <p>This class is non-instantiable (utility class pattern).</p>
 *
 * @author  Group 71
 * @version 1.0
 */
public class JsonFileUtil {

    /** Formatter used for {@link LocalDateTime} serialisation/deserialisation. */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /** Shared, thread-safe Gson instance with pretty printing and LocalDateTime support. */
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    /** Private constructor to prevent instantiation. */
    private JsonFileUtil() {
    }

    /**
     * Returns the shared Gson instance configured with the system's type adapters.
     *
     * @return the shared {@link Gson} instance
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * Reads a JSON file and returns its contents as a {@link JsonArray}.
     *
     * <p>If the file does not exist it is created as an empty array.
     * If the file exists but is empty, an empty array is returned.</p>
     *
     * @param path path to the JSON file
     * @return the parsed {@link JsonArray}; never {@code null}
     * @throws RuntimeException if the file cannot be read or does not contain a JSON array
     */
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

    /**
     * Reads a JSON file and deserialises its contents into a typed list.
     *
     * <p>If the file does not exist it is created as an empty array.
     * If the file exists but is empty, an empty list is returned.</p>
     *
     * @param <T>  the element type
     * @param path path to the JSON file
     * @param type the Gson {@link Type} token representing {@code List<T>}
     * @return the deserialised list; never {@code null}
     * @throws RuntimeException if the file cannot be read or parsed
     */
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

    /**
     * Serialises {@code data} to JSON and writes it to the specified file.
     *
     * <p>Parent directories are created if they do not exist. Existing file
     * contents are overwritten.</p>
     *
     * @param path the target file path
     * @param data the object to serialise (typically a {@code List<?>})
     * @throws RuntimeException if the file cannot be written
     */
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

    /**
     * Ensures that the specified file and its parent directories exist.
     * If the file does not exist it is initialised with an empty JSON array ({@code []}).
     *
     * @param path the file path to ensure
     * @throws RuntimeException if the file or directories cannot be created
     */
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

    /**
     * Gson {@link TypeAdapter} that serialises and deserialises {@link LocalDateTime}
     * using the ISO-8601 format ({@code yyyy-MM-dd'T'HH:mm:ss}).
     */
    private static class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

        /**
         * Writes a {@link LocalDateTime} as an ISO-8601 string, or a JSON null.
         *
         * @param out   the JSON writer
         * @param value the value to write (may be {@code null})
         * @throws IOException if an I/O error occurs
         */
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.format(FORMATTER));
            }
        }

        /**
         * Reads a {@link LocalDateTime} from an ISO-8601 JSON string, or returns {@code null}
         * for a JSON null or an empty string.
         *
         * @param in the JSON reader
         * @return the parsed {@link LocalDateTime}, or {@code null}
         * @throws IOException if an I/O error occurs
         */
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
