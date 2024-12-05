package com.example.rsser.View.Index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.R;


import java.util.List;

public class IndexAdpter extends RecyclerView.Adapter<IndexAdpter.ViewHolder> {

    private List<Item> itemList;
    private Context context;

    // 数据模型类
    public static class Item {
        String title;
        String content;
        String url;
        String src;
        String pubdata;
        public Item(String title, String pubdata, String src, String url, String content) {
            this.title = title;
            this.pubdata = pubdata;
            this.src = src;
            this.url = url;
            this.content = content;
        }
    }

    // 构造函数
    public IndexAdpter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
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
        holder.content.setText(item.content);
//        holder.headerImage.setImageResource(item.url);
        holder.src.setText(item.src);
        holder.pubdata.setText(item.pubdata);
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
            headerImage = itemView.findViewById(R.id.header_image);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            src = itemView.findViewById(R.id.src);
            pubdata = itemView.findViewById(R.id.pubdata);
        }
    }
}
