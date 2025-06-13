package com.app.music.service;


import com.app.music.dto.LoginRequest;
import com.app.music.dto.RegisterRequest;
import com.app.music.entity.User;
import com.app.music.repository.UserRepository;
import com.app.music.security.JwtService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    public String loginWithGoogle(String accessToken) throws Exception {
        // Gửi access token đến Google để lấy thông tin người dùng
        URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Invalid access token");
        }

        InputStream responseStream = conn.getInputStream();
        String response = new BufferedReader(new InputStreamReader(responseStream))
                .lines().collect(Collectors.joining());

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        String email = json.get("email").getAsString();  // Google trả về email
        String name = json.get("name").getAsString();

        // Dùng username là email
        User user = userRepository.findByUsername(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setName(name);
            newUser.setProvider("google");
            newUser.setPassword(null); // không cần password nếu là Google
            return userRepository.save(newUser);
        });

        return jwtService.generateToken(user.getUsername());
    }

    public String loginWithFacebook(String accessToken) throws Exception {
        // Facebook Graph API lấy thông tin user
        // Lấy email, name
        URL url = new URL("https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Invalid Facebook access token");
        }

        InputStream responseStream = conn.getInputStream();
        String response = new BufferedReader(new InputStreamReader(responseStream))
                .lines().collect(Collectors.joining());

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        // Facebook có thể không trả email nếu user không cho phép, cần kiểm tra
        String email = json.has("email") ? json.get("email").getAsString() : null;
        String name = json.has("name") ? json.get("name").getAsString() : null;

        if (email == null) {
            throw new RuntimeException("Email permission is required");
        }

        User user = userRepository.findByUsername(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setName(name);
            newUser.setProvider("facebook");
            newUser.setPassword(null);
            return userRepository.save(newUser);
        });

        return jwtService.generateToken(user.getUsername());
    }

    public void register(RegisterRequest req) {
        
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setProvider("local");
        user.setName(req.getUsername());
        userRepository.save(user);
    }

    public String login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!"local".equals(user.getProvider())) {
            throw new BadCredentialsException("Use OAuth to login this account");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return jwtService.generateToken(user.getUsername());
    }
}
