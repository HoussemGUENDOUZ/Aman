package com.example.aman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MecServiceProviderActivity extends AppCompatActivity {

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mec_service_provider);

        database = FirebaseDatabase.getInstance().getReference("serviceProviders");

        // send sp type
        String data = getIntent().getStringExtra("serviceType");
        TextView tv = (TextView) findViewById(R.id.edited);
        tv.setText(data);

        ListView listView = findViewById(R.id.listServiceProviders);
        List<ServiceProvider> items = new ArrayList<>();
        ServiceProviderAdapter adapter = new ServiceProviderAdapter(MecServiceProviderActivity.this, R.layout.item_service_provider,items);
        listView.setAdapter(adapter);
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ServiceProvider serviceProvider = snapshot.getValue(ServiceProvider.class);


                if (serviceProvider.getType().equals(data)) {

                    items.add(serviceProvider);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               // for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                ServiceProvider serviceProvider = snapshot.getValue(ServiceProvider.class);


                //}
                // myAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public void goToRoutingActivity(View view) {
        Intent intent = new Intent(this, RoutingActivity.class);
        startActivity(intent);
    }
}