package com.example.aman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ServiceAvailableAdapter extends RecyclerView.Adapter<ServiceAvailableAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ServicesAvailable service);
    }
    private int resourceLayout;
    private Context context;
    private static List<ServicesAvailable> items;
    private OnItemClickListener listener;

    public ServiceAvailableAdapter(Context context, int resource, List<ServicesAvailable> items,OnItemClickListener listener) {
        this.resourceLayout = resource;
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceLayout, parent, false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServicesAvailable service = items.get(position);
        holder.imageView.setImageResource(service.service_img);
        holder.textView.setText(service.service_type);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.service_type_imgIV);
            textView = itemView.findViewById(R.id.service_type_textTV);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ServicesAvailable service = items.get(position);
                        listener.onItemClick(service); // Pass any other data you need here
                    }
                }
            });
        }
    }
}