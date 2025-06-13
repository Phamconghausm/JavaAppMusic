package com.app.music.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "music")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Tên file thực tế

    private String title; // Tiêu đề bài hát

    private String genre;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String url;

    private String caption;

    private Boolean isPublic;

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
}