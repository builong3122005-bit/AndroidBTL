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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * BieuDoActivity - Màn hình hiển thị chi tiết biểu đồ nến phân tích tỉ giá (placeholder).
 * Nhận thông tin tỉ giá truyền từ màn hình chính để hiển thị thông tin biến động.
 */
public class BieuDoActivity extends AppCompatActivity {
    private TextView txtThoiGian, txtDate, tvBienDongGia;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bieu_do);
        
        // Căn lề tự động theo thanh hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtThoiGian = findViewById(R.id.txtThoiGian);
        txtDate = findViewById(R.id.txtDate);
        tvBienDongGia = findViewById(R.id.tvBienDongGia);
        imgBack = findViewById(R.id.imgBack);

        // Nhận dữ liệu tỉ giá hiện tại từ Intent truyền từ ConvertActivity sang
        String bienDongGia = getIntent().getStringExtra("BienDongGia");
        if (bienDongGia != null && !bienDongGia.isEmpty()) {
            tvBienDongGia.setText(bienDongGia);
        } else {
            tvBienDongGia.setText("1 USD = 25,400 VND"); // Giá trị mặc định
        }

        // Lấy thông tin ngày giờ hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());

        txtThoiGian.setText(time);
        txtDate.setText(date);

        // Sự kiện click nút quay lại
        if (imgBack != null) {
            imgBack.setOnClickListener(v -> finish());
        }
    }
}
