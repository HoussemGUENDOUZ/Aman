package com.example.aman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Contact extends AppCompatActivity {
    Button valider_operation , annuler_operation;
    String spId;
    Intent intent,intent1,intent2;
    String recivedId ,firstName , lastName ,description , type,phoneNumber;
    Bundle bundle;
    ServiceProvider serviceProvider;
    TextView nameTV,specialiteTV,ratingTV,phoneNumberTV, distanceTV;
    ImageView spImage;
    ImageButton call_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);
        valider_operation= findViewById(R.id.valider_operation);
        annuler_operation= findViewById(R.id.annuler_operation);
        call_btn= findViewById(R.id.call_btn);
        intent = getIntent();
        bundle = intent.getExtras();
        //recivedId = bundle.getString("orderId");
        recivedId = intent.getStringExtra("spId");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");;
        description =intent.getStringExtra("description");;
        type = intent.getStringExtra("type");
        phoneNumber = intent.getStringExtra("phoneNumber");
        //rating;

        //nameTV,specialiteTV,ratingTV,phoneNumberTV, distanceTV
        nameTV = findViewById(R.id.name);
        specialiteTV = findViewById(R.id.specialite);
        ratingTV = findViewById(R.id.ratingTV);
        phoneNumberTV = findViewById(R.id.phone_number);


        FirebaseDatabase.getInstance().getReference().child("serviceProviders").child(recivedId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceProvider = snapshot.getValue(ServiceProvider.class);


//                nameTV.setText(serviceProvider.getFirstName() + " " + serviceProvider.getLastName());
//                specialiteTV.setText(serviceProvider.getType());
//                ratingTV.setText(serviceProvider.getRating());
//                phoneNumberTV.setText(serviceProvider.getPhoneNumber());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

       // Toast.makeText(Contact.this,"this is spid "+ recivedId, Toast.LENGTH_SHORT).show();

        intent1 = new Intent(Contact.this , HomeActivity.class);
        intent2 = new  Intent(Contact.this , MecServiceProviderActivity.class);
//        if (intent !=null){
//             recivedId = intent.getStringExtra("orderId");
//
//        }
//        valider_operation.setOnClickListener(v -> {
//            bundle = intent.getExtras();
//            assert bundle != null;
//            recivedId = bundle.getString("orderId");
//
//                FirebaseDatabase.getInstance().getReference().child("orders").child(recivedId).child("status").setValue("delivered");
//
//            Toast.makeText(Contact.this,"votre opération a été effectuée avec succès", Toast.LENGTH_SHORT).show();
//            startActivity(intent1);
//
//        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                nameTV.setText(serviceProvider.getFirstName() + " " + serviceProvider.getLastName());
//                specialiteTV.setText(serviceProvider.getType());
//                ratingTV.setText(serviceProvider.getRating());
//                phoneNumberTV.setText(serviceProvider.getPhoneNumber());
//            }
//        }, 8000);


        nameTV.setText(firstName+ " " +lastName);
        specialiteTV.setText(type);
        phoneNumberTV.setText(phoneNumber);


        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
                startActivity(intent3);
            }
        });


        valider_operation.setOnClickListener(v -> {
            // Retrieve orderId directly from intent extras
            recivedId = intent.getStringExtra("orderId");

            if (recivedId != null) {
                FirebaseDatabase.getInstance().getReference()
                        .child("orders").child(recivedId).child("status").setValue("delivered");

                Toast.makeText(Contact.this, "Votre opération a été effectuée avec succès", Toast.LENGTH_SHORT).show();
                startActivity(intent1);
            } else {
                Toast.makeText(Contact.this, "Erreur: Impossible de récupérer l'ID de la commande", Toast.LENGTH_SHORT).show();
            }
        });

        annuler_operation.setOnClickListener(v -> {
            bundle = intent.getExtras();
            assert bundle != null;
            recivedId = bundle.getString("orderId");

            FirebaseDatabase.getInstance().getReference().child("orders").child(recivedId).child("status").setValue("canceled");

            Toast.makeText(Contact.this,"votre opération a été anuler", Toast.LENGTH_SHORT).show();
            startActivity(intent1);
            finish();

        });
// send data to activity
//        String data = getIntent().getStringExtra("demande");
//        TextView tv = (TextView) findViewById(R.id.contact);
//        tv.setText(data);
    }
}