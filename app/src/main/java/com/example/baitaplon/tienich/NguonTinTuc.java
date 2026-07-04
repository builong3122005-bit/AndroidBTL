package com.example.baitaplon.tienich;

import com.example.baitaplon.api.ApiClient_News_en;
import com.example.baitaplon.api.NewsApiService;
import com.example.baitaplon.model.NewsApiResponse;
import com.example.baitaplon.model.NewsArticle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Lớp cào dữ liệu tin tức kinh tế từ Jsoup (CafeF, VietNamBiz) và NewsAPI.
 */
public class NguonTinTuc {
    private static final String API_KEY = "d0aeb270d25b4018ad66b6e1b73f9ff0";

    // Cào bài viết từ VietNamBiz
    public static List<NewsArticle> fetchVietNamBiz() {
        List<NewsArticle> articles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://vietnambiz.vn/tai-chinh.htm").get();
            Elements newsList = doc.select("div.item");
            for (Element news : newsList) {
                Element aTag = news.selectFirst("div.image > a");
                String link = aTag != null ? aTag.attr("href") : "";
                if (!link.startsWith("http") && !link.isEmpty()) link = "https://vietnambiz.vn" + link;

                String desc = aTag != null ? Jsoup.parse(aTag.attr("title")).text() : "";

                Element h3 = news.selectFirst("div.content > h3.title");
                String title = h3 != null ? Jsoup.parse(h3.html()).text() : "";

                Element timeDiv = news.selectFirst("div.content > div.time");
                String pubDate = timeDiv != null ? Jsoup.parse(timeDiv.html()).text() : "";

                Element img = news.selectFirst("img.img120x80");
                String imgUrl = (img != null) ? img.attr("src") : "";

                if (!title.isEmpty() && !link.isEmpty() && !imgUrl.isEmpty()) {
                    articles.add(new NewsArticle(title, desc, pubDate, imgUrl, link));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    // Cào bài viết từ CafeF
    public static List<NewsArticle> fetchCafeF() {
        List<NewsArticle> articles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://cafef.vn/thi-truong-chung-khoan.chn")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36")
                    .timeout(10000)
                    .get();

            Elements newsList = doc.select("div.tlitem");
            for (Element news : newsList) {
                Element aTag = news.selectFirst("a.avatar.img-resize");
                String link = aTag != null ? aTag.attr("href") : "";
                if (!link.startsWith("http") && !link.isEmpty()) link = "https://cafef.vn" + link;

                Element img = aTag != null ? aTag.selectFirst("img") : null;
                String imgUrl = img != null ? img.attr("src") : "";

                String title = aTag != null ? aTag.attr("title") : "";

                Element timeSpan = news.selectFirst("span.time");
                String pubDate = timeSpan != null ? timeSpan.text() : "";

                String desc = aTag != null ? Jsoup.parse(aTag.attr("title")).text() : "";

                if (!title.isEmpty() && !link.isEmpty() && !imgUrl.isEmpty()) {
                    articles.add(new NewsArticle(title, desc, pubDate, imgUrl, link));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    // Gọi API lấy tin kinh tế quốc tế tiếng Anh
    public static List<NewsArticle> apiNews_en() {
        List<NewsArticle> articles = new ArrayList<>();
        try {
            NewsApiService apiService = ApiClient_News_en.getClient().create(NewsApiService.class);
            Response<NewsApiResponse> response = apiService.getNews("economy", API_KEY).execute();

            if (response.isSuccessful() && response.body() != null) {
                List<NewsApiResponse.Article> apiArticles = response.body().getArticles();
                for (NewsApiResponse.Article art : apiArticles) {
                    articles.add(new NewsArticle(
                            art.getTitle(),
                            art.getDescription(),
                            art.getPublishedAt(),
                            art.getUrlToImage(),
                            art.getUrl()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }
}
