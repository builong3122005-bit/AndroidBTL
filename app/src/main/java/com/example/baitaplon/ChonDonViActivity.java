package com.example.baitaplon;

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

import java.util.ArrayList;
import java.util.List;

/**
 * ChonDonViActivity - Màn hình chọn đơn vị tiền tệ nguồn hoặc đích.
 * Chứa danh sách các quốc gia, mã tiền tệ tương ứng và ảnh cờ nước đó.
 * Cung cấp chức năng tìm kiếm tiền tệ theo tên nước hoặc mã tiền tệ.
 */
public class ChonDonViActivity extends AppCompatActivity {
    private ImageView imgBack;
    private RecyclerView recyclerView;
    private ThongTinCacQuocGiaADapter ChonDonViadapter;
    
    // Danh sách tĩnh chứa dữ liệu tiền tệ giả lập của các nước
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
        
        // Căn lề thích ứng thanh trạng thái
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.ChuyenDoiRecyclerView);

        // Đóng màn hình khi nhấn nút quay lại (Back)
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Thiết lập Adapter cho danh sách RecyclerView hiển thị đơn vị tiền tệ
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChonDonViadapter = new ThongTinCacQuocGiaADapter(new ArrayList<>(ThongTinCacQuocGiaList), this);
        recyclerView.setAdapter(ChonDonViadapter);

        // Xử lý sự kiện chọn một đơn vị tiền tệ từ danh sách -> Trả kết quả về cho ConvertActivity
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

        // Xử lý ô tìm kiếm đơn vị tiền tệ
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
