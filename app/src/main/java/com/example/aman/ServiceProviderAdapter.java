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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

public class ServiceProviderAdapter extends ArrayAdapter<ServiceProvider> {
    private Context context;
    private int resourceLayout;
    List<ServiceProvider> items;
    private DatabaseReference database;

    public ServiceProviderAdapter(@NonNull Context context, int resource, List<ServiceProvider> items) {
        super(context, resource, items);
        this.context= context;
        this.resourceLayout = resource;
        this.items = items;
        database = FirebaseDatabase.getInstance().getReference("serviceProviders");

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;

        if (layout == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            layout = inflater.inflate(resourceLayout, null);
        }

        ServiceProvider serviceProvider = items.get(position);

        ImageView imageView = layout.findViewById(R.id.sp_img);
        TextView name = layout.findViewById(R.id.spName);
        TextView exprience = layout.findViewById(R.id.experience);
        ImageView star = layout.findViewById(R.id.star1);
        // demander un service providere
        Button demander = layout.findViewById(R.id.demander);

      //  imageView.setImageResource(serviceProvider.getService_img());
        name.setText(serviceProvider.getFirstName()+ " " +serviceProvider.getLastName());
        exprience.setText(String.valueOf(serviceProvider.getExperience()) + " years of experience ");
//        imageView.setImageResource(items.get(position).service_img);
//        textView.setText(items.get(position).service_type);

        demander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context,Contact.class);
                //boolean status = serviceProvider.isStatus();
                String demande = "demande arriver";
                //Intent intent = new Intent(HomeActivity.this, MecServiceProviderActivity.class);

                intent.putExtra("demande",demande);
                context.startActivity(intent);
                // create new order
                Date currentDate = new Date();
                DatabaseReference oredersRef = FirebaseDatabase.getInstance().getReference("orders");
                String orderId = oredersRef.push().getKey();

                Order order = new Order();
                int i = 1;
                order.setId(i);
                i++;
                order.setStatus("pending");
                order.setRating(4);
                order.setCreated_at(currentDate);
                oredersRef.child(orderId).setValue(order);
            }
        });
        return layout;


    }
}
