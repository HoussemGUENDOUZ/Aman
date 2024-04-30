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
import java.util.Date;
import java.util.List;
import java.util.Locale;

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







//
//
//        demander.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent intent = new Intent(context,Contact.class);
//                //boolean status = serviceProvider.isStatus();
//                String demande = "demande arriver";
//                //Intent intent = new Intent(HomeActivity.this, MecServiceProviderActivity.class);
//
//                intent.putExtra("demande",demande);
//                context.startActivity(intent);
//                // create new order
//                Date currentDate = new Date();
//                DatabaseReference oredersRef = FirebaseDatabase.getInstance().getReference("orders");
//                String orderId = oredersRef.push().getKey();
//
////                Order order = new Order();
////                int i = 1;
////                order.setId(i);
////                i++;
////                order.setStatus("pending");
////                order.setRating(4);
////                order.setCreated_at(currentDate);
////                oredersRef.child(orderId).setValue(order);
//            }
//        });
        return layout;


    }
}
