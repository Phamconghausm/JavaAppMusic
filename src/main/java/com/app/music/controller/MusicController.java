package com.app.music.controller;
import com.app.music.entity.Music;
import com.app.music.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Music music = musicService.uploadMusic(file);
        return ResponseEntity.ok(music);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(musicService.getAllMusic());
    }

    @GetMapping("/play/{id}")
    public ResponseEntity<?> play(@PathVariable Long id) {
        return musicService.getMusic(id)
                .map(music -> ResponseEntity.ok(Map.of("url", music.getUrl())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword) {
        return ResponseEntity.ok(musicService.search(keyword));
    }
}

