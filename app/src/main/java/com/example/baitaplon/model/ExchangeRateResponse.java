package com.example.baitaplon.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Phản hồi JSON từ ExchangeRate-API: chứa mã tiền gốc và bảng tỷ giá.
 */
public class ExchangeRateResponse {

    @SerializedName("base_code")
    private String baseCurrency;

    // Map chứa tỷ giá các đồng tiền so với baseCurrency
    @SerializedName("conversion_rates")
    private Map<String, Double> conversionRates;

    public Map<String, Double> getConversionRates() { return conversionRates; }
    public String getBaseCurrency() { return baseCurrency; }
}
