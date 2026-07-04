# Thư mục Giao diện trang_tintuc

## Các tệp tin layout:
- **activity_news.xml**: Layout màn hình danh sách tin tức kinh tế. Chứa Toolbar tích hợp menu lựa chọn loại tin (Trong nước / Quốc tế) và RecyclerView hiển thị các bài viết.
- **item_news.xml**: Layout định nghĩa giao diện một bài viết tin tức (ảnh thumbnail đại diện, tiêu đề bài viết, mô tả tóm tắt ngắn và ngày giờ xuất bản).

## 🌗 Hỗ trợ Day/Night Theme:
Cả hai tệp giao diện này đều sử dụng các tài nguyên màu động (`@color/background_main`, `@color/item_background`, `@color/text_primary`, `@color/text_secondary`) giúp toàn bộ màn hình tin tức và từng thẻ bài viết tự động chuyển màu nền tối/chữ sáng mượt mà khi người dùng bật chế độ Dark Mode.
