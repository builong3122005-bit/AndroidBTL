package com.example.baitaplon;

/**
 * ThongTinCacQuocGia - Lớp dữ liệu (Model) đại diện cho thông tin một quốc gia và tiền tệ tương ứng.
 */
public class ThongTinCacQuocGia {
    private String maTienTe; // Mã tiền tệ (VND, USD...)
    private String tenQuocGia; // Tên quốc gia tương ứng
    private int hinhQuocGia; // ID tài nguyên hình ảnh cờ quốc gia

    public ThongTinCacQuocGia(String tenQuocGia, String maTienTe, int hinhQuocGia) {
        this.maTienTe = maTienTe;
        this.tenQuocGia = tenQuocGia;
        this.hinhQuocGia = hinhQuocGia;
    }
    public String getMaTienTe() {
        return maTienTe;
    }
    public String getTenQuocGia() {
        return tenQuocGia;
    }
    public int getHinhQuocGia() {
        return hinhQuocGia;
    }
}
