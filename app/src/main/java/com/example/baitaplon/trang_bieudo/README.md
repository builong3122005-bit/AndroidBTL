# Thư mục trang_bieudo (Màn hình Biểu đồ Nến)

## Các tệp tin trong thư mục:
- **BieuDoActivity.java**: Màn hình hiển thị biểu đồ nến (Candlestick Chart) mô phỏng biến động lịch sử tỷ giá theo thời gian.

## Cách hoạt động:
- Lớp này sử dụng thư viện `CSVReader` để đọc tệp tin dữ liệu lịch sử lưu cục bộ (`FileDuLieu.csv`).
- Sau khi lọc lấy dữ liệu của cặp tiền tệ được yêu cầu (ví dụ: USD/VND), nó trích xuất các giá trị Open, High, Low, Close (OHLC) của từng ngày để cấu hình cho biểu đồ nến `CandleStickChart` thuộc thư viện `MPAndroidChart`.
- Biểu đồ được trang bị các hiệu ứng vẽ nến động, hỗ trợ thu phóng pinch zoom và kéo vuốt xem lịch sử giao dịch rất mượt mà.
