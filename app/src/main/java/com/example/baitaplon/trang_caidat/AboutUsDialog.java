package com.example.baitaplon.trang_caidat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.baitaplon.R;

/**
 * Hộp thoại giới thiệu thông tin nhóm phát triển và ứng dụng.
 */
public class AboutUsDialog {
    
    public static void show(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_about_us, null);
        builder.setView(dialogView);

        TextView tvAbousUs = dialogView.findViewById(R.id.tvAbouUs);
        Button btnClose = dialogView.findViewById(R.id.btnCloseDialog);

        tvAbousUs.setText(getAboutUsText());

        AlertDialog dialog = builder.create();
        dialog.show();

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    private static String getAboutUsText() {
        return "1. Về ứng dụng\n\n" +
                "- Ứng dụng [Chuyển Đổi Tiền Tệ] là một nền tảng chuyển đổi tiền tệ thông minh, giúp người dùng dễ dàng theo dõi và chuyển đổi tỷ giá ngoại tệ trên toàn cầu. Ứng dụng mang đến giao diện tối giản, trực quan, hỗ trợ tính năng làm việc ngoại tuyến với các tỷ giá đã lưu.\n\n" +
                "- Giao diện được thiết kế hiện đại, hài hòa, phù hợp cho mọi người dùng có nhu cầu chuyển tiền, đi du lịch hoặc tìm hiểu tin tức tài chính.\n\n" +
                "2. Sứ mệnh và tầm nhìn\n\n" +
                "- Mang đến một công cụ tiện ích nhanh chóng, hữu ích và mượt mà trong việc xử lý chuyển đổi tiền tệ.\n\n" +
                "3. Tính năng chính\n\n" +
                "- Chuyển đổi tiền tệ nhanh với bàn phím tích hợp.\n" +
                "- Biến động giá thị trường ngoại tệ cơ bản.\n" +
                "- Tin tức trong và ngoài nước.\n" +
                "- Hỗ trợ chế độ giao diện Xám / Trắng.\n\n" +
                "4. Thông tin nhóm phát triển\n\n" +
                "Nhà phát triển: Nhóm 3\n" +
                "Email hỗ trợ: support@example.com\n" +
                "Phiên bản ứng dụng: 1.0.0\n\n" +
                "❤️ Cảm ơn bạn đã tin tưởng và sử dụng ứng dụng của chúng tôi!";
    }
}
