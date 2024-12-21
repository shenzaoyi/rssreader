package com.example.rsser.View.RssManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.DAO.Source;

import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {
    private List<Source> sourceList;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public SourceAdapter(List<Source> sourceList) {
        this.sourceList = sourceList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Source source = sourceList.get(position);
        holder.textViewTitle.setText(source.getTitle());
        holder.textViewTitle.setTag(source.getId());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(source);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(source);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;

        ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Source source);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Source source);
    }
}