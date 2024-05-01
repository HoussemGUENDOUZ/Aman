package com.example.aman;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class ServiceProviderAdapter extends ArrayAdapter<ServiceProvider> {
    private Context context;
    private int resourceLayout;
    List<ServiceProvider> items;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String currentUserKey;
     LoadingDialog loadingDialog ;

    public ServiceProviderAdapter(@NonNull Context context, int resource, List<ServiceProvider> items) {
        super(context, resource, items);
        this.context= context;
        this.resourceLayout = resource;
        this.items = items;
        loadingDialog = new LoadingDialog(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;

        if (layout == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            layout = inflater.inflate(resourceLayout, null);
        }

        ServiceProvider serviceProvider = items.get(position);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users");
        users.orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        currentUserKey = dataSnapshot.getKey();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ImageView imageView = layout.findViewById(R.id.sp_img);
        TextView name = layout.findViewById(R.id.spName);
        TextView exprience = layout.findViewById(R.id.experience);
        ImageView star = layout.findViewById(R.id.star1);
        // demander un service providere
        Button demander = layout.findViewById(R.id.demander);

      //  imageView.setImageResource(serviceProvider.getService_img());
        name.setText(serviceProvider.getFirstName()+ " " +serviceProvider.getLastName());
        exprience.setText(serviceProvider.getExperience() + " years of experience ");
//        imageView.setImageResource(items.get(position).service_img);
//        textView.setText(items.get(position).service_type);

        demander.setOnClickListener(v -> {
            loadingDialog.startLoadingDialog();

            //Intent intent = new Intent(context,Contact.class);
            //boolean status = serviceProvider.isStatus();
          //  String demande = "demande arriver";
            //Intent intent = new Intent(HomeActivity.this, MecServiceProviderActivity.class);

//            intent.putExtra("demande",demande);
//            context.startActivity(intent);
            // create new order
            //Date currentDate = new Date();
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
            String orderId = ordersRef.push().getKey();
            Order order = new Order();
            order.setId(orderId);
            order.setStatus("pending");
            order.setRating(4);
            //order.setCreated_at(Long.parseLong(ServerValue.TIMESTAMP));
            order.setCreated_at(System.currentTimeMillis());
            String serviceProviderId = items.get(position).getUser_id(); // Assuming you have a method to retrieve Firebase key
            order.setService_provider_id(serviceProviderId);
            //order.setService_provider_id();
            order.setClient_id(currentUserKey);
            assert orderId != null;
            ordersRef.child(orderId).setValue(order);
            
            
            // get order statut
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Order order= dataSnapshot.getValue(Order.class);
                        if (order != null && order.getId().equals(orderId) && order.getStatus().equals("accepted")){
                            loadingDialog.dismissDialog();
                            Intent intent = new Intent(getContext(),Contact.class);
                            context.startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        return layout;


    }
//    private void showProgressBar() {
//        // Create and show the progress bar
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View progressBarView = inflater.inflate(R.layout.progress_bar_layout, null);
//        ProgressBar progressBar = progressBarView.findViewById(R.id.progressBar);
//        ViewGroup rootView = (ViewGroup) ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
//        rootView.addView(progressBarView);
//    }
}
