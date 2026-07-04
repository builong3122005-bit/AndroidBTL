package com.example.baitaplon.trang_chao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitaplon.R;
import com.example.baitaplon.trang_dangnhap.MainActivity;

/**
 * Màn hình chào mừng hiển thị logo trong 2 giây rồi tự động chuyển hướng đến màn hình đăng nhập.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Đợi 2 giây rồi mở màn hình đăng nhập chính
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                
                // Kết thúc màn hình chào để người dùng bấm nút back không quay lại đây nữa
                finish();
            }
        }, 2000);
    }
}
