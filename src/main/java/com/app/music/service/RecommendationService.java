package com.app.music.service;


import com.app.music.entity.Music;
import com.app.music.repository.MusicRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RecommendationService {

    private final MusicRepository musicRepository;

    public RecommendationService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    public List<Music> recommend(String token) {
        // Tạm thời chưa dùng token, chỉ random nhạc
        List<Music> all = musicRepository.findAll();
        Collections.shuffle(all); // Giả lập AI bằng cách xáo trộn
        return all.stream().limit(5).toList();
    }
}

