# Thư mục trang_chao (Màn hình Chào)

## Các tệp tin trong thư mục:
- **SplashActivity.java**: Màn hình xuất hiện đầu tiên khi mở ứng dụng. Hiển thị logo/thương hiệu trong 2 giây rồi tự chuyển tiếp đến màn hình đăng nhập.

## Cách hoạt động:
- Lớp này sử dụng đối tượng `Handler` để tạo độ trễ 2000 mili giây (2 giây). Sau khi hết thời gian, nó sử dụng `Intent` để mở màn hình `MainActivity` (Trang đăng nhập) và gọi hàm `finish()` để xóa màn hình chào khỏi danh sách ngăn xếp, giúp người dùng không thể quay lại màn hình chào khi nhấn phím back.
