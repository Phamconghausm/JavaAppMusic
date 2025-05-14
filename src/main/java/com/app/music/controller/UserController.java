package com.app.music.controller;

import com.app.music.dto.ProfileDTO;
import com.app.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile(@RequestHeader("Authorization") String token) {
        // Loại bỏ tiền tố "Bearer " từ token
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        ProfileDTO profile = userService.getProfile(jwt);
        return ResponseEntity.ok(profile);
    }
}