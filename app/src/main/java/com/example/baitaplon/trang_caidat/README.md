# Thư mục trang_caidat (Màn hình Cài đặt & Dialog)

## Các tệp tin trong thư mục:
- **CaiDatActivity.java**: Màn hình cài đặt cho phép thay đổi giao diện, đổi mật khẩu, xem giới thiệu và đăng xuất.
- **AboutUsDialog.java**: Hộp thoại (Dialog) hiển thị thông tin giới thiệu ngắn về ứng dụng và đội ngũ phát triển Nhóm 3.
- **TermsDialog.java**: Hộp thoại hiển thị thông tin Điều khoản và Điều kiện sử dụng dịch vụ của ứng dụng.

## Cách hoạt động:
- `CaiDatActivity` cho phép chuyển đổi chế độ giao diện: sáng (Light Mode) hoặc tối (Dark Mode). Lệnh chuyển theme được ghi nhận vào `SharedPreferences` và kích hoạt qua `AppCompatDelegate.setDefaultNightMode()`.
- Dialog Đổi mật khẩu được mở lên dưới dạng hộp thoại phụ, kiểm tra mật khẩu cũ khớp với mật khẩu phiên làm việc và gọi hàm `updatePassword()` của SQLite để cập nhật mật khẩu mới.
