package com.example.baitaplon.trang_dangnhap;

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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baitaplon.R;
import com.example.baitaplon.dulieu.DataBaseHelper_DangKy;
import com.example.baitaplon.model.ThongTinDangKy;
import com.example.baitaplon.trang_chuyendoi.ConvertActivity;
import com.example.baitaplon.trang_dangky.DangKyActivity;

/**
 * Màn hình đăng nhập của ứng dụng. Hỗ trợ đăng nhập tài khoản,
 * liên kết sang trang đăng ký hoặc vào nhanh với tư cách Khách.
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvDangKy;
    private EditText edtUid, edtPassword;
    private TextView tvKhach;
    private Button btnDangNhap;
    private DataBaseHelper_DangKy db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Thiết lập theme Sáng/Tối dựa theo cấu hình đã lưu trong SharedPreferences
        SharedPreferences userPrefs = getSharedPreferences("user_info", MODE_PRIVATE);
        String themeMode = userPrefs.getString("theme_mode", "light");
        if ("dark".equals(themeMode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        
        // Thiết lập padding tự động thích ứng với thanh hệ thống (Status/Navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtUid = findViewById(R.id.edtUid);
        edtPassword = findViewById(R.id.edtPassword);
        tvKhach = findViewById(R.id.tvKhach);
        btnDangNhap = findViewById(R.id.btnLogin);
        
        db = new DataBaseHelper_DangKy(this);

        // Chuyển sang màn hình Đăng ký tài khoản
        tvDangKy = findViewById(R.id.tvdangky);
        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        // Điền sẵn thông tin nếu vừa đăng ký thành công và quay lại
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id");
            String psw = intent.getStringExtra("psw");
            if (id != null && psw != null) {
                edtUid.setText(id);
                edtPassword.setText(psw);
            }
        }

        // Thực hiện đăng nhập và xác thực qua SQLite
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtUid.getText().toString().trim();
                String psw = edtPassword.getText().toString().trim();

                if (id.isEmpty() || psw.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.checkUser(id, psw)) {
                        ThongTinDangKy userInfo = db.getUserInfo(id);
                        if (userInfo != null) {
                            // Lưu thông tin người dùng vào SharedPreferences của phiên làm việc
                            SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", userInfo.getHoTen());
                            editor.putString("uid", userInfo.getUid());
                            editor.putString("hoten", userInfo.getHoTen());
                            editor.putString("matkhau", userInfo.getPsw());
                            editor.putString("email", userInfo.getEmail());
                            editor.putString("sdt", userInfo.getSdt());
                            editor.apply();

                            Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                            Intent convertIntent = new Intent(MainActivity.this, ConvertActivity.class);
                            startActivity(convertIntent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Chế độ Khách (Guest mode): không cần đăng nhập thực tế
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

                Intent convertIntent = new Intent(MainActivity.this, ConvertActivity.class);
                startActivity(convertIntent);
                finish();
            }
        });
    }
}
