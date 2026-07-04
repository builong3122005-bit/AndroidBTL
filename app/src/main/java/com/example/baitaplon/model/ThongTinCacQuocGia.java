package com.example.baitaplon.model;

/**
 * Lưu trữ thông tin tiền tệ của một quốc gia: tên, mã tiền, hình cờ.
 */
public class ThongTinCacQuocGia {
    private String maTienTe;    // VND, USD, JPY...
    private String tenQuocGia;
    private int hinhQuocGia;    // Resource ID của ảnh cờ

    public ThongTinCacQuocGia(String tenQuocGia, String maTienTe, int hinhQuocGia) {
        this.maTienTe = maTienTe;
        this.tenQuocGia = tenQuocGia;
        this.hinhQuocGia = hinhQuocGia;
    }

    public String getMaTienTe() { return maTienTe; }
    public String getTenQuocGia() { return tenQuocGia; }
    public int getHinhQuocGia() { return hinhQuocGia; }
}
