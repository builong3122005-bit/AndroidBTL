package com.example.baitaplon.trang_chuyendoi;

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

import com.example.baitaplon.R;
import com.example.baitaplon.api.ApiClient_Price;
import com.example.baitaplon.api.ExchangeRateApi;
import com.example.baitaplon.dulieu.ExchangeRateCacheHelper;
import com.example.baitaplon.model.ExchangeRateResponse;
import com.example.baitaplon.model.ThongTinCacQuocGia;
import com.example.baitaplon.trang_bieudo.BieuDoActivity;
import com.example.baitaplon.trang_caidat.CaiDatActivity;
import com.example.baitaplon.trang_thitruong.ThiTruongActivity;
import com.example.baitaplon.trang_tintuc.NewsActivity;
import com.example.baitaplon.tienich.CsvUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Màn hình Chuyển đổi chính (Trang chủ quy đổi tiền tệ).
 * Hỗ trợ nhập liệu qua bàn phím giả lập, tính toán cơ bản và tự động cập nhật tỷ giá.
 */
public class ConvertActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvmoney1, tvmoney2, tvTigia;
    private boolean isTopSelected = true; // Lưu ô nhập hiện tại là ô trên hay ô dưới
    private String numberTop = ""; 
    private String numberBottom = ""; 
    private ImageView imgvietnam, imgusa, imgResetPrice; 
    private ImageButton btnchange; 
    private String giaText = "";   // Chuỗi hiển thị tỷ giá (ví dụ: 1 USD = 25400 VND)
    private ExchangeRateCacheHelper cacheHelper; 

    private static final int REQUEST_CODE_SELECT_TOP = 1001;
    private static final int REQUEST_CODE_SELECT_BOTTOM = 1002;

    private String tenQuocGiaTop = "United States";
    private String tenQuocGiaBottom = "Vietnam";

    // Phục vụ tính toán cơ bản trên bàn phím giả lập
    private String currentOperator = "";
    private double operandTop = 0;
    private boolean isOperatorPressed = false;

    // Tỷ giá mặc định trong trường hợp mất mạng và không có cache
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
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        tvmoney1 = findViewById(R.id.tvmoney1);
        tvmoney2 = findViewById(R.id.tvmoney2);
        btnchange = findViewById(R.id.btnchange);
        imgvietnam = findViewById(R.id.imgvietnam);
        imgusa = findViewById(R.id.imgusa);
        tvTigia = findViewById(R.id.tvapi);
        imgResetPrice = findViewById(R.id.imgrefresh);
        
        cacheHelper = new ExchangeRateCacheHelper(this);

        loadPrice(tenQuocGiaTop, tenQuocGiaBottom);

        // Click làm mới tỷ giá và đồng bộ file CSV
        imgResetPrice.setOnClickListener(v -> {
            loadPrice(tenQuocGiaTop, tenQuocGiaBottom);
            Toast.makeText(getApplicationContext(), "Đang cập nhật dữ liệu từ API...", Toast.LENGTH_SHORT).show();
            
            String fromCurrency = getCurrencyCodeByCountry(tenQuocGiaTop);
            String toCurrency = getCurrencyCodeByCountry(tenQuocGiaBottom);
            updateDataFromApi(fromCurrency, toCurrency);
            CsvUtils.sortCsvFile(this, fromCurrency + "/" + toCurrency);
        });

        // Thiết lập sự kiện chọn Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_convert);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_convert) {
                return true; 
            } else if (itemId == R.id.bottom_market) {
                Intent intent = new Intent(ConvertActivity.this, ThiTruongActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.bottom_news) {
                Intent intent = new Intent(ConvertActivity.this, NewsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.bottom_profile) {
                Intent intent = new Intent(ConvertActivity.this, CaiDatActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Hoán đổi vị trí cờ và các giá trị tiền tương ứng
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

        // Chọn quốc gia nguồn (ô trên)
        imgvietnam.setOnClickListener(v -> {
            Intent intent = new Intent(ConvertActivity.this, ChonDonViActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_TOP);
        });

        // Chọn quốc gia đích (ô dưới)
        imgusa.setOnClickListener(v -> {
            Intent intent = new Intent(ConvertActivity.this, ChonDonViActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_BOTTOM);
        });

        // Mở màn hình biểu đồ nến lịch sử tỷ giá
        tvTigia.setOnClickListener(v -> {
            String fromCurrency = getCurrencyCodeByCountry(tenQuocGiaTop);
            String toCurrency = getCurrencyCodeByCountry(tenQuocGiaBottom);
            Intent intent = new Intent(ConvertActivity.this, BieuDoActivity.class);
            intent.putExtra("DulieuBieuDo", fromCurrency + "/" + toCurrency);
            intent.putExtra("BienDongGia", giaText);
            startActivity(intent);
        });

        // Chọn ô nhập số tiền phía trên
        tvmoney1.setOnClickListener(v -> {
            isTopSelected = true;
            tvmoney1.setBackgroundResource(R.drawable.selected_border);
            tvmoney2.setBackgroundResource(0);
        });

        // Chọn ô nhập số tiền phía dưới
        tvmoney2.setOnClickListener(v -> {
            isTopSelected = false;
            tvmoney2.setBackgroundResource(R.drawable.selected_border);
            tvmoney1.setBackgroundResource(0);
        });

        tvmoney1.setBackgroundResource(R.drawable.selected_border);

        // Đăng ký click cho các nút của bàn phím số
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

    private String getCurrencyCodeByCountry(String tenQuocGia) {
        for (ThongTinCacQuocGia item : ChonDonViActivity.ThongTinCacQuocGiaList) {
            if (item.getTenQuocGia().equals(tenQuocGia)) {
                return item.getMaTienTe();
            }
        }
        return "USD";
    }

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

    // Tải tỷ giá bằng Retrofit (chạy bất đồng bộ)
    public void loadPrice(String tenQuocGiaFrom, String tenQuocGiaTo) {
        String from = getCurrencyCodeByCountry(tenQuocGiaFrom);
        String to = getCurrencyCodeByCountry(tenQuocGiaTo);

        ExchangeRateApi api = ApiClient_Price.getClient().create(ExchangeRateApi.class);
        Call<ExchangeRateResponse> call = api.getExchangeRates("USD");
        call.enqueue(new Callback<ExchangeRateResponse>() {
            @Override
            public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ExchangeRateResponse rates = response.body();
                    Map<String, Double> ratesMap = rates.getConversionRates();
                    if (ratesMap != null) {
                        // Lưu đệm tất cả tỷ giá từ internet vào SQLite cục bộ
                        for (Map.Entry<String, Double> entry : ratesMap.entrySet()) {
                            cacheHelper.saveRate("USD", entry.getKey(), entry.getValue());
                        }

                        Double usdToFrom = ratesMap.get(from);
                        Double usdToTo = ratesMap.get(to);
                        if (usdToFrom != null && usdToTo != null) {
                            double rate = usdToTo / usdToFrom;
                            giaText = String.format("1 %s = %s %s", from, formatResult(rate), to);
                            tvTigia.setText(giaText);
                            convertCurrency(isTopSelected);
                            return;
                        }
                    }
                }
                fallbackToCacheOrMock(from, to);
            }

            @Override
            public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                fallbackToCacheOrMock(from, to);
            }
        });
    }

    // Dự phòng ngoại tuyến: Đọc tỷ giá từ SQLite, hoặc fallback về dữ liệu fix cứng mặc định
    private void fallbackToCacheOrMock(String from, String to) {
        Double usdToFrom = cacheHelper.getRate("USD", from);
        Double usdToTo = cacheHelper.getRate("USD", to);

        if (usdToFrom == null || usdToTo == null) {
            usdToFrom = mockRatesToUsd.get(from);
            usdToTo = mockRatesToUsd.get(to);
        }

        if (usdToFrom == null) usdToFrom = 1.0;
        if (usdToTo == null) usdToTo = 1.0;

        double rate = usdToTo / usdToFrom;
        giaText = String.format("1 %s = %s %s", from, formatResult(rate), to);
        tvTigia.setText(giaText);
        convertCurrency(isTopSelected);
    }

    // Tính toán quy đổi chéo tiền tệ theo tỷ giá đang hiển thị
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
                // Xử lý lỗi chuyển số
            }
        }
    }

    private double parseNumber(String number) {
        if (number == null || number.isEmpty()) return 0;
        try {
            return Double.parseDouble(number.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Định dạng số hiển thị cho tỷ giá và kết quả (nới rộng chữ số thập phân cho tỷ giá rất nhỏ)
    public String formatResult(double num) {
        if (num == 0) return "0";
        double absNum = Math.abs(num);
        if (absNum < 0.00001) {
            DecimalFormat formatter = new DecimalFormat("0.########");
            return formatter.format(num);
        } else if (absNum < 0.001) {
            DecimalFormat formatter = new DecimalFormat("0.######");
            return formatter.format(num);
        } else if (absNum < 0.1) {
            DecimalFormat formatter = new DecimalFormat("0.####");
            return formatter.format(num);
        } else {
            DecimalFormat formatter = new DecimalFormat("###.###");
            return formatter.format(num);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (isTopSelected) {
            handleKeyInput(id, true);
        } else {
            handleKeyInput(id, false);
        }
    }

    // Xử lý logic nhập liệu phím số, toán tử, xóa và tính dấu bằng
    private void handleKeyInput(int id, boolean isTop) {
        String numStr = isTop ? numberTop : numberBottom;

        if (id == R.id.btnc) {
            numberTop = "";
            numberBottom = "";
            tvmoney1.setText("0");
            tvmoney2.setText("0");
            currentOperator = "";
            isOperatorPressed = false;
        } else if (id == R.id.btne) {
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

    // Tải nến lịch sử từ API AlphaVantage (FX_DAILY) để đồng bộ lưu vào file CSV cục bộ
    public void updateDataFromApi(String fromSymbol, String toSymbol) {
        String apiUrl = "https://www.alphavantage.co/query?function=FX_DAILY&from_symbol="
                + fromSymbol + "&to_symbol=" + toSymbol
                + "&outputsize=full&apikey=B15AUJQ9234KXN1O";

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();

                JSONObject obj = new JSONObject(result.toString());
                JSONObject timeSeries = obj.getJSONObject("Time Series FX (Daily)");

                String slug = fromSymbol + "/" + toSymbol;
                String currency = toSymbol;
                Set<String> existingDates = CsvUtils.getExistingDates(this, slug);

                int addedCount = 0;
                List<String> dateList = new ArrayList<>();
                Iterator<String> it = timeSeries.keys();
                while (it.hasNext()) {
                    dateList.add(it.next());
                }

                Collections.sort(dateList);

                for (String date : dateList) {
                    if (!existingDates.contains(date)) {
                        JSONObject dayData = timeSeries.getJSONObject(date);
                        String open = dayData.getString("1. open");
                        String high = dayData.getString("2. high");
                        String low = dayData.getString("3. low");
                        String close = dayData.getString("4. close");

                        String lineCsv = slug + "," + date + "," + open + "," + high + "," + low + "," + close + "," + currency;
                        CsvUtils.appendLine(this, lineCsv);
                        addedCount++;
                    }
                }

                int finalAddedCount = addedCount;
                runOnUiThread(() -> Toast.makeText(this, "Đã thêm " + finalAddedCount + " bản ghi mới vào biểu đồ.", Toast.LENGTH_SHORT).show());

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Không thể kết nối API AlphaVantage, sử dụng dữ liệu ngoại tuyến.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
