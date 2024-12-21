package com.example.rsser.View.Index;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.R;
import com.example.rsser.View.Detail.DetailActivity;


import java.util.List;

public class IndexAdpter extends RecyclerView.Adapter<IndexAdpter.ViewHolder> {

    private List<Item> itemList;
    private Context context;

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    // 数据模型类
    public static class Item {
        String title;
        String content;
        String url; // 此 Item 的链接
        String src; // Source 的名称
        String pubdata;
        public Item(String title, String pubdata, String src, String url, String content) {
            this.title = title;
            this.pubdata = pubdata;
            this.src = src;
            this.url = url;
            this.content = content;
        }

        public String getTitle() {
            return this.title;
        }
    }

    // 构造函数
    public IndexAdpter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }
    public IndexAdpter(Context context) {
        this.context = context;
        this.itemList = new java.util.ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.title.setText(item.title);
//        holder.content.setText(item.content);
//        holder.headerImage.setImageResource(item.url);
        holder.src.setText(item.src);
        holder.pubdata.setText(item.pubdata);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);  // 创建一个意图，用于启动 DetailActivity
            intent.putExtra("title", item.title);
            intent.putExtra("content", item.content);
            intent.putExtra("url", item.url);
            intent.putExtra("src", item.src);
            intent.putExtra("pubdata", item.pubdata);
            context.startActivity(intent); // 使用上下文启动 DetailActivity
        });
    }

    public void addAll(List<Item> newItems) {
        itemList.addAll(newItems); // 将新的项添加到适配器列表
        notifyDataSetChanged(); // 通知适配器数据已更改
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder 类
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headerImage;
        TextView title;
        TextView content;
        TextView src; // 发布源的名称
        TextView pubdata;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            headerImage = itemView.findViewById(R.id.header_image);
            title = itemView.findViewById(R.id.title);
//            content = itemView.findViewById(R.id.content);
            src = itemView.findViewById(R.id.src);
            pubdata = itemView.findViewById(R.id.pubdata);
        }
    }
}
