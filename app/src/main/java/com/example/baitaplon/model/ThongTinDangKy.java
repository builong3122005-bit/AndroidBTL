package com.example.baitaplon.model;

/**
 * Lớp chứa thông tin tài khoản đăng ký, ánh xạ với bảng tblDangKy trong SQLite.
 */
public class ThongTinDangKy {
    public String uid;      // ID đăng nhập
    public String hoTen;    // Họ tên người dùng
    public String psw;      // Mật khẩu
    public String email;
    public String sdt;      // Số điện thoại

    public ThongTinDangKy(String uid, String hoTen, String psw, String email, String sdt){
        this.uid = uid;
        this.hoTen = hoTen;
        this.psw = psw;
        this.email = email;
        this.sdt = sdt;
    }

    public void setEmail(String email) { this.email = email; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setPsw(String psw) { this.psw = psw; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getUid() { return uid; }
    public String getHoTen() { return hoTen; }
    public String getPsw() { return psw; }
    public String getEmail() { return email; }
    public String getSdt() { return sdt; }
}
