    package com.app.music.entity;

    import jakarta.persistence.*;

    @Entity
    @Table(name = "music")
    public class Music {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @Lob
        @Column(columnDefinition = "TEXT")
        private String url;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

