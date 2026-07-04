package com.example.baitaplon.trang_dangky;

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

import com.example.baitaplon.R;
import com.example.baitaplon.dulieu.DataBaseHelper_DangKy;
import com.example.baitaplon.model.ThongTinDangKy;
import com.example.baitaplon.trang_dangnhap.MainActivity;

/**
 * Màn hình đăng ký tài khoản mới. Cho phép người dùng đăng ký và lưu trữ vào SQLite.
 */
public class DangKyActivity extends AppCompatActivity {

    private EditText edtUid, edtHoten, edtPsw, edtEmail, edtSdt;
    private Button btnDangKy;
    private DataBaseHelper_DangKy db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        
        // Thiết lập lùi màn hình tự động tương thích với thanh trạng thái hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtUid = findViewById(R.id.edtuid2);
        edtHoten = findViewById(R.id.edthoten);
        edtPsw = findViewById(R.id.edtpsw2);
        edtEmail = findViewById(R.id.edtemail);
        edtSdt = findViewById(R.id.edtsdt);
        btnDangKy = findViewById(R.id.btndangky);
        
        db = new DataBaseHelper_DangKy(this);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtUid.getText().toString().trim();
                String hoTen = edtHoten.getText().toString().trim();
                String psw = edtPsw.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();

                // Xác thực các ô nhập liệu
                if (id.isEmpty() || hoTen.isEmpty() || psw.isEmpty() || email.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(DangKyActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } 
                // Kiểm tra xem ID tài khoản đã có ai đăng ký chưa
                else if (db.isUserIdExists(id)) {
                    Toast.makeText(DangKyActivity.this, "Tài khoản ID đã tồn tại!", Toast.LENGTH_SHORT).show();
                } 
                // Lưu tài khoản mới và điều hướng về trang đăng nhập
                else {
                    db.addUserDangKy(new ThongTinDangKy(id, hoTen, psw, email, sdt));
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // Gửi ngược ID và mật khẩu để tự động điền ở trang đăng nhập
                    Intent intent = new Intent(DangKyActivity.this, MainActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("psw", psw);
                    setResult(RESULT_OK, intent);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
