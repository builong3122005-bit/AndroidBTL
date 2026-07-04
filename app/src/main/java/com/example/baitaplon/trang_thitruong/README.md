# Thư mục trang_thitruong (Màn hình Bảng Tỷ giá Thị trường)

## Các tệp tin trong thư mục:
- **ThiTruongActivity.java**: Màn hình hiển thị danh sách bảng biến động tỷ giá các đồng tiền so với đồng USD tiêu chuẩn kèm đồng hồ hiển thị thời gian hiện tại.
- **ExchangeRateAdapter.java**: Adapter xử lý hiển thị danh sách dòng tỷ giá quốc gia và lọc tìm kiếm danh sách tỷ giá theo tên nước hoặc mã tiền tệ.

## Cách hoạt động:
- `ThiTruongActivity` gọi API để tải tỷ giá của các đồng tiền so với USD, cập nhật kết quả lên UI và tự động ghi đè cache tỷ giá vào SQLite để phục vụ chế độ offline.
- Adapter sử dụng cơ chế `Filterable` để người dùng có thể gõ chữ trên ô tìm kiếm và lọc danh sách hiển thị ngay lập tức mà không cần gọi lại API.
- Bấm vào một dòng tỷ giá bất kỳ sẽ mở ra màn hình biểu đồ nến `BieuDoActivity` tương ứng của cặp tiền tệ đó.
