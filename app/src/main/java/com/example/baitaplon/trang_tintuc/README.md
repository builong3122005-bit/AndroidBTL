# Thư mục trang_tintuc (Màn hình Tin tức Tài chính)

## Các tệp tin trong thư mục:
- **NewsActivity.java**: Màn hình hiển thị danh sách tin tức. Cho phép chuyển đổi xem tin trong nước (tiếng Việt) và tin kinh tế quốc tế (tiếng Anh).
- **NewsAdapter.java**: Adapter quản lý hiển thị các bài viết tin tức gồm tiêu đề, mô tả ngắn, ngày đăng và ảnh đại diện. Tự động chuyển hướng mở trình duyệt ngoài khi người dùng click xem chi tiết bài viết.

## Cách hoạt động:
- `NewsActivity` khởi tạo luồng chạy phụ (`Thread`) để tải tin tức:
  - Tin trong nước: gọi tiện ích quét thông tin Jsoup từ CafeF và VietNamBiz.
  - Tin quốc tế: gọi API của trang NewsAPI.org thông qua giao thức Retrofit.
- Thư viện `Glide` được sử dụng để tải ảnh đại diện bài viết không đồng bộ, giúp danh sách cuộn mượt mà mà không lo nghẽn mạng.
- Tích hợp sẵn dữ liệu tin tức dự phòng nếu thiết bị không có kết nối internet hoặc API hết lượt gọi.
