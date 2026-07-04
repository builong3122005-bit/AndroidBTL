package com.example.baitaplon.tienich;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Các tiện ích đọc, ghi và sắp xếp tệp dữ liệu CSV lịch sử biến động tỷ giá.
 */
public class CsvUtils {

    // Lấy tập hợp ngày đã có dữ liệu tỷ giá của một cặp tiền tệ trong file CSV để tránh lưu trùng lặp
    public static Set<String> getExistingDates(Context context, String slug) {
        Set<String> dates = new HashSet<>();
        File file = new File(context.getFilesDir(), "FileDuLieu.csv");
        if (!file.exists()) return dates;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("slug")) continue;
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[0].equals(slug)) {
                    dates.add(parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dates;
    }

    // Ghi thêm dòng tỷ giá mới vào file CSV
    public static void appendLine(Context context, String line) {
        File file = new File(context.getFilesDir(), "FileDuLieu.csv");
        boolean fileExists = file.exists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                bw.write("slug,date,open,high,low,close,currency");
                bw.newLine();
            }
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Chuyển đổi định dạng yyyy-MM-dd thành M/d/yyyy
    public static String convertToMdyyyy(String input) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
            return formatter.format(parser.parse(input));
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }

    // Sắp xếp lại file CSV theo dòng thời gian tăng dần từ cũ đến mới để vẽ nến đúng thứ tự
    public static void sortCsvFile(Context context, String slug) {
        File file = new File(context.getFilesDir(), "FileDuLieu.csv");
        if (!file.exists()) return;

        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirst = true;
            while ((line = br.readLine()) != null) {
                if (isFirst) {
                    isFirst = false; // Bỏ qua dòng tiêu đề
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[0].equalsIgnoreCase(slug)) {
                    rows.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sắp xếp các dòng dựa theo thời gian ngày
        rows.sort(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return normalizeDate(o1[1]).compareTo(normalizeDate(o2[1]));
            }
        });

        // Ghi đè lại toàn bộ dữ liệu đã sắp xếp vào tệp CSV
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("slug,date,open,high,low,close,currency");
            bw.newLine();
            for (String[] parts : rows) {
                bw.write(String.join(",", parts));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Chuẩn hóa ngày dạng yyyy-MM-dd về yyyyMMdd để so sánh
    private static String normalizeDate(String date) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            return formatter.format(parser.parse(date));
        } catch (Exception e) {
            return date;
        }
    }
}
