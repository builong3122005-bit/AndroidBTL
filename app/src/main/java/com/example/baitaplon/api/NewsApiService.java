package com.example.baitaplon.api;

import com.example.baitaplon.model.NewsApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface gọi API tin tức từ NewsAPI.org.
 */
public interface NewsApiService {
    @GET("v2/everything")
    Call<NewsApiResponse> getNews(
        @Query("q") String query,
        @Query("apiKey") String apiKey
    );
}
