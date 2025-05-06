package com.app.music.dto;

public class ProfileDTO {
    private Long id;
    private String username;

    public ProfileDTO(Long id, String username) {
        this.username = username;
        this.id = id;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
