package com.example.baitaplon;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * NewsAdapter - Bộ điều hợp (Adapter) quản lý việc hiển thị danh sách tin tức trong RecyclerView.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsArticle> newsArticles;

    public NewsAdapter(List<NewsArticle> newsArticles) {
        this.newsArticles = newsArticles;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Tạo view hiển thị cho từng item tin tức từ XML layout R.layout.item_news
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        // Đưa dữ liệu bài viết tương ứng lên giao diện
        NewsArticle article = newsArticles.get(position);
        holder.title.setText(article.getTitle());
        holder.description.setText(article.getDescription());
        holder.publishedAt.setText(article.getPublishedAt());

        // Thiết lập hình ảnh đại diện tĩnh (placeholder) cho tin tức
        holder.thumbnail.setImageResource(R.drawable.ic_anh);

        // Khi người dùng click chọn một tin tức -> Mở trình duyệt ngoài để xem chi tiết bài báo
        holder.itemView.setOnClickListener(v -> {
            String link = article.getLink();
            if (link != null && !link.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }

    /**
     * ViewHolder chứa và ánh xạ các view giao diện của từng dòng tin tức.
     */
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, publishedAt;
        ImageView thumbnail;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            publishedAt = itemView.findViewById(R.id.tvPublishedAt);
            thumbnail = itemView.findViewById(R.id.imgThumbnail);
        }
    }
}
