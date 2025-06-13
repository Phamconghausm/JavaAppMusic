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
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("genre") String genre,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "public", required = false, defaultValue = "false") Boolean isPublic
    ) throws IOException {
        Music music = musicService.uploadMusic(file, title, genre, caption, isPublic);
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            musicService.deleteMusic(id);
            return ResponseEntity.ok("Xóa bài hát thành công");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa bài hát: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String genre,
            @RequestParam(required = false) String caption,
            @RequestParam(defaultValue = "false") Boolean isPublic
    ) {
        try {
            Music updated = musicService.updateMusic(id, title, genre, caption, isPublic);
            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

}

