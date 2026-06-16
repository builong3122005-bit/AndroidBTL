package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity - Màn hình chào mừng (Splash Screen).
 * Xuất hiện đầu tiên khi mở ứng dụng trong vòng 2 giây trước khi tự động chuyển hướng đến màn hình đăng nhập.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Sử dụng Handler để trì hoãn việc chuyển màn hình sau 2 giây (2000 mili giây)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Điều hướng từ SplashActivity sang MainActivity (Đăng nhập)
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Đóng màn hình chào để người dùng không quay lại được bằng nút Back
            }
        }, 2000);
    }
}
