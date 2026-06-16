package com.example.baitaplon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * ConvertActivity - Màn hình chính của ứng dụng để chuyển đổi tiền tệ.
 * Hỗ trợ nhập số tiền qua bàn phím số giả lập, chọn đơn vị tiền tệ nguồn/đích, hoán đổi tiền tệ,
 * cập nhật tỉ giá và chuyển hướng qua các trang khác bằng Bottom Navigation.
 */
public class ConvertActivity extends AppCompatActivity implements View.OnClickListener {

    // Khai báo các view hiển thị số tiền và tỉ giá
    private TextView tvmoney1, tvmoney2, tvTigia;
    private boolean isTopSelected = true; // Theo dõi xem ô tiền ở trên (nguồn) hay dưới (đích) đang được chọn
    private String numberTop = ""; // Số tiền ở ô trên dạng chuỗi
    private String numberBottom = ""; // Số tiền ở ô dưới dạng chuỗi
    private ImageView imgvietnam, imgusa, imgResetPrice; // Ảnh cờ và nút cập nhật tỉ giá
    private ImageButton btnchange; // Nút hoán đổi 2 loại tiền tệ
    private String giaText = ""; // Văn bản hiển thị tỉ giá quy đổi

    // Mã yêu cầu khi mở màn hình chọn đơn vị tiền tệ
    private static final int REQUEST_CODE_SELECT_TOP = 1001;
    private static final int REQUEST_CODE_SELECT_BOTTOM = 1002;

    // Tên quốc gia mặc định lúc khởi tạo
    private String tenQuocGiaTop = "United States";
    private String tenQuocGiaBottom = "Vietnam";

    // Các biến dùng cho phép tính cơ bản cộng, trừ, nhân, chia trên bàn phím
    private String currentOperator = "";
    private double operandTop = 0;
    private boolean isOperatorPressed = false;

    // Bản đồ lưu trữ tỉ giá giả lập so với đồng USD (offline)
    private static final Map<String, Double> mockRatesToUsd = new HashMap<>();
    static {
        mockRatesToUsd.put("VND", 25400.0);
        mockRatesToUsd.put("USD", 1.0);
        mockRatesToUsd.put("CNY", 7.25);
        mockRatesToUsd.put("CUP", 24.0);
        mockRatesToUsd.put("INR", 83.5);
        mockRatesToUsd.put("IDR", 16300.0);
        mockRatesToUsd.put("JPY", 157.0);
        mockRatesToUsd.put("KRW", 1380.0);
        mockRatesToUsd.put("LAK", 21000.0);
        mockRatesToUsd.put("MYR", 4.7);
        mockRatesToUsd.put("MMK", 2100.0);
        mockRatesToUsd.put("PHP", 58.5);
        mockRatesToUsd.put("SGD", 1.35);
        mockRatesToUsd.put("TWD", 32.4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_convert);
        
        // Căn lề tự động theo thanh hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Ánh xạ các view từ XML layout
        tvmoney1 = findViewById(R.id.tvmoney1);
        tvmoney2 = findViewById(R.id.tvmoney2);
        btnchange = findViewById(R.id.btnchange);
        imgvietnam = findViewById(R.id.imgvietnam);
        imgusa = findViewById(R.id.imgusa);
        tvTigia = findViewById(R.id.tvapi);
        imgResetPrice = findViewById(R.id.imgrefresh);

        // Tải tỉ giá ban đầu giữa 2 quốc gia mặc định
        loadPrice(tenQuocGiaTop, tenQuocGiaBottom);

        // Sự kiện click nút tải lại tỉ giá -> Hiển thị Toast thông báo bằng tiếng Việt
        imgResetPrice.setOnClickListener(v -> {
            loadPrice(tenQuocGiaTop, tenQuocGiaBottom);
            Toast.makeText(getApplicationContext(), "Cập nhật tỉ giá thành công (dữ liệu ngoại tuyến)!", Toast.LENGTH_SHORT).show();
        });

        // Thiết lập thanh điều hướng Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_convert);

        // Sự kiện khi click chọn các tab trên thanh điều hướng dưới màn hình
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_convert) {
                return true; // Đang ở màn hình Chuyển đổi nên không cần chuyển nữa
            } else if (itemId == R.id.bottom_market) {
                // Sang màn hình Thị trường
                Intent intent = new Intent(ConvertActivity.this, ThiTruongActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.bottom_news) {
                // Sang màn hình Tin tức
                Intent intent = new Intent(ConvertActivity.this, NewsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.bottom_profile) {
                // Sang màn hình Cài đặt
                Intent intent = new Intent(ConvertActivity.this, CaiDatActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Sự kiện click hoán đổi vị trí của hai loại tiền tệ
        btnchange.setOnClickListener(v -> {
            Drawable tempImage = imgvietnam.getDrawable();
            imgvietnam.setImageDrawable(imgusa.getDrawable());
            imgusa.setImageDrawable(tempImage);

            tvmoney1.setText("0");
            tvmoney2.setText("0");

            String tempCountry = tenQuocGiaTop;
            tenQuocGiaTop = tenQuocGiaBottom;
            tenQuocGiaBottom = tempCountry;

            numberTop = "";
            numberBottom = "";
            loadPrice(tenQuocGiaTop, tenQuocGiaBottom);
        });

        // Sự kiện click chọn tiền tệ phía trên
        imgvietnam.setOnClickListener(v -> {
            Intent intent = new Intent(ConvertActivity.this, ChonDonViActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_TOP);
        });

        // Sự kiện click chọn tiền tệ phía dưới
        imgusa.setOnClickListener(v -> {
            Intent intent = new Intent(ConvertActivity.this, ChonDonViActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_BOTTOM);
        });

        // Sự kiện click vào dòng chữ tỉ giá để mở màn hình Biểu đồ (BieuDoActivity)
        tvTigia.setOnClickListener(v -> {
            String fromCurrency = getCurrencyCodeByCountry(tenQuocGiaTop);
            String toCurrency = getCurrencyCodeByCountry(tenQuocGiaBottom);
            Intent intent = new Intent(ConvertActivity.this, BieuDoActivity.class);
            intent.putExtra("DulieuBieuDo", fromCurrency + "/" + toCurrency);
            intent.putExtra("BienDongGia", giaText);
            startActivity(intent);
        });

        // Chọn ô nhập liệu phía trên làm ô chủ động nhập số
        tvmoney1.setOnClickListener(v -> {
            isTopSelected = true;
            tvmoney1.setBackgroundResource(R.drawable.selected_border);
            tvmoney2.setBackgroundResource(0);
        });

        // Chọn ô nhập liệu phía dưới làm ô chủ động nhập số
        tvmoney2.setOnClickListener(v -> {
            isTopSelected = false;
            tvmoney2.setBackgroundResource(R.drawable.selected_border);
            tvmoney1.setBackgroundResource(0);
        });

        // Mặc định ban đầu chọn ô phía trên làm ô nhập liệu
        tvmoney1.setBackgroundResource(R.drawable.selected_border);

        // Đăng ký sự kiện click cho toàn bộ phím số và phím chức năng của bàn phím giả lập
        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnadd, R.id.btnsub, R.id.btnmul, R.id.btndiv,
                R.id.btndot, R.id.btnc, R.id.btne, R.id.btnequal
        };
        for (int id : buttonIds) {
            View btn = findViewById(id);
            if (btn != null) btn.setOnClickListener(this);
        }
    }

    /**
     * Lấy mã tiền tệ (VND, USD, CNY...) dựa theo tên quốc gia tương ứng.
     */
    private String getCurrencyCodeByCountry(String tenQuocGia) {
        for (ThongTinCacQuocGia item : ChonDonViActivity.ThongTinCacQuocGiaList) {
            if (item.getTenQuocGia().equals(tenQuocGia)) {
                return item.getMaTienTe();
            }
        }
        return "USD";
    }

    /**
     * Nhận kết quả trả về từ màn hình chọn quốc gia (ChonDonViActivity)
     * để cập nhật ảnh cờ và tỉ giá tương ứng.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            int imgResId = data.getIntExtra("imgResId", -1);
            String maTienTe = data.getStringExtra("maTienTe");
            String tenQuocGia = data.getStringExtra("tenQuocGia");

            if (imgResId != -1 && maTienTe != null) {
                if (requestCode == REQUEST_CODE_SELECT_TOP) {
                    imgvietnam.setImageResource(imgResId);
                    tenQuocGiaTop = tenQuocGia;
                } else if (requestCode == REQUEST_CODE_SELECT_BOTTOM) {
                    imgusa.setImageResource(imgResId);
                    tenQuocGiaBottom = tenQuocGia;
                }
                loadPrice(tenQuocGiaTop, tenQuocGiaBottom);
            }
        }
    }

    /**
     * Tính toán tỉ giá quy đổi giữa hai đồng tiền của hai quốc gia và hiển thị lên giao diện.
     */
    public void loadPrice(String tenQuocGiaFrom, String tenQuocGiaTo) {
        String from = getCurrencyCodeByCountry(tenQuocGiaFrom);
        String to = getCurrencyCodeByCountry(tenQuocGiaTo);

        Double rateFrom = mockRatesToUsd.get(from);
        Double rateTo = mockRatesToUsd.get(to);

        if (rateFrom == null) rateFrom = 1.0;
        if (rateTo == null) rateTo = 1.0;

        // Tính toán tỉ giá: rate = rateTo / rateFrom (ví dụ USD -> VND = 25400 / 1 = 25400)
        double rate = rateTo / rateFrom;
        giaText = String.format("1 %s = %s %s", from, formatResult(rate), to);
        tvTigia.setText(giaText);
        convertCurrency(isTopSelected);
    }

    /**
     * Chuyển đổi số tiền từ ô nhập chủ động sang ô kết quả dựa trên tỉ giá hiện tại.
     */
    private void convertCurrency(boolean fromTop) {
        String rateText = tvTigia.getText().toString();
        if (rateText.isEmpty() || !rateText.contains("=")) {
            return;
        }

        String[] parts = rateText.split("=");
        if (parts.length == 2) {
            String rateStr = parts[1].replaceAll("[^\\d.]", "").trim();
            try {
                double rate = Double.parseDouble(rateStr);
                if (fromTop) {
                    double amount = parseNumber(numberTop);
                    double result = amount * rate;
                    numberBottom = formatResult(result);
                    tvmoney2.setText(numberBottom);
                } else {
                    double amount = parseNumber(numberBottom);
                    double result = amount / rate;
                    numberTop = formatResult(result);
                    tvmoney1.setText(numberTop);
                }
            } catch (NumberFormatException e) {
                // Bỏ qua nếu lỗi định dạng số
            }
        }
    }

    /**
     * Chuyển đổi chuỗi số (có thể chứa dấu phẩy) thành kiểu double để tính toán.
     */
    private double parseNumber(String number) {
        if (number == null || number.isEmpty()) return 0;
        try {
            return Double.parseDouble(number.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Định dạng kết quả số thực thành dạng chuỗi hiển thị tối đa 3 chữ số thập phân.
     */
    public String formatResult(double num) {
        DecimalFormat formatter = new DecimalFormat("###.###");
        return formatter.format(num);
    }

    /**
     * Nhận sự kiện click từ bàn phím số giả lập.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (isTopSelected) {
            handleKeyInput(id, true);
        } else {
            handleKeyInput(id, false);
        }
    }

    /**
     * Xử lý ký tự nhập vào từ bàn phím giả lập cho ô trên hoặc ô dưới.
     */
    private void handleKeyInput(int id, boolean isTop) {
        String numStr = isTop ? numberTop : numberBottom;

        if (id == R.id.btnc) {
            // Nút xóa toàn bộ (C)
            numberTop = "";
            numberBottom = "";
            tvmoney1.setText("0");
            tvmoney2.setText("0");
            currentOperator = "";
            isOperatorPressed = false;
        } else if (id == R.id.btne) {
            // Nút xóa ký tự cuối cùng (E)
            if (!numStr.isEmpty()) {
                numStr = numStr.substring(0, numStr.length() - 1);
                if (isTop) {
                    numberTop = numStr;
                    tvmoney1.setText(numberTop.isEmpty() ? "0" : numberTop);
                } else {
                    numberBottom = numStr;
                    tvmoney2.setText(numberBottom.isEmpty() ? "0" : numberBottom);
                }
                convertCurrency(isTop);
            }
        } else if (id == R.id.btndot) {
            // Nút dấu phẩy thập phân (,)
            if (!numStr.contains(",")) {
                numStr += numStr.isEmpty() ? "0," : ",";
                if (isTop) {
                    numberTop = numStr;
                    tvmoney1.setText(numberTop);
                } else {
                    numberBottom = numStr;
                    tvmoney2.setText(numberBottom);
                }
            }
        } else if (id == R.id.btnadd || id == R.id.btnsub || id == R.id.btnmul || id == R.id.btndiv) {
            // Phím phép tính: cộng, trừ, nhân, chia
            if (!numStr.isEmpty() && !isOperatorPressed) {
                operandTop = parseNumber(numStr);
                isOperatorPressed = true;

                if (id == R.id.btnadd) currentOperator = "+";
                else if (id == R.id.btnsub) currentOperator = "-";
                else if (id == R.id.btnmul) currentOperator = "*";
                else if (id == R.id.btndiv) currentOperator = "/";

                numStr += " " + currentOperator + " ";
                if (isTop) {
                    numberTop = numStr;
                    tvmoney1.setText(numberTop);
                } else {
                    numberBottom = numStr;
                    tvmoney2.setText(numberBottom);
                }
            }
        } else if (id == R.id.btnequal) {
            // Nút dấu bằng (=) tính toán kết quả phép tính
            if (isOperatorPressed) {
                String[] parts = numStr.split("[" + "\\+\\-\\*/" + "]");
                if (parts.length == 2) {
                    double secondOperand = parseNumber(parts[1].trim());
                    double result = 0;
                    switch (currentOperator) {
                        case "+": result = operandTop + secondOperand; break;
                        case "-": result = operandTop - secondOperand; break;
                        case "*": result = operandTop * secondOperand; break;
                        case "/": result = secondOperand != 0 ? operandTop / secondOperand : 0; break;
                    }
                    numStr = formatResult(result);
                    if (isTop) {
                        numberTop = numStr;
                        tvmoney1.setText(numberTop);
                    } else {
                        numberBottom = numStr;
                        tvmoney2.setText(numberBottom);
                    }
                    isOperatorPressed = false;
                    currentOperator = "";
                    convertCurrency(isTop);
                }
            } else {
                convertCurrency(isTop);
            }
        } else if (id >= R.id.btn0 && id <= R.id.btn9) {
            // Phím số từ 0 đến 9
            String value = String.valueOf(id - R.id.btn0);
            numStr += value;
            if (isTop) {
                numberTop = numStr;
                tvmoney1.setText(numberTop);
            } else {
                numberBottom = numStr;
                tvmoney2.setText(numberBottom);
            }
            if (!isOperatorPressed) convertCurrency(isTop);
        }
    }
}
