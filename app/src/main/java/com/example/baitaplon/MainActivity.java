package com.example.baitaplon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * MainActivity - Màn hình đăng nhập của ứng dụng chuyển đổi tiền tệ.
 * Cho phép đăng nhập bằng tài khoản, liên kết sang màn hình đăng ký hoặc truy cập dưới quyền khách.
 */
public class MainActivity extends AppCompatActivity {

    // Khai báo các view thành phần giao diện
    private TextView tvDangKy;
    private EditText edtUid, edtPassword;
    private TextView tvKhach;
    private Button btnDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Kích hoạt giao diện Edge-to-Edge để tối ưu hóa hiển thị toàn màn hình
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        
        // Thiết lập Padding tự động thích ứng với thanh hệ thống (Status Bar / Navigation Bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần từ XML layout sang Java code
        edtUid = findViewById(R.id.edtUid);
        edtPassword = findViewById(R.id.edtPassword);
        tvKhach = findViewById(R.id.tvKhach);
        btnDangNhap = findViewById(R.id.btnLogin);

        // Sự kiện click nút "Đăng ký" -> Chuyển sang màn hình Đăng ký (DangKyActivity)
        tvDangKy = findViewById(R.id.tvdangky);
        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        // Kiểm tra xem có dữ liệu tài khoản tự điền gửi từ trang đăng ký về hay không
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id");
            String psw = intent.getStringExtra("psw");
            if (id != null && psw != null) {
                edtUid.setText(id);
                edtPassword.setText(psw);
            }
        }

        // Sự kiện click nút "Đăng nhập" -> Lưu thông tin giả lập & Chuyển sang màn hình Chuyển đổi (ConvertActivity)
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtUid.getText().toString();
                String psw = edtPassword.getText().toString();

                // Kiểm tra ô nhập liệu trống
                if (id.isEmpty() || psw.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                } else {
                    // Lưu thông tin người dùng giả lập vào SharedPreferences để dùng ở trang Cài đặt / Hồ sơ
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", id);
                    editor.putString("uid", id);
                    editor.putString("hoten", "Hoàng Đình Tới");
                    editor.putString("matkhau", psw);
                    editor.putString("email", id + "@gmail.com");
                    editor.putString("sdt", "0123456789");
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang trang chính (Chuyển đổi tiền tệ)
                    Intent convertIntent = new Intent(MainActivity.this, ConvertActivity.class);
                    startActivity(convertIntent);
                    finish(); // Kết thúc MainActivity để không quay lại được khi bấm back
                }
            }
        });

        // Sự kiện đăng nhập với quyền Khách (Guest) -> Lưu thông tin Khách & Chuyển sang màn hình Chuyển đổi
        tvKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", "Guest");
                editor.putString("uid", "Guest");
                editor.putString("hoten", "Tài khoản khách");
                editor.putString("matkhau", "");
                editor.putString("email", "guest@example.com");
                editor.putString("sdt", "0000000000");
                editor.apply();

                Toast.makeText(MainActivity.this, "Đăng nhập dưới tư cách Khách!", Toast.LENGTH_SHORT).show();

                // Chuyển sang trang chính (Chuyển đổi tiền tệ)
                Intent convertIntent = new Intent(MainActivity.this, ConvertActivity.class);
                startActivity(convertIntent);
                finish();
            }
        });
    }
}