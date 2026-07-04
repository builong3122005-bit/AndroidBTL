package com.example.baitaplon.trang_chuyendoi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.R;
import com.example.baitaplon.model.ThongTinCacQuocGia;

import java.util.ArrayList;
import java.util.List;

/**
 * Màn hình chọn đơn vị tiền tệ quy đổi. Hỗ trợ tìm kiếm theo tên hoặc ký hiệu tiền tệ.
 */
public class ChonDonViActivity extends AppCompatActivity {
    private ImageView imgBack;
    private RecyclerView recyclerView;
    private ThongTinCacQuocGiaADapter ChonDonViadapter;
    
    // Seed dữ liệu cứng danh sách quốc gia, mã tiền và hình cờ
    public static final List<ThongTinCacQuocGia> ThongTinCacQuocGiaList = new ArrayList<>();

    static {
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Vietnam", "VND", R.drawable.icon_vietnam));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("United States", "USD", R.drawable.icon_usa));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("China", "CNY", R.drawable.ic_china));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Cuba", "CUP", R.drawable.ic_cuba));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("India", "INR", R.drawable.ic_india));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Indonesia", "IDR", R.drawable.ic_indonesia));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Japan", "JPY", R.drawable.ic_japam));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("South Korea", "KRW", R.drawable.ic_korea));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Laos", "LAK", R.drawable.ic_laos));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Malaysia", "MYR", R.drawable.ic_malaysia));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Myanmar", "MMK", R.drawable.ic_myanmar));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Philippines", "PHP", R.drawable.ic_philippins));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Singapore", "SGD", R.drawable.ic_singapor));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("Taiwan", "TWD", R.drawable.ic_taiwan));
        ThongTinCacQuocGiaList.add(new ThongTinCacQuocGia("East Timor", "USD", R.drawable.ic_timo_lest));
    }

    private EditText edtTimKiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chon_don_vi_tien_te);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.ChuyenDoiRecyclerView);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChonDonViadapter = new ThongTinCacQuocGiaADapter(new ArrayList<>(ThongTinCacQuocGiaList), this);
        recyclerView.setAdapter(ChonDonViadapter);

        // Click chọn một dòng -> trả dữ liệu về ConvertActivity
        ChonDonViadapter.setOnItemClickListener(new ThongTinCacQuocGiaADapter.OnItemClickListener() {
            @Override
            public void onItemClick(ThongTinCacQuocGia item) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("imgResId", item.getHinhQuocGia());
                resultIntent.putExtra("maTienTe", item.getMaTienTe());
                resultIntent.putExtra("tenQuocGia", item.getTenQuocGia());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        // Xử lý bộ lọc tìm kiếm thời gian thực
        edtTimKiem = findViewById(R.id.edtTimKiem);
        edtTimKiem.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim().toLowerCase();
                List<ThongTinCacQuocGia> filteredList = new ArrayList<>();
                for (ThongTinCacQuocGia item : ThongTinCacQuocGiaList) {
                    if (item.getTenQuocGia().toLowerCase().contains(query) || item.getMaTienTe().toLowerCase().contains(query)) {
                        filteredList.add(item);
                    }
                }
                ChonDonViadapter.updateList(filteredList);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Phím enter trên bàn phím cứng/ảo
        edtTimKiem.setOnEditorActionListener((v, actionId, event) -> {
            String query = edtTimKiem.getText().toString().trim().toLowerCase();
            List<ThongTinCacQuocGia> filteredList = new ArrayList<>();
            for (ThongTinCacQuocGia item : ThongTinCacQuocGiaList) {
                if (item.getTenQuocGia().toLowerCase().contains(query) || item.getMaTienTe().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }
            ChonDonViadapter.updateList(filteredList);
            Toast.makeText(this, "Tìm thấy " + filteredList.size() + " đồng tiền!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}
