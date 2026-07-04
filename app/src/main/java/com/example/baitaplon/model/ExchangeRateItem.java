package com.example.baitaplon.model;

/**
 * Một dòng tỉ giá trong bảng thị trường: mã tiền, tên nước, cờ, tỉ giá so với USD.
 */
public class ExchangeRateItem {
    private String currencyCode;    // VND, JPY...
    private String countryName;
    private int flagResId;          // Resource ID ảnh cờ
    private double rateAgainstUsd;  // Tỉ giá so với 1 USD

    public ExchangeRateItem(String currencyCode, String countryName, int flagResId, double rateAgainstUsd) {
        this.currencyCode = currencyCode;
        this.countryName = countryName;
        this.flagResId = flagResId;
        this.rateAgainstUsd = rateAgainstUsd;
    }

    public String getCurrencyCode() { return currencyCode; }
    public String getCountryName() { return countryName; }
    public int getFlagResId() { return flagResId; }
    public double getRateAgainstUsd() { return rateAgainstUsd; }
    public void setRateAgainstUsd(double rateAgainstUsd) { this.rateAgainstUsd = rateAgainstUsd; }
}
