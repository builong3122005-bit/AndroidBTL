package com.example.baitaplon;

/**
 * NewsArticle - Lớp dữ liệu (Model) biểu diễn cho một bài viết tin tức.
 */
public class NewsArticle {
    private String title; // Tiêu đề bài viết
    private String description; // Tóm tắt mô tả nội dung
    private String publishedAt; // Thời gian đăng bài viết
    private String link; // Đường dẫn liên kết đến bài báo gốc

    public NewsArticle(String title, String description, String publishedAt, String link) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getLink() {
        return link;
    }
}
