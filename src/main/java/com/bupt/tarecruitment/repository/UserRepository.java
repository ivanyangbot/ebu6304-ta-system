package com.bupt.tarecruitment.repository;

import com.bupt.tarecruitment.model.Admin;
import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.MO;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.util.JsonFileUtil;
import com.bupt.tarecruitment.util.PathUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Data-access repository for {@link com.bupt.tarecruitment.model.User} entities.
 *
 * <p>All users (applicants, MOs, and admins) are stored as a single JSON array
 * in {@code users.json}. Each entry carries a {@code role} field that is used
 * to deserialise the record into the correct subclass
 * ({@link com.bupt.tarecruitment.model.Applicant},
 * {@link com.bupt.tarecruitment.model.MO}, or
 * {@link com.bupt.tarecruitment.model.Admin}).</p>
 *
 * <p>Write operations ({@code create*}, {@code update*}, {@code delete*}) are
 * synchronised on the class monitor to avoid concurrent write conflicts in a
 * multi-threaded servlet environment.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.AuthService
 */
public class UserRepository {
    private final Path filePath;
    private final Gson gson;

    public UserRepository(ServletContext servletContext) {
        this.filePath = PathUtil.getDataFilePath(servletContext, "users.json");
        this.gson = JsonFileUtil.getGson();
    }

    /**
     * Returns all users in the system.
     *
     * @return list of all {@link User} objects (may include {@link com.bupt.tarecruitment.model.Applicant},
     *         {@link com.bupt.tarecruitment.model.MO}, and {@link com.bupt.tarecruitment.model.Admin})
     */
    public List<User> findAll() {
        JsonArray jsonArray = JsonFileUtil.readJsonArray(filePath);
        List<User> users = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            JsonObject object = element.getAsJsonObject();
            String role = object.has("role") ? object.get("role").getAsString() : "";
            if ("APPLICANT".equalsIgnoreCase(role)) {
                users.add(gson.fromJson(object, Applicant.class));
            } else if ("MO".equalsIgnoreCase(role)) {
                users.add(gson.fromJson(object, MO.class));
            } else if ("ADMIN".equalsIgnoreCase(role)) {
                users.add(gson.fromJson(object, Admin.class));
            } else {
                users.add(gson.fromJson(object, User.class));
            }
        }

        return users;
    }

    public List<User> searchByFullName(String searchName) {
        if (searchName == null || searchName.trim().isEmpty()) {
            return findAll();
        }

        String normalizedSearchName = searchName.trim().toLowerCase(Locale.ROOT);
        List<User> allUsers = findAll();
        List<User> matchedUsers = new ArrayList<>();

        for (User user : allUsers) {
            if (user.getFullName() != null
                    && user.getFullName().trim().toLowerCase(Locale.ROOT).contains(normalizedSearchName)) {
                matchedUsers.add(user);
            }
        }

        return matchedUsers;
    }

    /**
     * Finds a user by their unique ID.
     *
     * @param id the user ID to search for
     * @return the matching {@link User}, or {@code null} if not found
     */
    public User findById(String id) {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds a user by username and password (used for login).
     *
     * @param username the login username
     * @param password the account password
     * @return the matching {@link User}, or {@code null} if credentials do not match
     */
    public User findByUsernameAndPassword(String username, String password) {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds a user by username (case-insensitive).
     *
     * @param username the username to look up; {@code null} returns {@code null}
     * @return the matching {@link User}, or {@code null} if not found
     */
    public User findByUsername(String username) {
        if (username == null) {
            return null;
        }

        String normalizedUsername = username.trim().toLowerCase(Locale.ROOT);
        List<User> users = findAll();
        for (User user : users) {
            if (user.getUsername() != null
                    && user.getUsername().trim().toLowerCase(Locale.ROOT).equals(normalizedUsername)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds a user by email address (case-insensitive).
     *
     * @param email the email to look up; {@code null} returns {@code null}
     * @return the matching {@link User}, or {@code null} if not found
     */
    public User findByEmail(String email) {
        if (email == null) {
            return null;
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        List<User> users = findAll();
        for (User user : users) {
            if (user.getEmail() != null
                    && user.getEmail().trim().toLowerCase(Locale.ROOT).equals(normalizedEmail)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds an applicant by their unique ID.
     *
     * @param id the user ID to look up
     * @return the matching {@link com.bupt.tarecruitment.model.Applicant},
     *         or {@code null} if not found or if the user is not an applicant
     */
    public Applicant findApplicantById(String id) {
        User user = findById(id);
        if (user instanceof Applicant) {
            return (Applicant) user;
        }
        return null;
    }

    /**
     * Returns all users with the {@code APPLICANT} role.
     *
     * @return list of {@link com.bupt.tarecruitment.model.Applicant} objects; never {@code null}
     */
    public List<Applicant> findAllApplicants() {
        List<User> users = findAll();
        List<Applicant> applicants = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Applicant) {
                applicants.add((Applicant) user);
            }
        }
        return applicants;
    }

    /**
     * Persists a new applicant account.
     * Checks username and email uniqueness under a class-level lock.
     *
     * @param applicant the applicant to create
     * @throws IllegalArgumentException if the username or email is already taken
     */
    public void createApplicant(Applicant applicant) {
        synchronized (UserRepository.class) {
            List<User> users = findAll();
            for (User user : users) {
                if (user.getUsername() != null
                        && user.getUsername().trim().equalsIgnoreCase(applicant.getUsername().trim())) {
                    throw new IllegalArgumentException("Username is already taken.");
                }
                if (user.getEmail() != null
                        && user.getEmail().trim().equalsIgnoreCase(applicant.getEmail().trim())) {
                    throw new IllegalArgumentException("Email is already registered.");
                }
            }
            users.add(applicant);
            JsonFileUtil.writeJson(filePath, users);
        }
    }

    /**
     * Updates an existing applicant's profile in place.
     *
     * @param updatedApplicant the applicant with updated fields (must have the same ID)
     * @throws RuntimeException if no applicant with the given ID is found
     */
    public void updateApplicant(Applicant updatedApplicant) {
        synchronized (UserRepository.class) {
            List<User> users = findAll();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(updatedApplicant.getId())) {
                    users.set(i, updatedApplicant);
                    JsonFileUtil.writeJson(filePath, users);
                    return;
                }
            }
        }
        throw new RuntimeException("Applicant not found: " + updatedApplicant.getId());
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the ID of the user to delete
     * @throws RuntimeException if no user with the given ID is found
     */
    public void deleteUserById(String id) {
        synchronized (UserRepository.class) {
            List<User> users = findAll();
            boolean removed = users.removeIf(user -> user.getId().equals(id));
            if (!removed) {
                throw new RuntimeException("User not found: " + id);
            }
            JsonFileUtil.writeJson(filePath, users);
        }
    }

    public void updatePassword(String userId, String newPassword) {
        synchronized (UserRepository.class) {
            List<User> users = findAll();
            for (User user : users) {
                if (user.getId().equals(userId)) {
                    user.setPassword(newPassword);
                    JsonFileUtil.writeJson(filePath, users);
                    return;
                }
            }
            throw new RuntimeException("User not found: " + userId);
        }
    }

    public void createMO(MO mo) {
        synchronized (UserRepository.class) {
            List<User> users = findAll();
            for (User user : users) {
                if (user.getUsername() != null
                        && user.getUsername().trim().equalsIgnoreCase(mo.getUsername().trim())) {
                    throw new IllegalArgumentException("Username is already taken.");
                }
                if (user.getEmail() != null
                        && user.getEmail().trim().equalsIgnoreCase(mo.getEmail().trim())) {
                    throw new IllegalArgumentException("Email is already registered.");
                }
            }
            users.add(mo);
            JsonFileUtil.writeJson(filePath, users);
        }
    }

    public void createAdmin(Admin admin) {
        synchronized (UserRepository.class) {
            List<User> users = findAll();
            for (User user : users) {
                if (user.getUsername() != null
                        && user.getUsername().trim().equalsIgnoreCase(admin.getUsername().trim())) {
                    throw new IllegalArgumentException("Username is already taken.");
                }
                if (user.getEmail() != null
                        && user.getEmail().trim().equalsIgnoreCase(admin.getEmail().trim())) {
                    throw new IllegalArgumentException("Email is already registered.");
                }
            }
            users.add(admin);
            JsonFileUtil.writeJson(filePath, users);
        }
    }

    public List<MO> findAllMOs() {
        List<User> users = findAll();
        List<MO> mos = new ArrayList<>();
        for (User user : users) {
            if (user instanceof MO) {
                mos.add((MO) user);
            }
        }
        return mos;
    }

    public List<Admin> findAllAdmins() {
        List<User> users = findAll();
        List<Admin> admins = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Admin) {
                admins.add((Admin) user);
            }
        }
        return admins;
    }
}
