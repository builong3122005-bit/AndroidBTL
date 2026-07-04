# Thư mục trang_dangky (Màn hình Đăng ký)

## Các tệp tin trong thư mục:
- **DangKyActivity.java**: Màn hình đăng ký tài khoản thành viên mới cho ứng dụng.

## Cách hoạt động:
- Tiếp nhận thông tin nhập liệu từ người dùng (ID tài khoản, Họ tên, Mật khẩu, Email, Số điện thoại).
- Kiểm tra tính trùng lặp của ID tài khoản thông qua hàm `db.isUserIdExists(id)` trong SQLite để tránh xung đột dữ liệu.
- Lưu tài khoản mới vào cơ sở dữ liệu và gửi thông tin ID & mật khẩu ngược lại cho `MainActivity` qua `Intent` để người dùng không cần gõ lại sau khi đăng ký thành công.
