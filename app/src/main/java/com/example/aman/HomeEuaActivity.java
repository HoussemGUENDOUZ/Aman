package com.example.aman;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeEuaActivity extends AppCompatActivity {
    List<EmergencyCase> emergencyCases = new ArrayList<>();
    EmergencyCaseAdapter adapter;
    RecyclerView casesRV;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    private static final String CHANNEL_ID = "emergency_cases_channel";
    String currentUserKey,unitKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_eua);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        casesRV = findViewById(R.id.EmergencyCasesRV);
        casesRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmergencyCaseAdapter(this,emergencyCases);
        casesRV.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initNotificationChannel();
        fetchEmergencyCases();
    }
    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);  // Change MainActivity to your activity that should be opened
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.red_ball)  // Replace with your icon resource
                .setContentTitle("New Emergency Case")
                .setContentText("a new emergency case is detected")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Emergency Cases";
            String description = "Channel for Emergency Case notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void fetchEmergencyCases() {
        if (user != null) {
            String currentUserEmail = user.getEmail();
            databaseReference.child("users").orderByChild("email").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            // Assuming the user node has an 'agent_id' child
                            currentUserKey = userSnapshot.getKey();
                            databaseReference.child("emergencyUnits").orderByChild("agent_id").equalTo(currentUserKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot unitSnapshot : snapshot.getChildren()) {
                                            unitKey = unitSnapshot.getKey();
                                            databaseReference.child("emergencyCases").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    emergencyCases.clear();
                                                    for (DataSnapshot caseSnapshot : snapshot.getChildren()) {
                                                        EmergencyCase emergencyCase = caseSnapshot.getValue(EmergencyCase.class);
                                                        if (emergencyCase.emergency_unit_id.equals(unitKey)){
                                                            //Toast.makeText(getApplicationContext(),caseSnapshot.getKey(),Toast.LENGTH_SHORT).show();
                                                            emergencyCases.add(emergencyCase);
                                                        }
                                                    }
                                                    sendNotification();
                                                    adapter = new EmergencyCaseAdapter(getApplicationContext(),emergencyCases);
                                                    casesRV.setAdapter(adapter);
                                                    //adapter.notifyDataSetChanged();
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.w("Firebase Data", "loadEmergencyCases:onCancelled", error.toException());
                                                }
                                            });
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.w("Firebase Data", "loadEmergencyUnits:onCancelled", error.toException());
                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Firebase Data", "loadUsers:onCancelled", error.toException());
                }
            });
        }
    }
}