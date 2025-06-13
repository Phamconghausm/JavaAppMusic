package com.app.music.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // Có thể là email nếu dùng Google/Facebook

    private String password; // Có thể để null nếu là OAuth

    private String provider; // "local", "google", "facebook"

    private String name; // Tên hiển thị (từ Google hoặc Facebook)

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProvider() {
        return provider;
    }

    public String getName() {
        return name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setName(String name) {
        this.name = name;
    }
}


