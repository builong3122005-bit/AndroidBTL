package com.example.baitaplon.trang_chuyendoi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.R;
import com.example.baitaplon.model.ThongTinCacQuocGia;

import java.util.List;

/**
 * Adapter quản lý danh sách hiển thị các quốc gia để chọn tiền tệ quy đổi.
 */
public class ThongTinCacQuocGiaADapter extends RecyclerView.Adapter<ThongTinCacQuocGiaADapter.ThongTinCacQuocGiaViewHolder> {
    private List<ThongTinCacQuocGia> thongTinCacQuocGiaList;
    private Context context;
    private OnItemClickListener listener;

    public ThongTinCacQuocGiaADapter(List<ThongTinCacQuocGia> thongTinCacQuocGiaList, Context context) {
        this.context = context;
        this.thongTinCacQuocGiaList = thongTinCacQuocGiaList;
    }

    public interface OnItemClickListener {
        void onItemClick(ThongTinCacQuocGia item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ThongTinCacQuocGiaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chon_don_vi_chuyen_doi, parent, false);
        return new ThongTinCacQuocGiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThongTinCacQuocGiaViewHolder holder, int position) {
        ThongTinCacQuocGia thongTin = thongTinCacQuocGiaList.get(position);
        holder.tenQuocGia.setText(thongTin.getTenQuocGia());
        holder.maTienTe.setText(thongTin.getMaTienTe());
        holder.hinhQuocGia.setImageResource(thongTin.getHinhQuocGia());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(thongTin);
        });
    }

    @Override
    public int getItemCount() {
        return thongTinCacQuocGiaList.size();
    }

    public static class ThongTinCacQuocGiaViewHolder extends RecyclerView.ViewHolder {
        TextView maTienTe, tenQuocGia;
        ImageView hinhQuocGia;

        public ThongTinCacQuocGiaViewHolder(View itemView) {
            super(itemView);
            tenQuocGia = itemView.findViewById(R.id.txtTenQuocGia);
            maTienTe = itemView.findViewById(R.id.txtMaTienTe);
            hinhQuocGia = itemView.findViewById(R.id.imgContentFlag || R.id.imgAnhNuoc);
            // Let's use the exact view IDs from the original view_file:
            // R.id.imgAnhNuoc is used in the original.
            hinhQuocGia = itemView.findViewById(R.id.imgAnhNuoc);
        }
    }

    // Cập nhật lại danh sách sau khi lọc qua ô tìm kiếm
    public void updateList(List<ThongTinCacQuocGia> newList) {
        this.thongTinCacQuocGiaList = newList;
        notifyDataSetChanged();
    }
}
