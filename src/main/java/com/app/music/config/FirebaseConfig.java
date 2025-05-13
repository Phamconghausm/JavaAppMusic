package com.app.music.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {

        String firebaseConfig = System.getenv("FIREBASE_CONFIG");

        if (firebaseConfig == null || firebaseConfig.isEmpty()) {
            throw new RuntimeException("FIREBASE_CONFIG environment variable is not set.");
        }

        ByteArrayInputStream serviceAccount = new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("appmusic-2f719.firebasestorage.app")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
