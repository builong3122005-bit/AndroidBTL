package com.example.baitaplon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * ThongTinCacQuocGiaADapter - Bộ điều hợp (Adapter) quản lý danh sách chọn đơn vị tiền tệ.
 */
public class ThongTinCacQuocGiaADapter extends RecyclerView.Adapter<ThongTinCacQuocGiaADapter.ThongTinCacQuocGiaViewHolder> {
    private List<ThongTinCacQuocGia> thongTinCacQuocGiaList;
    private Context context;
    private OnItemClickListener listener;

    public ThongTinCacQuocGiaADapter(List<ThongTinCacQuocGia> thongTinCacQuocGiaList, Context context) {
        this.context = context;
        this.thongTinCacQuocGiaList = thongTinCacQuocGiaList;
    }

    // Giao diện (Interface) để bắt sự kiện click vào từng dòng đơn vị tiền tệ
    public interface OnItemClickListener {
        void onItemClick(ThongTinCacQuocGia item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ThongTinCacQuocGiaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Ánh xạ layout từng dòng item từ XML item_chon_don_vi_chuyen_doi
        View view = LayoutInflater.from(context).inflate(R.layout.item_chon_don_vi_chuyen_doi, parent, false);
        return new ThongTinCacQuocGiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThongTinCacQuocGiaViewHolder holder, int position) {
        // Thiết lập dữ liệu cờ nước, tên nước, mã tiền tệ cho từng dòng
        ThongTinCacQuocGia thongTin = thongTinCacQuocGiaList.get(position);
        holder.tenQuocGia.setText(thongTin.getTenQuocGia());
        holder.maTienTe.setText(thongTin.getMaTienTe());
        holder.hinhQuocGia.setImageResource(thongTin.getHinhQuocGia());
        
        // Sự kiện click chọn dòng
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(thongTin);
        });
    }

    @Override
    public int getItemCount() {
        return thongTinCacQuocGiaList.size();
    }

    /**
     * ViewHolder chứa các view hiển thị thông tin tiền tệ của một quốc gia.
     */
    public static class ThongTinCacQuocGiaViewHolder extends RecyclerView.ViewHolder {
        TextView maTienTe, tenQuocGia;
        ImageView hinhQuocGia;

        public ThongTinCacQuocGiaViewHolder(View itemView) {
            super(itemView);
            tenQuocGia = itemView.findViewById(R.id.txtTenQuocGia);
            maTienTe = itemView.findViewById(R.id.txtMaTienTe);
            hinhQuocGia = itemView.findViewById(R.id.imgAnhNuoc);
        }
    }

    /**
     * Cập nhật danh sách mới (dùng khi tìm kiếm) và làm mới giao diện.
     */
    public void updateList(List<ThongTinCacQuocGia> newList) {
        this.thongTinCacQuocGiaList = newList;
        notifyDataSetChanged();
    }
}
