package com.example.rsser.View.RssManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.DAO.Type;

import java.util.List;

interface OnTypeClickListener {
    void onTypeClick(Type type);
}

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {

    private List<Type> typeList;
    private OnTypeClickListener listener;

    public TypeAdapter(List<Type> typeList, OnTypeClickListener listener) {
        this.typeList = typeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String type = typeList.get(position).getName();
        holder.textView.setText(type);
        holder.textView.setTag(typeList.get(position).getId());

        // 设置点击事件  
        holder.itemView.setOnClickListener(v -> listener.onTypeClick(typeList.get(position)));
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
    public void addType(Type t) {
        this.typeList.add(t);
    }
}