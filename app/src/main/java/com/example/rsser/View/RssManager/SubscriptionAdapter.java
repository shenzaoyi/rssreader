package com.example.rsser.View.RssManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.DAO.Type;
import com.example.rsser.R;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    private List<Type> subscriptionTypes;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    public SubscriptionAdapter(List<Type> subscriptionTypes) {
        this.subscriptionTypes = subscriptionTypes;
    }
    public void addType(Type t) {
        this.subscriptionTypes.add(t);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Type type = subscriptionTypes.get(position);
        holder.subscriptionType.setText(subscriptionTypes.get(position).getName());
        int num = subscriptionTypes.get(position).getNum();
        // 真坑， 传入的 int 类型默认就换了方法，重载上了
        holder.typeNum.setText((String.valueOf(num)));
        holder.subscriptionType.setTextSize(25);
        holder.typeNum.setTextSize(15);
        holder.subscriptionType.setTag(subscriptionTypes.get(position).getId());
        holder.descriptionType.setText(subscriptionTypes.get(position).getDescription());
        holder.descriptionType.setTextSize(10);
        holder.itemView.setOnClickListener(v->{
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(type);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(v, type);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return subscriptionTypes.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Type type);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, Type type);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subscriptionType;
        TextView descriptionType;
        TextView typeNum;

        ViewHolder(View itemView) {
            super(itemView);
            subscriptionType = itemView.findViewById(R.id.typeName);
            descriptionType = itemView.findViewById(R.id.typeDesc);
            typeNum = itemView.findViewById(R.id.typeNum);
        }
    }
}