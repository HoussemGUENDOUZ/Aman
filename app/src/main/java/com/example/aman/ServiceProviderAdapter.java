package com.example.aman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


      //  imageView.setImageResource(serviceProvider.getService_img());
        name.setText(serviceProvider.getFirstName()+ " " +serviceProvider.getLastName());
        exprience.setText(String.valueOf(serviceProvider.getExperience()) + " years of experience ");

//        imageView.setImageResource(items.get(position).service_img);
//        textView.setText(items.get(position).service_type);
        return layout;


    }
}
