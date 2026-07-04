package com.example.baitaplon.trang_tintuc;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.R;
import com.example.baitaplon.model.NewsArticle;
import com.example.baitaplon.tienich.NguonTinTuc;
import com.example.baitaplon.trang_chuyendoi.ConvertActivity;
import com.example.baitaplon.trang_caidat.CaiDatActivity;
import com.example.baitaplon.trang_thitruong.ThiTruongActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Màn hình Tin tức: hiển thị tin kinh tế trong nước (cào Jsoup) và tin quốc tế (NewsAPI).
 */
public class NewsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Mặc định ban đầu hiển thị tin trong nước
        loadDomesticNews();

        // Cài đặt bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_news);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_convert) {
                Intent intent = new Intent(NewsActivity.this, ConvertActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.bottom_market) {
                Intent intent = new Intent(NewsActivity.this, ThiTruongActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.bottom_news) {
                return true;
            } else if (itemId == R.id.bottom_profile) {
                Intent intent = new Intent(NewsActivity.this, CaiDatActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Thiết lập sự kiện đổi bộ lọc tin trên Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_trongnuoc) {
                loadDomesticNews();
                return true;
            } else if (itemId == R.id.bottom_quocte) {
                loadInternationalNews();
                return true;
            }
            return false;
        });
    }

    // Tải tin tức Việt Nam (chạy trên thread phụ tránh đơ UI)
    private void loadDomesticNews() {
        new Thread(() -> {
            List<NewsArticle> list = new ArrayList<>();
            try {
                list.addAll(NguonTinTuc.fetchCafeF());
                list.addAll(NguonTinTuc.fetchVietNamBiz());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Mock dữ liệu cứng dự phòng nếu không kết nối được
            if (list.isEmpty()) {
                list.add(new NewsArticle(
                        "Tỷ giá USD hôm nay trong nước đồng loạt giảm sâu",
                        "Giá USD tại các ngân hàng thương mại sáng nay ghi nhận xu hướng giảm mạnh so với hôm qua.",
                        "10 phút trước - CafeF",
                        "",
                        "https://cafef.vn"
                ));
                list.add(new NewsArticle(
                        "Ngân hàng Nhà nước tiếp tục hút ròng tín phiếu",
                        "Động thái mới nhằm ổn định tỷ giá thị trường tự do trước áp lực từ lạm phát toàn cầu.",
                        "1 giờ trước - VietNamBiz",
                        "",
                        "https://vietnambiz.vn"
                ));
                list.add(new NewsArticle(
                        "Thị trường vàng miếng SJC biến động khó lường",
                        "Khoảng cách giữa giá vàng trong nước và thế giới vẫn duy trì ở mức cao kỷ lục.",
                        "3 giờ trước - CafeF",
                        "",
                        "https://cafef.vn"
                ));
            }

            final List<NewsArticle> finalNewsList = list;
            runOnUiThread(() -> {
                newsAdapter = new NewsAdapter(finalNewsList);
                recyclerView.setAdapter(newsAdapter);
            });
        }).start();
    }

    // Tải tin tức thế giới
    private void loadInternationalNews() {
        new Thread(() -> {
            List<NewsArticle> list = new ArrayList<>();
            try {
                list.addAll(NguonTinTuc.apiNews_en());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Mock dữ liệu cứng dự phòng
            if (list.isEmpty()) {
                list.add(new NewsArticle(
                        "Fed phát tín hiệu sẽ sớm cắt giảm lãi suất",
                        "Chủ tịch Cục Dự trữ Liên bang Mỹ chỉ ra rằng áp lực lạm phát đang dần hạ nhiệt trên toàn cầu.",
                        "2 giờ trước - Reuters",
                        "",
                        "https://reuters.com"
                ));
                list.add(new NewsArticle(
                        "Đồng Euro tăng giá mạnh trước cuộc họp chính sách của ECB",
                        "Thị trường kỳ vọng Ngân hàng Trung ương Châu Âu sẽ duy trì lập trường cứng rắn khi kinh tế phục hồi.",
                        "4 giờ trước - Bloomberg",
                        "",
                        "https://bloomberg.com"
                ));
                list.add(new NewsArticle(
                        "Thị trường tiền tệ toàn cầu biến động theo chính sách thương mại mới",
                        "Các đồng tiền chủ chốt biến động liên tục khi các thỏa thuận thương mại quốc tế chuẩn bị được sửa đổi.",
                        "6 giờ trước - CNBC",
                        "",
                        "https://cnbc.com"
                ));
            } else if (list.size() > 20) {
                list = list.subList(0, 20); // Giới hạn lấy 20 bản tin
            }

            final List<NewsArticle> finalNewsList = list;
            runOnUiThread(() -> {
                newsAdapter = new NewsAdapter(finalNewsList);
                recyclerView.setAdapter(newsAdapter);
            });
        }).start();
    }
}
