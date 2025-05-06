package com.app.music.controller;


import com.app.music.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<?> recommend(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(recommendationService.recommend(token));
    }
}
