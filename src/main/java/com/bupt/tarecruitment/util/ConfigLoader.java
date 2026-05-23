package com.bupt.tarecruitment.util;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility that loads machine-local configuration overrides from
 * {@code local.properties} at application startup and exposes them
 * as servlet-context init-params.
 *
 * <h2>Loading order (highest priority first)</h2>
 * <ol>
 *   <li><strong>Environment variable</strong> – if {@code VOLCENGINE_API_KEY} is
 *       set in the OS environment, it takes precedence over everything else.</li>
 *   <li><strong>local.properties beside the WAR</strong> – the file is looked up
 *       in the directory that contains the running WAR/exploded web-app root
 *       ({@code catalina.home/../local.properties}).</li>
 *   <li><strong>local.properties on the classpath</strong> – useful during
 *       IDE-embedded Tomcat runs; place the file at the project root and the
 *       Maven resource filter copies it to {@code WEB-INF/classes/}.</li>
 *   <li><strong>web.xml context-param</strong> – the value already set in
 *       {@code web.xml} is used if none of the above are present (fallback to
 *       the placeholder value {@code YOUR_VOLCANO_ENGINE_API_KEY_HERE} which
 *       causes the system to silently use the static resource catalogue).</li>
 * </ol>
 *
 * <h2>Usage</h2>
 * <pre>
 *   // In DataBootstrapListener.contextInitialized:
 *   ConfigLoader.load(sce.getServletContext());
 *
 *   // In any servlet:
 *   String key = getServletContext().getInitParameter("volcengine.api.key");
 * </pre>
 *
 * <h2>Security note</h2>
 * <p>{@code local.properties} is listed in {@code .gitignore} and must never be
 * committed to version control. A {@code local.properties.template} file with a
 * placeholder value is committed instead so that new developers know what to
 * create.</p>
 *
 * @author  Group 71
 * @version 1.0
 */
public final class ConfigLoader {

    private static final Logger LOGGER = Logger.getLogger(ConfigLoader.class.getName());

    /** The properties file name looked up on disk and classpath. */
    private static final String PROPS_FILE = "local.properties";

    /** Property key for the Volcano Engine API key. */
    public static final String KEY_VOLCENGINE_API = "volcengine.api.key";

    /** Property key for the model endpoint ID. */
    public static final String KEY_VOLCENGINE_MODEL = "volcengine.model.id";

    /** Environment variable name that can override the file-based key. */
    private static final String ENV_VOLCENGINE_API = "VOLCENGINE_API_KEY";

    /** Sentinel value written in the template – treated as "not configured". */
    private static final String PLACEHOLDER = "YOUR_VOLCANO_ENGINE_API_KEY_HERE";

    /** Private constructor – utility class. */
    private ConfigLoader() {
    }

    /**
     * Loads {@code local.properties} and injects values into the servlet context
     * as init-params (overriding whatever was set in {@code web.xml}).
     *
     * <p>This method is idempotent: if the context attribute
     * {@code "CONFIG_LOADED"} is already present it returns immediately.</p>
     *
     * @param context the running application's servlet context
     */
    public static void load(ServletContext context) {
        if (context.getAttribute("CONFIG_LOADED") != null) {
            return;
        }

        Properties props = new Properties();

        // 1. Try to read from filesystem (next to the deployed web-app root)
        boolean loadedFromFile = tryLoadFromFilesystem(props, context);

        // 2. Fallback: classpath (e.g. target/classes/local.properties during dev)
        if (!loadedFromFile) {
            tryLoadFromClasspath(props);
        }

        // 3. Override with environment variable (highest priority)
        String envKey = System.getenv(ENV_VOLCENGINE_API);
        if (envKey != null && !envKey.isBlank()) {
            props.setProperty(KEY_VOLCENGINE_API, envKey.trim());
            LOGGER.info("ConfigLoader: Volcano Engine API key loaded from environment variable.");
        }

        // 4. Inject non-placeholder values into the servlet context
        for (String name : props.stringPropertyNames()) {
            String value = props.getProperty(name, "").trim();
            if (!value.isEmpty() && !PLACEHOLDER.equals(value)) {
                context.setInitParameter(name, value);
            }
        }

        context.setAttribute("CONFIG_LOADED", Boolean.TRUE);
    }

    /**
     * Attempts to locate and load {@code local.properties} from the filesystem.
     *
     * <p>The following paths are tried in order:</p>
     * <ol>
     *   <li>The directory returned by {@link ServletContext#getRealPath(String)}
     *       for {@code "/"} – i.e. the web-app root.</li>
     *   <li>The {@code user.dir} system property (JVM working directory).</li>
     * </ol>
     *
     * @param props   properties object to populate
     * @param context servlet context used to resolve the web-app root
     * @return {@code true} if the file was found and loaded successfully
     */
    private static boolean tryLoadFromFilesystem(Properties props, ServletContext context) {
        // candidate 1: web-app root (works for exploded WAR)
        String realPath = context.getRealPath("/");
        if (realPath != null) {
            Path candidate = Paths.get(realPath, PROPS_FILE);
            if (loadFile(props, candidate)) {
                LOGGER.info("ConfigLoader: Loaded " + PROPS_FILE + " from " + candidate);
                return true;
            }
        }

        // candidate 2: JVM working directory (typical IDE run)
        Path candidate = Paths.get(System.getProperty("user.dir", "."), PROPS_FILE);
        if (loadFile(props, candidate)) {
            LOGGER.info("ConfigLoader: Loaded " + PROPS_FILE + " from " + candidate);
            return true;
        }

        return false;
    }

    /**
     * Attempts to load {@code local.properties} from the classpath
     * ({@code WEB-INF/classes/local.properties}).
     *
     * @param props properties object to populate
     */
    private static void tryLoadFromClasspath(Properties props) {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(PROPS_FILE)) {
            if (is != null) {
                props.load(is);
                LOGGER.info("ConfigLoader: Loaded " + PROPS_FILE + " from classpath.");
            }
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "ConfigLoader: Could not load " + PROPS_FILE + " from classpath.", e);
        }
    }

    /**
     * Loads a properties file from the given {@link Path}.
     *
     * @param props     properties object to populate on success
     * @param path      file path to attempt
     * @return {@code true} if the file exists and was read without error
     */
    private static boolean loadFile(Properties props, Path path) {
        if (!Files.isReadable(path)) {
            return false;
        }
        try (InputStream is = Files.newInputStream(path)) {
            props.load(is);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "ConfigLoader: Failed to read " + path, e);
            return false;
        }
    }
}
