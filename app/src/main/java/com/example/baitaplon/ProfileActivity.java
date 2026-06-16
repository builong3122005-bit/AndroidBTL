package com.example.baitaplon;

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

/**
 * ProfileActivity - Màn hình xem thông tin chi tiết hồ sơ cá nhân.
 * Hỗ trợ hiển thị thông tin tài khoản hiện tại, sửa thông tin qua hộp thoại
 * và xóa tài khoản hiện tại.
 */
public class ProfileActivity extends AppCompatActivity {
    // Khai báo các TextView hiển thị thông tin
    private TextView tvUID2, tvHoten2, tvMatkhau2, tvemail2, tvSDT2;
    private ImageView imgBack2;
    private Button btnSua, btnXoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        
        // Căn lề lùi thích ứng thanh hệ thống
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

        // Tải thông tin người dùng từ bộ nhớ SharedPreferences
        loadUserData();

        // Nút quay lại
        imgBack2.setOnClickListener(v -> finish());

        // Nút mở hộp thoại chỉnh sửa
        btnSua.setOnClickListener(v -> showEditDialog());

        // Nút mở hộp thoại xóa tài khoản
        btnXoa.setOnClickListener(v -> showDeleteDialog());
    }

    /**
     * Đọc và hiển thị dữ liệu của người dùng từ SharedPreferences.
     */
    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String userId = sharedPreferences.getString("uid", "Guest");
        String username = sharedPreferences.getString("username", "Guest");
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

    /**
     * Hiển thị hộp thoại Sửa thông tin hồ sơ (Tên, Email, Sđt).
     */
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

        // Hiển thị thông tin cũ lên các ô nhập liệu
        edtName.setText(sharedPreferences.getString("hoten", ""));
        edtEmail.setText(sharedPreferences.getString("email", ""));
        edtPhone.setText(sharedPreferences.getString("sdt", ""));

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSave.setOnClickListener(v -> {
            // Cho phép tài khoản Khách trải nghiệm giao diện nhưng không lưu thực tế
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

            // Lưu thông tin mới vào SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("hoten", newName);
            editor.putString("email", newEmail);
            editor.putString("sdt", newPhone);
            editor.apply();

            Toast.makeText(ProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            loadUserData(); // Cập nhật lại giao diện hiển thị
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * Hiển thị hộp thoại cảnh báo và xác nhận xóa tài khoản.
     */
    private void showDeleteDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");

        // Tài khoản khách không thể xóa
        if (username.equals("Guest")) {
            Toast.makeText(this, "Vui lòng đăng nhập để sử dụng tính năng này!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xóa tài khoản")
                .setMessage("Bạn có chắc chắn muốn xóa tài khoản này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xóa sạch thông tin đăng nhập
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Toast.makeText(ProfileActivity.this, "Xóa tài khoản thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển hướng quay lại màn hình đăng nhập chính
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
