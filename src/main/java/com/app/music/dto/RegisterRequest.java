package com.app.music.dto;

import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @Size(min = 3, message = "Username phải có ít nhất 3 ký tự")
    private String username;

    @Size(min = 8, message = "Password phải có ít nhất 8 ký tự")
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}