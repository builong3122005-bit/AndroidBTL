package com.example.baitaplon.dulieu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.baitaplon.model.ThongTinDangKy;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý cơ sở dữ liệu đăng ký và đăng nhập của người dùng.
 */
public class DataBaseHelper_DangKy extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dangky.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tblDangKy";
    private static final String COL_ID = "Id";            // ID đăng nhập của người dùng
    private static final String COL_NAME = "name";         // Họ tên hiển thị
    private static final String COL_PSW = "password";      // Mật khẩu đăng nhập
    private static final String COL_EMAIL = "email";       // Email
    private static final String COL_SDT = "sdt";           // Số điện thoại

    public DataBaseHelper_DangKy(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " TEXT PRIMARY KEY, " +
                COL_NAME + " TEXT, " +
                COL_PSW + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_SDT + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Thêm tài khoản mới sau khi đăng ký thành công
    public long addUserDangKy(ThongTinDangKy thongTinDangKy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID, thongTinDangKy.getUid());
        values.put(COL_NAME, thongTinDangKy.getHoTen());
        values.put(COL_PSW, thongTinDangKy.getPsw());
        values.put(COL_EMAIL, thongTinDangKy.getEmail());
        values.put(COL_SDT, thongTinDangKy.getSdt());
        return db.insert(TABLE_NAME, null, values);
    }

    // Lấy chi tiết thông tin tài khoản qua ID để điền vào trang cá nhân
    public ThongTinDangKy getUserInfo(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{userId})) {
            if (cursor != null && cursor.moveToFirst()) {
                String uid = cursor.getString(0);
                String name = cursor.getString(1);
                String psw = cursor.getString(2);
                String email = cursor.getString(3);
                String sdt = cursor.getString(4);

                return new ThongTinDangKy(uid, name, psw, email, sdt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách toàn bộ người dùng (phục vụ mục đích quản lý nếu cần)
    public List<ThongTinDangKy> getAllThongTin() {
        List<ThongTinDangKy> thongTinDangKyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                thongTinDangKyList.add(new ThongTinDangKy(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PSW)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SDT))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return thongTinDangKyList;
    }

    // Cập nhật thông tin họ tên, email, sđt khi người dùng chỉnh sửa hồ sơ
    public int updateThongTinDangKy(ThongTinDangKy thongTinDangKy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, thongTinDangKy.getHoTen());
        values.put(COL_PSW, thongTinDangKy.getPsw());
        values.put(COL_EMAIL, thongTinDangKy.getEmail());
        values.put(COL_SDT, thongTinDangKy.getSdt());
        return db.update(TABLE_NAME, values, COL_ID + "=?", new String[]{thongTinDangKy.getUid()});
    }

    // Xác thực tài khoản đăng nhập xem ID và password có đúng không
    public boolean checkUser(String userID, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + "=? AND " + COL_PSW + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{userID, password});

        boolean isValid = cursor.moveToFirst();
        cursor.close();
        return isValid;
    }

    // Kiểm tra xem ID đã tồn tại chưa để tránh trùng lặp khi người dùng đăng ký mới
    public boolean isUserIdExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COL_ID + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{id});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Đổi mật khẩu tài khoản
    public boolean updatePassword(String userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PSW, newPassword);
        int rows = db.update(TABLE_NAME, values, COL_ID + "=?", new String[]{userId});
        return rows > 0;
    }

    // Xóa tài khoản người dùng vĩnh viễn khỏi SQLite
    public int deleteUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + "=?", new String[]{userId});
    }
}
