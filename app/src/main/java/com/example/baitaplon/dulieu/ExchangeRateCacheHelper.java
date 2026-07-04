package com.example.baitaplon.dulieu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Quản lý bộ nhớ đệm (cache) tỷ giá tiền tệ ngoại tuyến dưới dạng SQLite.
 */
public class ExchangeRateCacheHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "exchangerates.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tblExchangeRates";
    private static final String COL_BASE = "base_currency";       // Đồng tiền gốc làm gốc quy đổi
    private static final String COL_TARGET = "target_currency";   // Đồng tiền đích nhận tỷ giá
    private static final String COL_RATE = "rate";                // Tỷ giá

    public ExchangeRateCacheHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Sử dụng khóa chính kết hợp (base_currency, target_currency)
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_BASE + " TEXT, " +
                COL_TARGET + " TEXT, " +
                COL_RATE + " REAL, " +
                "PRIMARY KEY (" + COL_BASE + ", " + COL_TARGET + "))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Lưu đè hoặc thêm mới tỉ giá
    public void saveRate(String base, String target, double rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BASE, base);
        values.put(COL_TARGET, target);
        values.put(COL_RATE, rate);
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // Lấy tỉ giá đã lưu trong cache khi không có mạng
    public Double getRate(String base, String target) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_RATE + " FROM " + TABLE_NAME + " WHERE " + COL_BASE + "=? AND " + COL_TARGET + "=?";
        try (Cursor cursor = db.rawQuery(query, new String[]{base, target})) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
