package com.example.baitaplon.api;

import com.example.baitaplon.model.ExchangeRateResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface gọi API tỷ giá từ ExchangeRate-API.
 */
public interface ExchangeRateApi {
    // Lấy tỷ giá mới nhất dựa theo đồng tiền gốc (VD: USD)
    @GET("latest/{baseCurrency}")
    Call<ExchangeRateResponse> getExchangeRates(@Path("baseCurrency") String baseCurrency);
}
