package com.example.baitaplon.trang_caidat;

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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baitaplon.R;
import com.example.baitaplon.dulieu.DataBaseHelper_DangKy;
import com.example.baitaplon.trang_chuyendoi.ConvertActivity;
import com.example.baitaplon.trang_dangnhap.MainActivity;
import com.example.baitaplon.trang_hoso.ProfileActivity;
import com.example.baitaplon.trang_thitruong.ThiTruongActivity;
import com.example.baitaplon.trang_tintuc.NewsActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Màn hình Cài đặt: hỗ trợ bật/tắt chế độ sáng tối, đổi mật khẩu, xem giới thiệu/điều khoản và đăng xuất.
 */
public class CaiDatActivity extends AppCompatActivity {

    private TextView Logout, txtUser, tvTerms, tvAboutUs;
    private ImageView imgAccount, imgTerms, imgAboutUs;
    private Button btnGray, btnWhite;
    private ImageView imgLogout, imgThemes, imgLanguages;
    private DataBaseHelper_DangKy db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cai_dat);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

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

        db = new DataBaseHelper_DangKy(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");
        txtUser.setText(username);

        // Đổi mật khẩu tài khoản
        TextView tvChangePassword = findViewById(R.id.tvChangePassword);
        tvChangePassword.setOnClickListener(v -> showChangePasswordDialog());

        // Đăng xuất tài khoản
        Logout.setOnClickListener(v -> performLogout());
        imgLogout.setOnClickListener(v -> performLogout());

        // Bấm chọn tab chuyển đổi màn hình
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_profile) {
                return true; 
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

        // Click tên/ảnh đại diện -> mở thông tin chi tiết hồ sơ
        txtUser.setOnClickListener(v -> openProfile());
        imgAccount.setOnClickListener(v -> openProfile());

        // Nhấn nút chuyển sang chế độ Tối
        btnGray.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("theme_mode", "dark");
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Toast.makeText(CaiDatActivity.this, "Đã chuyển sang chế độ Tối", Toast.LENGTH_SHORT).show();
        });

        // Nhấn nút chuyển sang chế độ Sáng
        btnWhite.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("theme_mode", "light");
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(CaiDatActivity.this, "Đã chuyển sang chế độ Sáng", Toast.LENGTH_SHORT).show();
        });

        // Các tính năng demo đang trong quá trình phát triển
        TextView tvThemes = findViewById(R.id.textView12);
        TextView tvLanguages = findViewById(R.id.textView24);

        tvThemes.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());
        imgThemes.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());

        tvLanguages.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());
        imgLanguages.setOnClickListener(v -> Toast.makeText(CaiDatActivity.this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show());

        // Mở Dialog Điều khoản và Giới thiệu
        tvTerms.setOnClickListener(v -> TermsDialog.show(CaiDatActivity.this));
        imgTerms.setOnClickListener(v -> TermsDialog.show(CaiDatActivity.this));

        tvAboutUs.setOnClickListener(v -> AboutUsDialog.show(CaiDatActivity.this));
        imgAboutUs.setOnClickListener(v -> AboutUsDialog.show(CaiDatActivity.this));
    }

    // Đăng xuất: xóa sạch SharedPreferences và quay lại trang Đăng nhập
    private void performLogout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(CaiDatActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Mở trang Hồ sơ chi tiết
    private void openProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String userId = sharedPreferences.getString("username", "Guest");

        Intent intent = new Intent(CaiDatActivity.this, ProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    // Hộp thoại đổi mật khẩu
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

            String userId = sharedPreferences.getString("uid", "");
            db.updatePassword(userId, newPass);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("matkhau", newPass);
            editor.apply();

            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
