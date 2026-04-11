package com.bupt.tarecruitment.model;

public class Admin extends User {
    public Admin() {
    }

    public Admin(String id, String username, String password, String role, String fullName, String email) {
        super(id, username, password, role, fullName, email);
    }
}
