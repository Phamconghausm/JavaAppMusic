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

    public Music uploadMusic(MultipartFile file) throws IOException {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());
            String url = blob.signUrl(365, TimeUnit.DAYS).toString();

            Music music = new Music();
            music.setName(fileName);
            music.setUrl(url);
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


}

