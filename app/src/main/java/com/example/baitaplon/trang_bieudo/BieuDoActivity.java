package com.example.baitaplon.trang_bieudo;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baitaplon.R;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Màn hình Biểu đồ: Hiển thị biến động giá lịch sử tỷ giá dạng nến (Candlestick chart).
 */
public class BieuDoActivity extends AppCompatActivity {
    private TextView txtThoiGian, txtDate, tvBienDongGia;
    private CandleStickChart candleStickChart;
    private String DulieuBieuDo, BienDongGia;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bieu_do);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        candleStickChart = findViewById(R.id.candleStickChart);

        readCSVAndDisplayChart();

        txtThoiGian = findViewById(R.id.txtThoiGian);
        txtDate = findViewById(R.id.txtDate);
        tvBienDongGia = findViewById(R.id.tvBienDongGia);
        BienDongGia = getIntent().getStringExtra("BienDongGia");
        if (BienDongGia != null && !BienDongGia.isEmpty()) {
            tvBienDongGia.setText(BienDongGia);
        } else {
            tvBienDongGia.setText("1 USD = 25.400 VND");
        }
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());

        txtThoiGian.setText(time);
        txtDate.setText(date);
        
        imgBack = findViewById(R.id.imgBack);
        if (imgBack != null) {
            imgBack.setOnClickListener(v -> finish());
        }
    }

    // Đọc lịch sử tỷ giá từ file CSV lưu trữ cục bộ để hiển thị lên biểu đồ nến
    private void readCSVAndDisplayChart() {
        try {
            File file = new File(getFilesDir(), "FileDuLieu.csv");
            if (!file.exists()) {
                Log.e("CSV Error", "FileDuLieu.csv không tồn tại.");
                return;
            }

            CSVReader csvReader = new CSVReader(new FileReader(file));
            String[] row;
            ArrayList<CandleEntry> entries = new ArrayList<>();
            ArrayList<String> xLabels = new ArrayList<>();

            DulieuBieuDo = getIntent().getStringExtra("DulieuBieuDo");
            Log.d("CSV", "Đọc dữ liệu cho slug: " + DulieuBieuDo);

            csvReader.readNext(); // Bỏ qua dòng header của file CSV

            while ((row = csvReader.readNext()) != null) {
                if (row.length > 5) {
                    String slug = row[0];
                    if (slug.equals(DulieuBieuDo)) {
                        try {
                            float open = parseFloatSafe(row[2]);
                            float high = parseFloatSafe(row[3]);
                            float low = parseFloatSafe(row[4]);
                            float close = parseFloatSafe(row[5]);

                            entries.add(new CandleEntry(entries.size(), high, low, open, close));
                            xLabels.add(row[1]); 
                        } catch (NumberFormatException e) {
                            Log.e("CSV Error", "Lỗi dòng: " + row[0], e);
                        }
                    }
                }
            }

            if (!entries.isEmpty()) {
                CandleDataSet dataSet = new CandleDataSet(entries, DulieuBieuDo);

                // Thiết lập màu sắc nến theo chuẩn màu sắc thị trường (Đỏ giảm - Xanh tăng)
                dataSet.setDecreasingColor(android.graphics.Color.RED);
                dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
                dataSet.setIncreasingColor(android.graphics.Color.GREEN);
                dataSet.setIncreasingPaintStyle(Paint.Style.FILL);
                dataSet.setNeutralColor(android.graphics.Color.LTGRAY);
                dataSet.setShadowColor(android.graphics.Color.DKGRAY);
                dataSet.setShadowColorSameAsCandle(true);
                dataSet.setDrawValues(false); 

                XAxis xAxis = candleStickChart.getXAxis();
                xAxis.setDrawGridLines(true);
                xAxis.setGridColor(android.graphics.Color.DKGRAY);
                xAxis.setGridLineWidth(0.5f);
                xAxis.setDrawAxisLine(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setDrawLabels(true);
                xAxis.setLabelRotationAngle(-45f); 
                xAxis.setTextColor(android.graphics.Color.LTGRAY);
                xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xLabels));

                YAxis leftAxis = candleStickChart.getAxisLeft();
                leftAxis.setDrawGridLines(true);
                leftAxis.setGridColor(android.graphics.Color.DKGRAY);
                leftAxis.setGridLineWidth(0.5f);
                leftAxis.setTextColor(android.graphics.Color.LTGRAY);

                candleStickChart.getAxisRight().setEnabled(false);

                candleStickChart.setBackgroundColor(android.graphics.Color.parseColor("#383636")); 
                candleStickChart.getDescription().setEnabled(false);
                candleStickChart.setPinchZoom(true);
                candleStickChart.setScaleEnabled(true);
                candleStickChart.setDragEnabled(true);
                candleStickChart.setDrawBorders(false);
                candleStickChart.animateX(800); 
                
                candleStickChart.moveViewToX(entries.size() - candleStickChart.getVisibleXRange());

                CandleData candleData = new CandleData(dataSet);
                candleStickChart.setData(candleData);

                candleStickChart.setVisibleXRangeMaximum(35); 
                candleStickChart.invalidate(); 
            } else {
                Log.e("CSV Error", "Không có dữ liệu cho: " + DulieuBieuDo);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            Log.e("CSV Error", "Lỗi đọc file CSV", e);
        }
    }

    private float parseFloatSafe(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            Log.e("CSV Error", "Lỗi parse float: " + value, e);
            return 0f;
        }
    }
}
