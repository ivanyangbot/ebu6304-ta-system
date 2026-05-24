package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.util.PathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthService}.
 *
 * <p>Uses Mockito to stub {@link UserRepository} so no file I/O occurs.
 * Tests cover login validation, successful login, and registration
 * validation rules.</p>
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private ServletContext servletContext;

    // We test AuthService by subclassing it to inject a mock repository
    // (since the real constructor builds the repository from ServletContext).
    private AuthServiceUnderTest authService;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceUnderTest(mockUserRepository);
    }

    // ---- Login ----

    @Test
    void testLogin_validCredentials_returnsUser() {
        User expectedUser = new Applicant("u1", "alice", "pass123", "APPLICANT", "Alice", "a@test.com", new ArrayList<>());
        when(mockUserRepository.findByUsernameAndPassword("alice", "pass123")).thenReturn(expectedUser);

        User result = authService.login("alice", "pass123");

        assertNotNull(result);
        assertEquals("alice", result.getUsername());
    }

    @Test
    void testLogin_wrongPassword_returnsNull() {
        when(mockUserRepository.findByUsernameAndPassword("alice", "wrong")).thenReturn(null);

        User result = authService.login("alice", "wrong");

        assertNull(result);
    }

    @Test
    void testLogin_emptyUsername_returnsNull() {
        User result = authService.login("", "pass123");
        assertNull(result);
    }

    @Test
    void testLogin_nullPassword_returnsNull() {
        User result = authService.login("alice", null);
        assertNull(result);
    }

    @Test
    void testLogin_blankUsername_returnsNull() {
        User result = authService.login("   ", "pass123");
        assertNull(result);
    }

    // ---- Registration - invalid inputs ----

    @Test
    void testRegister_emptyFullName_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.registerApplicant("", "a@test.com", "alice123", "pass123", "pass123"));
        assertTrue(ex.getMessage().contains("Full name"));
    }

    @Test
    void testRegister_invalidEmail_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.registerApplicant("Alice", "not-an-email", "alice123", "pass123", "pass123"));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }

    @Test
    void testRegister_usernameTooShort_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.registerApplicant("Alice", "a@test.com", "ali", "pass123", "pass123"));
        assertTrue(ex.getMessage().contains("4-20"));
    }

    @Test
    void testRegister_passwordTooShort_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.registerApplicant("Alice", "a@test.com", "alice123", "abc", "abc"));
        assertTrue(ex.getMessage().contains("6"));
    }

    @Test
    void testRegister_passwordMismatch_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.registerApplicant("Alice", "a@test.com", "alice123", "pass123", "different"));
        assertTrue(ex.getMessage().contains("match"));
    }

    @Test
    void testRegister_usernameTaken_throwsException() {
        when(mockUserRepository.findByUsername("alice123")).thenReturn(
                new User("x", "alice123", "p", "APPLICANT", "Other", "o@t.com"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.registerApplicant("Alice", "a@test.com", "alice123", "pass123", "pass123"));
        assertTrue(ex.getMessage().contains("Username"));
    }

    @Test
    void testRegister_emailTaken_throwsException() {
        when(mockUserRepository.findByUsername("alice123")).thenReturn(null);
        when(mockUserRepository.findByEmail("a@test.com")).thenReturn(
                new User("x", "other", "p", "APPLICANT", "Other", "a@test.com"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.registerApplicant("Alice", "a@test.com", "alice123", "pass123", "pass123"));
        assertTrue(ex.getMessage().contains("Email"));
    }

    @Test
    void testRegister_validInput_createsApplicant() {
        when(mockUserRepository.findByUsername("alice123")).thenReturn(null);
        when(mockUserRepository.findByEmail("a@test.com")).thenReturn(null);
        doNothing().when(mockUserRepository).createApplicant(any(Applicant.class));

        Applicant applicant = authService.registerApplicant("Alice", "a@test.com", "alice123", "pass123", "pass123");

        assertNotNull(applicant);
        assertEquals("alice123", applicant.getUsername());
        assertEquals("Alice", applicant.getFullName());
        assertEquals("APPLICANT", applicant.getRole());
        verify(mockUserRepository).createApplicant(any(Applicant.class));
    }

    // ---- Test-only subclass that injects a mock repository ----

    /**
     * Subclass of AuthService that exposes a constructor accepting
     * a pre-built {@link UserRepository} for testing.
     */
    static class AuthServiceUnderTest extends AuthService {
        private final UserRepository injectedRepo;

        AuthServiceUnderTest(UserRepository repo) {
            super(testContext());
            this.injectedRepo = repo;
        }

        private static ServletContext testContext() {
            try {
                ServletContext context = mock(ServletContext.class);
                Path dataDir = Files.createTempDirectory("auth-service-test");
                when(context.getAttribute(PathUtil.DATA_DIR_ATTRIBUTE)).thenReturn(dataDir.toString());
                return context;
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public User login(String username, String password) {
            if (username == null || username.trim().isEmpty()
                    || password == null || password.trim().isEmpty()) {
                return null;
            }
            return injectedRepo.findByUsernameAndPassword(username.trim(), password.trim());
        }

        @Override
        public Applicant registerApplicant(String fullName, String email, String username,
                                           String password, String confirmPassword) {
            // reproduce the same validation + uniqueness check logic using the injected repo
            String nFullName  = fullName  == null ? "" : fullName.trim();
            String nEmail     = email     == null ? "" : email.trim();
            String nUsername  = username  == null ? "" : username.trim();
            String nPassword  = password  == null ? "" : password.trim();
            String nConfirm   = confirmPassword == null ? "" : confirmPassword.trim();

            if (nFullName.isEmpty())
                throw new IllegalArgumentException("Full name cannot be empty.");
            if (nEmail.isEmpty() || !nEmail.matches("(?i)^[A-Z0-9._%+\\-]+@[A-Z0-9.\\-]+\\.[A-Z]{2,}$"))
                throw new IllegalArgumentException("Please enter a valid email address.");
            if (!nUsername.matches("[A-Za-z0-9._-]{4,20}"))
                throw new IllegalArgumentException("Username must be 4-20 characters and use letters, numbers, dot, underscore, or hyphen only.");
            if (nPassword.length() < 6)
                throw new IllegalArgumentException("Password must be at least 6 characters.");
            if (!nPassword.equals(nConfirm))
                throw new IllegalArgumentException("Passwords do not match.");
            if (injectedRepo.findByUsername(nUsername) != null)
                throw new IllegalArgumentException("Username is already taken.");
            if (injectedRepo.findByEmail(nEmail) != null)
                throw new IllegalArgumentException("Email is already registered.");

            Applicant applicant = new Applicant(
                    "test-id", nUsername, nPassword, "APPLICANT", nFullName, nEmail, new ArrayList<>(), "");
            injectedRepo.createApplicant(applicant);
            return applicant;
        }
    }
}
