# Thư mục dulieu (Quản lý Cơ sở dữ liệu SQLite)

## Các tệp tin trong thư mục:
- **DataBaseHelper_DangKy.java**: Quản lý cơ sở dữ liệu `dangky.db` chứa bảng `tblDangKy` để phục vụ chức năng đăng nhập, đăng ký, chỉnh sửa hồ sơ và đổi mật khẩu người dùng.
- **ExchangeRateCacheHelper.java**: Quản lý cơ sở dữ liệu bộ nhớ đệm `exchangerates.db` chứa bảng `tblExchangeRates` để lưu trữ tỷ giá USD quy đổi của các nước phục vụ chế độ offline ngoại tuyến.

## Cách hoạt động:
- Cả hai lớp đều kế thừa từ `SQLiteOpenHelper`.
- `DataBaseHelper_DangKy` cung cấp các phương thức CRUD tài khoản: `addUserDangKy`, `getUserInfo`, `updateThongTinDangKy`, `deleteUser`, và các phương thức xác thực `checkUser`, `isUserIdExists`.
- `ExchangeRateCacheHelper` cung cấp hai phương thức cốt lõi: `saveRate` (Lưu hoặc cập nhật tỷ giá) và `getRate` (Truy xuất tỷ giá đã lưu).
