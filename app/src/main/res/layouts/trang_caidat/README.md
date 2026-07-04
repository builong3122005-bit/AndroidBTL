# Thư mục Giao diện trang_caidat

## Các tệp tin layout:
- **activity_cai_dat.xml**: Layout màn hình Cài đặt ứng dụng. Cung cấp các nút lựa chọn chế độ hiển thị (Sáng/Tối), nút đổi mật khẩu, xem giới thiệu/điều khoản và đăng xuất tài khoản.
- **dialog_about_us.xml**: Layout hộp thoại giới thiệu thông tin nhóm phát triển.
- **dialog_terms_conditions.xml**: Layout hộp thoại điều khoản và điều kiện sử dụng dịch vụ.
- **dialog_doimatkhau.xml**: Layout hộp thoại thay đổi mật khẩu tài khoản (gồm ô nhập mật khẩu cũ, mật khẩu mới và xác nhận mật khẩu mới).

## 🌗 Hỗ trợ Day/Night Theme:
Tất cả các hộp thoại (Dialog) hiển thị thông tin giới thiệu, điều khoản, đổi mật khẩu đều sử dụng màu nền `@color/item_background` và màu chữ `@color/text_primary`. Điều này giúp các hộp thoại tự động bo tròn nền tối và đổi chữ sáng đồng bộ với chế độ Dark Mode của hệ thống.
