package com.example.aman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ServiceAvailableAdapter extends ArrayAdapter<ServicesAvailable> {

    private int resourceLayout;
    private Context context;

    List<ServicesAvailable> items;
    public ServiceAvailableAdapter(@NonNull Context context, int resource, List<ServicesAvailable> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.context = context;
        this.items = items;
    }

    public View getView(int position, View convertView , ViewGroup parent ){
        View layout =convertView;

        if (layout == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            layout = inflater.inflate(resourceLayout, null);
        }

        ServicesAvailable servicesAvailable = getItem(position);

        ImageView imageView = layout.findViewById(R.id.service_type_imgIV);
        TextView textView = layout.findViewById(R.id.service_type_textTV);
imageView.setImageResource(items.get(position).service_img);
textView.setText(items.get(position).service_type);
return layout;


    }
}
