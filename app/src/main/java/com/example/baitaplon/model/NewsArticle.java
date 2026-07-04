package com.example.baitaplon.model;

/**
 * Thông tin một bài báo tin tức: tiêu đề, mô tả, ngày đăng, ảnh, link gốc.
 */
public class NewsArticle {
    private String title;
    private String description;
    private String publishedAt;
    private String imageUrl;
    private String link;        // Link tới bài báo gốc

    public NewsArticle(String title, String description, String publishedAt, String imageUrl, String link) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.imageUrl = imageUrl;
        this.link = link;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPublishedAt() { return publishedAt; }
    public String getImageUrl() { return imageUrl; }
    public String getLink() { return link; }
}
