package com.example.baitaplon.model;

import java.util.List;

/**
 * Phản hồi JSON từ NewsAPI: chứa trạng thái và danh sách bài viết.
 */
public class NewsApiResponse {
    private String status;
    private int totalResults;
    private List<Article> articles;

    public List<Article> getArticles() { return articles; }

    /**
     * Một bài viết chi tiết từ NewsAPI.
     */
    public static class Article {
        private String title;
        private String description;
        private String publishedAt;
        private String urlToImage;
        private String url;

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPublishedAt() { return publishedAt; }
        public String getUrlToImage() { return urlToImage; }
        public String getUrl() { return url; }
    }
}
