package com.app.music.service;

import com.app.music.entity.Music;
import com.app.music.repository.MusicRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MusicService {

    private final MusicRepository musicRepository;

    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    public Music uploadMusic(MultipartFile file, String title, String genre, String caption, Boolean isPublic) throws IOException {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());
            String url = blob.signUrl(365, TimeUnit.DAYS).toString();

            Music music = new Music();
            music.setName(fileName);
            music.setUrl(url);
            music.setTitle(title);
            music.setGenre(genre);
            music.setCaption(caption);
            music.setIsPublic(isPublic);

            return musicRepository.save(music);
        } catch (Exception e) {
            throw new IOException("Lỗi khi upload nhạc lên Firebase Storage: " + e.getMessage(), e);
        }
    }

    public List<Music> getAllMusic() {
        return musicRepository.findAll();
    }

    public Optional<Music> getMusic(Long id) {
        return musicRepository.findById(id);
    }

    public List<Music> search(String keyword) {
        return musicRepository.findAll().stream()
                .filter(m -> m.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public void deleteMusic(Long id) throws IOException {
        Music music = musicRepository.findById(id)
                .orElseThrow(() -> new IOException("Không tìm thấy bài hát"));

        // Xóa file trên Firebase Storage
        Bucket bucket = StorageClient.getInstance().bucket();
        boolean deleted = bucket.get(music.getName()).delete();
        if (!deleted) throw new IOException("Xóa file trên Firebase thất bại");

        // Xóa trong database
        musicRepository.delete(music);
    }

    public Music updateMusic(Long id, String title, String genre, String caption, Boolean isPublic) throws IOException {
        Music music = musicRepository.findById(id)
                .orElseThrow(() -> new IOException("Không tìm thấy bài hát"));

        music.setTitle(title);
        music.setGenre(genre);
        music.setCaption(caption);
        music.setIsPublic(isPublic);

        return musicRepository.save(music);
    }


}

