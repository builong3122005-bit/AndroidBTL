# Thư mục trang_hoso (Màn hình Hồ sơ cá nhân)

## Các tệp tin trong thư mục:
- **ProfileActivity.java**: Màn hình hiển thị chi tiết thông tin cá nhân của tài khoản đang đăng nhập (ID, Họ tên, Email, Số điện thoại).

## Cách hoạt động:
- Đọc thông tin của người dùng hiện tại từ tệp tin lưu trữ tạm thời `SharedPreferences` để hiển thị trực tiếp lên màn hình.
- Nút "Sửa" mở ra một Dialog cho phép người dùng thay đổi họ tên, email, sđt. Thông tin mới được lưu đè lại vào cả SQLite và `SharedPreferences`.
- Nút "Xóa" thực hiện xóa tài khoản này vĩnh viễn khỏi SQLite, đồng thời xóa sạch phiên làm việc của người dùng hiện tại và chuyển hướng quay trở lại màn hình đăng nhập.
