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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * CaiDatActivity - Màn hình Cài đặt ứng dụng.
 * Quản lý các tùy chọn: xem/sửa hồ sơ cá nhân, chọn giao diện, đổi mật khẩu,
 * xem điều khoản, thông tin giới thiệu và đăng xuất tài khoản.
 */
public class CaiDatActivity extends AppCompatActivity {

    // Khai báo các view trong giao diện cài đặt
    private TextView Logout, txtUser, tvTerms, tvAboutUs;
    private ImageView imgAccount, imgTerms, imgAboutUs;
    private Button btnGray, btnWhite;
    private ImageView imgLogout, imgThemes, imgLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cai_dat);
        
        // Thiết lập lề thích ứng thanh trạng thái
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Ánh xạ các view từ XML layout
        txtUser = findViewById(R.id.txtUser);
        imgAccount = findViewById(R.id.imgAccount);
        btnWhite = findViewById(R.id.btnWhite);
        btnGray = findViewById(R.id.btnGrey);
        Logout = findViewById(R.id.txtLogout);
        imgLogout = findViewById(R.id.imgLogout);
        tvTerms = findViewById(R.id.tvTerms);
        imgTerms = findViewById(R.id.imgTerms);
        imgAboutUs = findViewById(R.id.imgAbout);
        tvAboutUs = findViewById(R.id.tvAbout);
        imgThemes = findViewById(R.id.imgThemes);
        imgLanguages = findViewById(R.id.imgLanguages);

        // Thiết lập thanh điều hướng Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        // Đọc tên người dùng từ SharedPreferences và hiển thị
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");
        txtUser.setText(username);

        // Sự kiện click Đổi mật khẩu
        TextView tvChangePassword = findViewById(R.id.tvChangePassword);
        tvChangePassword.setOnClickListener(v -> showChangePasswordDialog());

        // Sự kiện click Đăng xuất
        Logout.setOnClickListener(v -> performLogout());
        imgLogout.setOnClickListener(v -> performLogout());

        // Thiết lập sự kiện chọn tab trên Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_profile) {
                return true; // Đang ở màn hình Cài đặt
            } else if (itemId == R.id.bottom_convert) {
                Intent intent = new Intent(CaiDatActivity.this, ConvertActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.bottom_market) {
                Intent intent = new Intent(CaiDatActivity.this, ThiTruongActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.bottom_news) {
                Intent intent = new Intent(CaiDatActivity.this, NewsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Click vào tên/avatar để mở màn hình hồ sơ (ProfileActivity)
        txtUser.setOnClickListener(v -> openProfile());
        imgAccount.setOnClickListener(v -> openProfile());

        // Thông báo cho các chức năng đang phát triển bằng tiếng Việt
        btnGray.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());
        btnWhite.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());

        TextView tvThemes = findViewById(R.id.textView12);
        TextView tvLanguages = findViewById(R.id.textView24);

        tvThemes.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());
        imgThemes.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());

        tvLanguages.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());
        imgLanguages.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());

        // Mở hộp thoại Điều khoản sử dụng
        tvTerms.setOnClickListener(v -> TermsDialog.show(CaiDatActivity.this));
        imgTerms.setOnClickListener(v -> TermsDialog.show(CaiDatActivity.this));

        // Mở hộp thoại Về chúng tôi
        tvAboutUs.setOnClickListener(v -> AboutUsDialog.show(CaiDatActivity.this));
        imgAboutUs.setOnClickListener(v -> AboutUsDialog.show(CaiDatActivity.this));
    }

    /**
     * Thực hiện đăng xuất tài khoản: xóa dữ liệu SharedPreferences và quay về trang đăng nhập.
     */
    private void performLogout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(CaiDatActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Mở màn hình chi tiết hồ sơ cá nhân.
     */
    private void openProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String userId = sharedPreferences.getString("username", "Guest");

        Intent intent = new Intent(CaiDatActivity.this, ProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    /**
     * Hiển thị hộp thoại Đổi mật khẩu.
     */
    private void showChangePasswordDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_doimatkhau);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setAttributes(layoutParams);
        }

        EditText edtOld = dialog.findViewById(R.id.edtOldpassword);
        EditText edtNew = dialog.findViewById(R.id.edtNewpassword);
        EditText edtConfirm = dialog.findViewById(R.id.edtConfirmnp);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnOk = dialog.findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnOk.setOnClickListener(v -> {
            // Cho phép tài khoản Khách trải nghiệm giao diện nhưng không lưu thực tế
            if (username.equals("Guest")) {
                Toast.makeText(this, "Tài khoản Khách chỉ xem giao diện, không thực sự đổi mật khẩu!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }

            String oldPass = edtOld.getText().toString().trim();
            String newPass = edtNew.getText().toString().trim();
            String confirmPass = edtConfirm.getText().toString().trim();
            String passwordFromPrefs = sharedPreferences.getString("matkhau", "");

            if (!oldPass.equals(passwordFromPrefs)) {
                Toast.makeText(this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("matkhau", newPass);
            editor.apply();

            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
