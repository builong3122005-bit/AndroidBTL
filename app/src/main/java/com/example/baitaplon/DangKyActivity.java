package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * DangKyActivity - Màn hình đăng ký tài khoản mới.
 * Cho phép người dùng điền thông tin và truyền ngược thông tin về màn hình đăng nhập để tự động điền.
 */
public class DangKyActivity extends AppCompatActivity {

    // Khai báo các EditText nhập liệu và nút đăng ký
    private EditText edtUid, edtHoten, edtPsw, edtEmail, edtSdt;
    private Button btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        
        // Căn lề tự động thích ứng với thanh hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view từ XML sang mã Java
        edtUid = findViewById(R.id.edtuid2);
        edtHoten = findViewById(R.id.edthoten);
        edtPsw = findViewById(R.id.edtpsw2);
        edtEmail = findViewById(R.id.edtemail);
        edtSdt = findViewById(R.id.edtsdt);
        btnDangKy = findViewById(R.id.btndangky);

        // Đăng ký sự kiện click cho nút Đăng ký
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtUid.getText().toString();
                String hoTen = edtHoten.getText().toString();
                String psw = edtPsw.getText().toString();
                String email = edtEmail.getText().toString();
                String sdt = edtSdt.getText().toString();

                // Xác thực xem có ô nhập liệu nào trống hay không
                if (id.isEmpty() || hoTen.isEmpty() || psw.isEmpty() || email.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(DangKyActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển hướng quay trở về màn hình đăng nhập (MainActivity) kèm theo thông tin tài khoản vừa đăng ký
                    Intent intent = new Intent(DangKyActivity.this, MainActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("psw", psw);
                    setResult(RESULT_OK, intent);
                    startActivity(intent);
                    finish(); // Kết thúc DangKyActivity
                }
            }
        });
    }
}
