package com.example.baitaplon.trang_thitruong;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.R;
import com.example.baitaplon.model.ExchangeRateItem;
import com.example.baitaplon.trang_bieudo.BieuDoActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter quản lý hiển thị các dòng tỉ giá ngoại tệ so với USD ở trang Thị trường.
 */
public class ExchangeRateAdapter extends RecyclerView.Adapter<ExchangeRateAdapter.ViewHolder> implements Filterable {
    private List<ExchangeRateItem> items;
    private List<ExchangeRateItem> filteredItems; 

    public ExchangeRateAdapter(List<ExchangeRateItem> items) {
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
    }

    // Cập nhật dữ liệu tỉ giá và vẽ lại RecyclerView
    public void updateData(List<ExchangeRateItem> newItems) {
        this.items = newItems;
        this.filteredItems = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exchange_rate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExchangeRateItem item = filteredItems.get(position);
        holder.imgFlag.setImageResource(item.getFlagResId());
        holder.tvCurrencyCode.setText(item.getCurrencyCode());
        holder.tvCountryName.setText(item.getCountryName());

        double rate = item.getRateAgainstUsd();
        holder.tvExchangeRateValue.setText(formatRate(rate));

        // Bấm vào dòng tỷ giá -> mở biểu đồ nến lịch sử tương ứng
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BieuDoActivity.class);
            intent.putExtra("DulieuBieuDo", "USD/" + item.getCurrencyCode());
            intent.putExtra("BienDongGia", "1 USD = " + formatRate(rate) + " " + item.getCurrencyCode());
            v.getContext().startActivity(intent);
        });
    }

    // Định dạng số tỷ giá (giữ nhiều số thập phân nếu tỷ giá rất nhỏ)
    private String formatRate(double rate) {
        if (rate == 0) return "0";
        double absRate = Math.abs(rate);
        if (absRate < 0.0001) {
            return new DecimalFormat("0.########").format(rate);
        } else if (absRate < 0.01) {
            return new DecimalFormat("0.######").format(rate);
        } else {
            return new DecimalFormat("###,###.####").format(rate);
        }
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    // Bộ lọc hỗ trợ tìm kiếm theo mã tiền hoặc tên quốc gia
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                List<ExchangeRateItem> resultsList = new ArrayList<>();
                if (query.isEmpty()) {
                    resultsList.addAll(items);
                } else {
                    for (ExchangeRateItem item : items) {
                        if (item.getCurrencyCode().toLowerCase().contains(query) ||
                            item.getCountryName().toLowerCase().contains(query)) {
                            resultsList.add(item);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = resultsList;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems.clear();
                if (results.values != null) {
                    filteredItems.addAll((List<ExchangeRateItem>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFlag;
        TextView tvCurrencyCode;
        TextView tvCountryName;
        TextView tvExchangeRateValue;

        ViewHolder(View itemView) {
            super(itemView);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            tvCurrencyCode = itemView.findViewById(R.id.tvCurrencyCode);
            tvCountryName = itemView.findViewById(R.id.tvCountryName);
            tvExchangeRateValue = itemView.findViewById(R.id.tvExchangeRateValue);
        }
    }
}
