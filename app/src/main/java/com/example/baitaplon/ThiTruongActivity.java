package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * ThiTruongActivity - Màn hình hiển thị thông tin thị trường tài chính.
 * Hiển thị thời gian hiện tại và hỗ trợ điều hướng trang.
 */
public class ThiTruongActivity extends AppCompatActivity {
    // Khai báo các view hiển thị ngày giờ và nút quay lại
    private TextView txtThoiGian, txtDate;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thi_truong);
        
        // Cấu hình padding thích ứng thanh trạng thái
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        txtThoiGian = findViewById(R.id.txtThoiGian);
        txtDate = findViewById(R.id.txtDate);
        imgBack = findViewById(R.id.imgBack);

        // Sự kiện click nút quay lại
        if (imgBack != null) {
            imgBack.setOnClickListener(v -> finish());
        }

        // Lấy thông tin ngày và giờ hiện tại từ hệ thống để hiển thị
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = timeFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());

        txtThoiGian.setText(time);
        txtDate.setText(date);

        // Thiết lập thanh điều hướng dưới (Bottom Navigation)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.bottom_market);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_convert) {
                    Intent intent = new Intent(ThiTruongActivity.this, ConvertActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.bottom_market) {
                    return true; // Đang ở trang Thị trường
                } else if (itemId == R.id.bottom_news) {
                    Intent intent = new Intent(ThiTruongActivity.this, NewsActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.bottom_profile) {
                    Intent intent = new Intent(ThiTruongActivity.this, CaiDatActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            });
        }
    }
}
