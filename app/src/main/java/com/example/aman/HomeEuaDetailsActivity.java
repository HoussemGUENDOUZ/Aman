package com.example.aman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeEuaDetailsActivity extends AppCompatActivity {
    FirebaseUser user;
    List<EmergencyCase> emergencyCases = new ArrayList<>();
    private DatabaseReference databaseReference;
    String currentUserKey,unitKey;
    ImageView emergencyImage;
    TextView emergencyDescription;
    StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_eua_details);


        emergencyDescription=findViewById(R.id.emergencyDescription);
        emergencyImage=findViewById(R.id.euaImage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        String caseId = intent.getStringExtra("case_id");
        long caseTime = intent.getLongExtra("case_time", -1);
        String caseType = intent.getStringExtra("case_type");
        String caseDescription = intent.getStringExtra("case_description");
        String caseImage = intent.getStringExtra("case_image");
        // Use the received data to populate the UI or perform other actions
        //Log.d("img",caseImage);
        if (caseType != null) {
            emergencyDescription.setText(caseType);
        }
        if (caseImage != null) {
            String compltUrl= "https://firebasestorage.googleapis.com/v0/b/your-firebase-app.appspot.com/o/emergencyPhotos%2F" + caseImage + "?alt=media";
            Log.d("img2",caseImage);

            loadImageFromUrl(caseImage);
            // loadImageFromFirebase(caseImage);
            //loadImageFromUrl("https://firebasestorage.googleapis.com/v0/b/aman-90584.appspot.com/o/emergencyPhotos%2FJPEG_20240622_153625_1540633218112927220.jpg?alt=media&token=f621c0da-6176-44d4-baf9-f4295ea39305");
        }


    }
    private void loadImageFromUrl(String imageUrl) {
        try {
            Picasso.get()
                    .load(imageUrl)
                    .into(emergencyImage);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImageFromFirebase(String imageUrl) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String imageURL = "emergencyPhotos/"+imageUrl;
        StorageReference storageRef = storage.getReferenceFromUrl(imageURL);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .into(emergencyImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(HomeEuaDetailsActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


//        if (caseImage != null) {
//            Toast.makeText(getApplicationContext(), caseImage, Toast.LENGTH_SHORT).show();
//            // Here you might want to load the image from a URL or Firebase Storage reference
//            // Assuming caseImage is a URL or a path to the image file
//            // For demonstration, let's say you decode it from Base64 (if it was encoded)
//            try {
//                byte[] decodedString = android.util.Base64.decode(caseImage, android.util.Base64.DEFAULT);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                emergencyImage.setImageBitmap(decodedByte);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
//            }
//        }
//        emergencyImage = findViewById(R.id.euaImage);
//            if (user != null) {
//                String currentUserEmail = user.getEmail();
//                databaseReference.child("users").orderByChild("email").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                                // Assuming the user node has an 'agent_id' child
//                                currentUserKey = userSnapshot.getKey();
//                                databaseReference.child("emergencyUnits").orderByChild("agent_id").equalTo(currentUserKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        if (snapshot.exists()) {
//                                            for (DataSnapshot unitSnapshot : snapshot.getChildren()) {
//                                                unitKey = unitSnapshot.getKey();
//                                                databaseReference.child("emergencyCases").orderByChild("emergency_unit_id").equalTo(unitKey).addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        emergencyCases.clear();
//                                                        for (DataSnapshot caseSnapshot : snapshot.getChildren()) {
//                                                            EmergencyCase emergencyCase = caseSnapshot.getValue(EmergencyCase.class);
//                                                            if (emergencyCase.emergency_unit_id.equals(unitKey)){
//                                                                //Toast.makeText(getApplicationContext(),caseSnapshot.getKey(),Toast.LENGTH_SHORT).show();
//                                                                emergencyCases.add(emergencyCase);
//                                                                String imgUrl= "emergencyPhotos/"+emergencyCase.getImage();
//                                                                storageReference=storageReference.child(imgUrl);
//                                                                long MAXBYTES= 1024*1024;
//                                                                storageReference.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                                                    @Override
//                                                                    public void onSuccess(byte[] bytes) {
//                                                                        //convert byte to bitmap
//                                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                                                                        emergencyImage.setImageBitmap(bitmap);
//                                                                    }
//                                                                }).addOnFailureListener(new OnFailureListener() {
//                                                                    @Override
//                                                                    public void onFailure(@NonNull Exception e) {
//
//                                                                    }
//                                                                });
//
//                                                            }
//                                                        }
//
//                                                    }
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//                                                        Log.w("Firebase Data", "loadEmergencyCases:onCancelled", error.toException());
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//                                        Log.w("Firebase Data", "loadEmergencyUnits:onCancelled", error.toException());
//                                    }
//                                });
//                            }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.w("Firebase Data", "loadUsers:onCancelled", error.toException());
//                    }
//                });
//            }
//    }
//
//

