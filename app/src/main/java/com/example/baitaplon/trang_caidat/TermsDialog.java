package com.example.baitaplon.trang_caidat;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.baitaplon.R;

/**
 * Hộp thoại hiển thị Điều khoản dịch vụ và chính sách bảo mật.
 */
public class TermsDialog {

    public static void show(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_terms_conditions, null);
        builder.setView(dialogView);

        TextView tvTerms = dialogView.findViewById(R.id.tvTermsDialog);
        Button btnClose = dialogView.findViewById(R.id.btnCloseDialog);

        tvTerms.setText(getTermsText());

        AlertDialog dialog = builder.create();
        dialog.show();

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    private static String getTermsText() {
        return "📄 ĐIỀU KHOẢN VÀ ĐIỀU KIỆN SỬ DỤNG\n\n"
                + "1. Giới thiệu\n"
                + "- Bằng việc sử dụng ứng dụng [Chuyển Đổi Tiền Tệ], bạn đồng ý với các điều khoản này.\n\n"
                + "2. Tài khoản người dùng\n"
                + "- Cần đăng nhập để sử dụng các chức năng nâng cao như xem/chỉnh sửa hồ sơ cá nhân và đổi mật khẩu.\n"
                + "- Người dùng chịu trách nhiệm tự bảo mật thông tin tài khoản đăng nhập của mình.\n\n"
                + "3. Nguồn dữ liệu & API\n"
                + "- Ứng dụng cung cấp tỷ giá tiền tệ chỉ nhằm mục đích tham khảo.\n"
                + "- Chúng tôi không chịu trách nhiệm trong trường hợp dữ liệu bị sai lệch hoặc chậm trễ do lỗi hệ thống.\n\n"
                + "4. Tính chính xác\n"
                + "- Tỷ giá hiển thị chỉ mang tính chất tham khảo.\n"
                + "- Khuyến cáo không sử dụng các tỷ giá giả lập này cho các giao dịch tài chính thực tế.\n\n"
                + "5. Quyền sở hữu trí tuệ\n"
                + "- Mọi tài nguyên, hình ảnh và mã nguồn của ứng dụng thuộc về nhà phát triển.\n"
                + "- Nghiêm cấm sao chép hoặc tái sử dụng bất hợp pháp.\n\n"
                + "6. Bảo mật thông tin\n"
                + "- Chúng tôi cam kết bảo vệ thông tin cá nhân của bạn lưu trên thiết bị.\n"
                + "- Không thu thập hay chia sẻ thông tin cho bất kỳ bên thứ ba nào.\n\n"
                + "7. Giới hạn trách nhiệm\n"
                + "- Người dùng chịu hoàn toàn trách nhiệm đối với mọi hành vi và quyết định sử dụng dữ liệu của mình.\n"
                + "- Chúng tôi không chịu trách nhiệm với bất kỳ thiệt hại trực tiếp hay gián tiếp nào phát sinh.\n\n"
                + "8. Thay đổi điều khoản\n"
                + "- Điều khoản có thể được cập nhật bất kỳ lúc nào để phù hợp hơn với ứng dụng.\n"
                + "- Việc tiếp tục sử dụng ứng dụng đồng nghĩa bạn đồng ý với các điều khoản mới.\n\n"
                + "9. Liên hệ hỗ trợ\n"
                + "Email hỗ trợ: support@example.com\n"
                + "Số điện thoại: 0123 456 789\n\n"
                + "✅ Bằng việc sử dụng ứng dụng, bạn xác nhận đã đọc và đồng ý hoàn toàn với các điều khoản này.";
    }
}
