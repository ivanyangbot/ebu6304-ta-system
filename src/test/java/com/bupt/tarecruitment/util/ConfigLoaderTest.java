package com.bupt.tarecruitment.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.servlet.ServletContext;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link ConfigLoader} local configuration loading.
 *
 * <p>The tests use a temporary web root and mocked servlet context to verify
 * property injection, placeholder filtering, and idempotent loading.</p>
 */
class ConfigLoaderTest {
    @TempDir
    Path webRoot;

    /**
     * Verifies concrete filesystem properties are injected as context parameters.
     *
     * @throws Exception if writing the temporary properties file fails
     */
    @Test
    void load_injectsNonPlaceholderFilesystemProperties() throws Exception {
        Files.writeString(webRoot.resolve("local.properties"), String.join(System.lineSeparator(),
                "volcengine.api.key=test-api-key",
                "volcengine.model.id=test-model"));
        ServletContext context = mock(ServletContext.class);
        when(context.getRealPath("/")).thenReturn(webRoot.toString());

        ConfigLoader.load(context);

        verify(context).setInitParameter("volcengine.api.key", "test-api-key");
        verify(context).setInitParameter("volcengine.model.id", "test-model");
        verify(context).setAttribute("CONFIG_LOADED", Boolean.TRUE);
    }

    /**
     * Verifies placeholder and blank property values are not injected.
     *
     * @throws Exception if writing the temporary properties file fails
     */
    @Test
    void load_skipsPlaceholderAndBlankValues() throws Exception {
        Files.writeString(webRoot.resolve("local.properties"), String.join(System.lineSeparator(),
                "volcengine.api.key=YOUR_VOLCANO_ENGINE_API_KEY_HERE",
                "volcengine.model.id=   "));
        ServletContext context = mock(ServletContext.class);
        when(context.getRealPath("/")).thenReturn(webRoot.toString());

        ConfigLoader.load(context);

        verify(context, never()).setInitParameter("volcengine.api.key", "YOUR_VOLCANO_ENGINE_API_KEY_HERE");
        verify(context, never()).setInitParameter("volcengine.model.id", "");
        verify(context).setAttribute("CONFIG_LOADED", Boolean.TRUE);
    }

    /**
     * Verifies repeated loading exits before reading filesystem configuration again.
     */
    @Test
    void load_returnsImmediatelyWhenAlreadyLoaded() {
        ServletContext context = mock(ServletContext.class);
        when(context.getAttribute("CONFIG_LOADED")).thenReturn(Boolean.TRUE);

        ConfigLoader.load(context);

        verify(context, never()).getRealPath("/");
        verify(context, never()).setInitParameter("volcengine.api.key", "test-api-key");
    }
}
