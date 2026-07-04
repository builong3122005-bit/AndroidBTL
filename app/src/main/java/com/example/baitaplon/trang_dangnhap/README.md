# Thư mục trang_dangnhap (Màn hình Đăng nhập)

## Các tệp tin trong thư mục:
- **MainActivity.java**: Màn hình quản lý đăng nhập người dùng vào hệ thống.

## Cách hoạt động:
- Cho phép người dùng nhập ID và mật khẩu để xác thực tài khoản qua cơ sở dữ liệu SQLite (`DataBaseHelper_DangKy`).
- Hỗ trợ điền tự động ID và mật khẩu nếu người dùng vừa mới đăng ký tài khoản thành công từ trang đăng ký.
- Cho phép truy cập nhanh bằng tài khoản "Khách" (Guest Mode) để sử dụng giao diện mà không cần tạo tài khoản.
- Lưu thông tin phiên đăng nhập hiện tại vào `SharedPreferences` để các màn hình khác như cài đặt và hồ sơ cá nhân có thể đọc và sử dụng.
