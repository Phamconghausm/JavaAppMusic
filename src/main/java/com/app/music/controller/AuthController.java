package com.app.music.controller;
import com.app.music.dto.LoginRequest;
import com.app.music.dto.RegisterRequest;
import com.app.music.repository.UserRepository;
import com.app.music.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;

    @PostMapping("/oauth/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String accessToken = payload.get("access_token");
        try {
            String jwt = authService.loginWithGoogle(accessToken);
            return ResponseEntity.ok(Collections.singletonMap("token", jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
        }
    }

    @PostMapping("/oauth/facebook")
    public ResponseEntity<?> facebookLogin(@RequestBody Map<String, String> payload) {
        String accessToken = payload.get("access_token");
        try {
            String jwt = authService.loginWithFacebook(accessToken);
            return ResponseEntity.ok(Collections.singletonMap("token", jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Facebook token or permission denied");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            // Trả về lỗi 409 Conflict và message rõ ràng
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Tên đăng nhập đã tồn tại");
        }
        authService.register(request);
        return ResponseEntity.ok("Register success");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }
}

