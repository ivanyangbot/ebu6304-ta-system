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

public class UserRepository {
    private final Path filePath;
    private final Gson gson;

    public UserRepository(ServletContext servletContext) {
        this.filePath = PathUtil.getDataFilePath(servletContext, "users.json");
        this.gson = JsonFileUtil.getGson();
    }

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

    public User findById(String id) {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User findByUsernameAndPassword(String username, String password) {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

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

    public Applicant findApplicantById(String id) {
        User user = findById(id);
        if (user instanceof Applicant) {
            return (Applicant) user;
        }
        return null;
    }

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
}
