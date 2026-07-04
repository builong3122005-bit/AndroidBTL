# Thư mục trang_chuyendoi (Màn hình Quy đổi & Chọn tiền tệ)

## Các tệp tin trong thư mục:
- **ConvertActivity.java**: Màn hình chính phục vụ quy đổi tiền tệ. Tích hợp bàn phím ảo xử lý cộng trừ nhân chia trực tiếp và tải tỷ giá trực tuyến.
- **ChonDonViActivity.java**: Màn hình liệt kê danh sách quốc gia/tiền tệ và tìm kiếm để người dùng chọn tiền tệ quy đổi.
- **ThongTinCacQuocGiaADapter.java**: Adapter quản lý việc hiển thị danh sách các quốc gia, hình ảnh cờ quốc kỳ và mã tiền tệ tương ứng trong RecyclerView của trang chọn đơn vị.

## Cách hoạt động:
- `ConvertActivity` gọi API ExchangeRate-API để tải tỷ giá của các đồng tiền. Nếu mất mạng, lớp này sẽ tự động chuyển sang đọc tỷ giá lưu đệm (cache) trong SQLite hoặc sử dụng bảng tỷ giá mặc định ban đầu.
- Khi người dùng bấm vào hình cờ, `ChonDonViActivity` sẽ được hiển thị để người dùng chọn quốc gia. Tệp Adapter giúp hiển thị danh sách này mượt mà và cập nhật danh sách ngay lập tức khi người dùng nhập từ khóa tìm kiếm.
