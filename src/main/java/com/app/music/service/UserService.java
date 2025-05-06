package com.app.music.service;

import com.app.music.dto.ProfileDTO;
import com.app.music.entity.User;
import com.app.music.repository.UserRepository;
import com.app.music.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileDTO getProfile(String token) {
        // Trích xuất username từ token JWT
        String username = jwtService.extractUsername(token);
        if (username == null) {
            throw new RuntimeException("Token không hợp lệ");
        }

        // Tìm người dùng theo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new ProfileDTO(user.getId(), user.getUsername());
    }
}