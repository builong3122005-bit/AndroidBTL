package com.example.baitaplon.trang_hoso;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baitaplon.R;
import com.example.baitaplon.dulieu.DataBaseHelper_DangKy;
import com.example.baitaplon.model.ThongTinDangKy;
import com.example.baitaplon.trang_dangnhap.MainActivity;

/**
 * Màn hình thông tin hồ sơ người dùng.
 * Cho phép xem chi tiết, cập nhật thông tin cá nhân và xóa tài khoản vĩnh viễn khỏi SQLite.
 */
public class ProfileActivity extends AppCompatActivity {
    private TextView tvUID2, tvHoten2, tvMatkhau2, tvemail2, tvSDT2;
    private ImageView imgBack2;
    private Button btnSua, btnXoa;
    private DataBaseHelper_DangKy db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvUID2 = findViewById(R.id.tvUID2);
        tvHoten2 = findViewById(R.id.tvHoten2);
        tvMatkhau2 = findViewById(R.id.tvMatkhau2);
        tvemail2 = findViewById(R.id.tvemail2);
        tvSDT2 = findViewById(R.id.tvSDT2);
        imgBack2 = findViewById(R.id.imgBack2);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);

        db = new DataBaseHelper_DangKy(this);

        loadUserData();

        imgBack2.setOnClickListener(v -> finish());
        btnSua.setOnClickListener(v -> showEditDialog());
        btnXoa.setOnClickListener(v -> showDeleteDialog());
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String userId = sharedPreferences.getString("uid", "Guest");
        String hoten = sharedPreferences.getString("hoten", "Tài khoản khách");
        String matkhau = sharedPreferences.getString("matkhau", "");
        String email = sharedPreferences.getString("email", "guest@example.com");
        String sdt = sharedPreferences.getString("sdt", "0000000000");

        tvUID2.setText(userId);
        tvHoten2.setText(hoten);
        tvMatkhau2.setText(matkhau.isEmpty() ? "******" : matkhau);
        tvemail2.setText(email);
        tvSDT2.setText(sdt);
    }

    // Hộp thoại chỉnh sửa thông tin họ tên, email, sđt
    private void showEditDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_suahoso);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setAttributes(layoutParams);
        }

        final EditText edtName = dialog.findViewById(R.id.edtName);
        final EditText edtEmail = dialog.findViewById(R.id.edtEmail);
        final EditText edtPhone = dialog.findViewById(R.id.edtPhone);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        edtName.setText(sharedPreferences.getString("hoten", ""));
        edtEmail.setText(sharedPreferences.getString("email", ""));
        edtPhone.setText(sharedPreferences.getString("sdt", ""));

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSave.setOnClickListener(v -> {
            if (username.equals("Guest")) {
                Toast.makeText(ProfileActivity.this, "Tài khoản Khách chỉ xem giao diện, không thực sự sửa hồ sơ!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }

            String newName = edtName.getText().toString().trim();
            String newEmail = edtEmail.getText().toString().trim();
            String newPhone = edtPhone.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
                Toast.makeText(ProfileActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = sharedPreferences.getString("uid", "");
            String password = sharedPreferences.getString("matkhau", "");
            db.updateThongTinDangKy(new ThongTinDangKy(userId, newName, password, newEmail, newPhone));

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("hoten", newName);
            editor.putString("username", newName);
            editor.putString("email", newEmail);
            editor.putString("sdt", newPhone);
            editor.apply();

            Toast.makeText(ProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            loadUserData(); 
            dialog.dismiss();
        });

        dialog.show();
    }

    // Xác nhận xóa tài khoản khỏi SQLite và SharedPreferences
    private void showDeleteDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");

        if (username.equals("Guest")) {
            Toast.makeText(this, "Vui lòng đăng nhập để sử dụng tính năng này!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xóa tài khoản")
                .setMessage("Bạn có chắc chắn muốn xóa tài khoản này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    String userId = sharedPreferences.getString("uid", "");
                    db.deleteUser(userId);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Toast.makeText(ProfileActivity.this, "Xóa tài khoản thành công!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
