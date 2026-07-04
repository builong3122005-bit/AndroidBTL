package com.example.baitaplon.trang_thitruong;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.R;
import com.example.baitaplon.api.ApiClient_Price;
import com.example.baitaplon.api.ExchangeRateApi;
import com.example.baitaplon.dulieu.ExchangeRateCacheHelper;
import com.example.baitaplon.model.ExchangeRateItem;
import com.example.baitaplon.model.ExchangeRateResponse;
import com.example.baitaplon.trang_chuyendoi.ConvertActivity;
import com.example.baitaplon.trang_caidat.CaiDatActivity;
import com.example.baitaplon.trang_tintuc.NewsActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Màn hình thị trường: Hiển thị bảng tỷ giá các đồng tiền so với đồng USD tiêu chuẩn.
 */
public class ThiTruongActivity extends AppCompatActivity {
    private TextView txtThoiGian, txtDate;
    private ImageView imgBack;
    private RecyclerView recyclerViewRates;
    private EditText edtTimKiem;

    private ExchangeRateAdapter adapter;
    private List<ExchangeRateItem> rateItems;
    private ExchangeRateCacheHelper cacheHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thi_truong);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        txtThoiGian = findViewById(R.id.txtThoiGian);
        txtDate = findViewById(R.id.txtDate);
        imgBack = findViewById(R.id.imgBack);
        recyclerViewRates = findViewById(R.id.recyclerViewRates);
        edtTimKiem = findViewById(R.id.edtTimKiem);

        cacheHelper = new ExchangeRateCacheHelper(this);

        if (imgBack != null) {
            imgBack.setOnClickListener(v -> finish());
        }

        // Lấy ngày giờ hệ thống hiện tại để hiển thị
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = timeFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        txtThoiGian.setText(time);
        txtDate.setText(date);

        initDefaultRates();

        recyclerViewRates.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExchangeRateAdapter(rateItems);
        recyclerViewRates.setAdapter(adapter);

        // Lắng nghe thay đổi tìm kiếm để lọc nhanh dòng tỷ giá
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fetchRatesFromApi();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.bottom_market);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_convert) {
                    Intent intent = new Intent(ThiTruongActivity.this, ConvertActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.bottom_market) {
                    return true;
                } else if (itemId == R.id.bottom_news) {
                    Intent intent = new Intent(ThiTruongActivity.this, NewsActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.bottom_profile) {
                    Intent intent = new Intent(ThiTruongActivity.this, CaiDatActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            });
        }
    }

    // Giá trị tỷ giá mặc định ban đầu so với USD
    private void initDefaultRates() {
        rateItems = new ArrayList<>();
        rateItems.add(new ExchangeRateItem("VND", "Vietnam", R.drawable.icon_vietnam, 25400.0));
        rateItems.add(new ExchangeRateItem("USD", "United States", R.drawable.icon_usa, 1.0));
        rateItems.add(new ExchangeRateItem("CNY", "China", R.drawable.ic_china, 7.25));
        rateItems.add(new ExchangeRateItem("CUP", "Cuba", R.drawable.ic_cuba, 24.0));
        rateItems.add(new ExchangeRateItem("INR", "India", R.drawable.ic_india, 83.5));
        rateItems.add(new ExchangeRateItem("IDR", "Indonesia", R.drawable.ic_indonesia, 16300.0));
        rateItems.add(new ExchangeRateItem("JPY", "Japan", R.drawable.ic_japam, 157.0));
        rateItems.add(new ExchangeRateItem("KRW", "South Korea", R.drawable.ic_korea, 1380.0));
        rateItems.add(new ExchangeRateItem("LAK", "Laos", R.drawable.ic_laos, 21000.0));
        rateItems.add(new ExchangeRateItem("MYR", "Malaysia", R.drawable.ic_malaysia, 4.7));
        rateItems.add(new ExchangeRateItem("MMK", "Myanmar", R.drawable.ic_myanmar, 2100.0));
        rateItems.add(new ExchangeRateItem("PHP", "Philippines", R.drawable.ic_philippins, 58.5));
        rateItems.add(new ExchangeRateItem("SGD", "Singapore", R.drawable.ic_singapor, 1.35));
        rateItems.add(new ExchangeRateItem("TWD", "Taiwan", R.drawable.ic_taiwan, 32.4));
    }

    // Lấy tỷ giá từ Internet qua API
    private void fetchRatesFromApi() {
        ExchangeRateApi api = ApiClient_Price.getClient().create(ExchangeRateApi.class);
        Call<ExchangeRateResponse> call = api.getExchangeRates("USD");
        call.enqueue(new Callback<ExchangeRateResponse>() {
            @Override
            public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Double> ratesMap = response.body().getConversionRates();
                    if (ratesMap != null) {
                        List<ExchangeRateItem> updatedList = new ArrayList<>();
                        for (ExchangeRateItem item : rateItems) {
                            Double newRate = ratesMap.get(item.getCurrencyCode());
                            if (newRate != null) {
                                item.setRateAgainstUsd(newRate);
                                cacheHelper.saveRate("USD", item.getCurrencyCode(), newRate);
                            }
                            updatedList.add(item);
                        }
                        adapter.updateData(updatedList);
                        Toast.makeText(ThiTruongActivity.this, "Cập nhật tỉ giá thị trường thành công!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                fallbackToOffline();
            }

            @Override
            public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                fallbackToOffline();
            }
        });
    }

    // Fallback khi lỗi mạng: đọc cache SQLite
    private void fallbackToOffline() {
        List<ExchangeRateItem> offlineList = new ArrayList<>();
        for (ExchangeRateItem item : rateItems) {
            Double cachedRate = cacheHelper.getRate("USD", item.getCurrencyCode());
            if (cachedRate != null) {
                item.setRateAgainstUsd(cachedRate);
            }
            offlineList.add(item);
        }
        adapter.updateData(offlineList);
        Toast.makeText(this, "Không thể kết nối mạng. Sử dụng tỉ giá ngoại tuyến/mặc định.", Toast.LENGTH_SHORT).show();
    }
}
