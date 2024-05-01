package com.example.aman;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderAdapter extends ArrayAdapter<Order> {

    private Context context;
    private int resourceLayout;
    List<Order> items;
    private DatabaseReference database;

    public OrderAdapter(@NonNull Context context, int resource, List<Order> items) {
        super(context, resource, items);
        this.context= context;
        this.resourceLayout = resource;
        this.items = items;
        database = FirebaseDatabase.getInstance().getReference("orders");
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        if (layout == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            layout = inflater.inflate(resourceLayout, null);
        }
        Order order = items.get(position);
        MaterialTextView  date = layout.findViewById(R.id.date);
        Button accept = layout.findViewById(R.id.accept_btn);
        Button refuse = layout.findViewById(R.id.cancel_btn);
        Date date1 = new Date(order.getCreated_at());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(date1);
        date.setText(formattedDate);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //database.orderByChild();
                FirebaseDatabase.getInstance().getReference("orders").child(order.getId()).child("status").setValue("refused");
            }
        });
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setStatus("refused");
            }
        });
        return layout;
    }
}
