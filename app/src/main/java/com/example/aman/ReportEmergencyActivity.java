package com.example.aman;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ReportEmergencyActivity extends AppCompatActivity {
    ImageView imageView , police , civilProtection , gendarmerie,lastSelectedUnit=null, lastSelectedCase=null;
    EditText descriptionET;
    Button addImage , signaler;
    String  nearestUnit,currentUserKey;
    private LinearLayout caseLayout;
    private String currentPhotoPath;
    private Uri photoURI;
    private final static int REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS = 2;
    private ActivityResultLauncher<Intent> takePictureLauncher;


    FusedLocationProviderClient fusedLocationProviderClient;
    double latitude,longitude;
    FirebaseAuth mAuth;
    FirebaseUser user;
    User User1;
    LoadingDialog loadingDialog;

    String imageFileName;
    String pathComplet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_emergency);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        loadingDialog = new LoadingDialog(ReportEmergencyActivity.this,"veuillez patienter...",false);
        imageView = findViewById(R.id.profile_imgRE);
        imageView.setOnClickListener(this::goToProfile);
        // Get the LinearLayout for the "case" section
        caseLayout = findViewById(R.id.cas_icon);
        // report emergency
        police = findViewById(R.id.policeIV);
        civilProtection = findViewById(R.id.protectionCivileIV);
        gendarmerie = findViewById(R.id.gendarmerieIV);
        // Assign each ImageView a unique tag
        gendarmerie.setTag("gendarmerie");
        civilProtection.setTag("protection civile");
        police.setTag("police");
        descriptionET = findViewById(R.id.descriptionET);
        descriptionET.addTextChangedListener(reportwatcher);
        addImage = findViewById(R.id.addImg_btn);
        signaler = findViewById(R.id.signaler_btn);
        signaler.setEnabled(false);
        GradientDrawable normal = new GradientDrawable();
        normal.setStroke(1, Color.BLACK);
        GradientDrawable selected = new GradientDrawable();
        selected.setStroke(10,Color.BLACK);
        police.setBackground(normal);
        gendarmerie.setBackground(normal);
        civilProtection.setBackground(normal);
        View.OnClickListener selectlistener = v -> {
            // If there was a previously selected ImageView, reset its state
            if (lastSelectedUnit != null) {
                lastSelectedUnit.setBackground(normal);
            }
            // Set the clicked ImageView to the selected state
            v.setBackground(selected);
            // Update the last selected ImageView
            lastSelectedUnit = (ImageView) v;
            // If there was a previously selected case, reset its state
            if (lastSelectedCase != null) {
                lastSelectedCase.setBackground(normal);
                lastSelectedCase = null;
            }
            // Update the "case" section based on the selected "emergency unit"
            updateCaseSection((String) v.getTag(),normal,selected);
        };
        police.setOnClickListener(selectlistener);
        civilProtection.setOnClickListener(selectlistener);
        gendarmerie.setOnClickListener(selectlistener);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        currentUserKey = dataSnapshot.getKey();
                        User1 = dataSnapshot.getValue(User.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // take photo
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    dispatchTakePictureIntent();
                }
            }
        });
        signaler.setOnClickListener(v -> {
            if (lastSelectedUnit != null && lastSelectedCase != null) {
                loadingDialog.startLoadingDialog();
                getLastLocation();
                // Get the tags of the last selected ImageViews
                String selectedUnit = (String) lastSelectedUnit.getTag();
                String selectedCase = (String) lastSelectedCase.getTag();
                // Get the DatabaseReference for the "emergency_units" collection
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("emergencyUnits");
                if(photoURI !=null){
                    uploadPhotoToFirebase(photoURI);
                }else {
                    Toast.makeText(ReportEmergencyActivity.this,"Ajouter une photo s'il vous plait", Toast.LENGTH_SHORT).show();
                }
                if (currentPhotoPath != null) {
                    uploadImagePathToFirebase(currentPhotoPath);
                } else {
                    Toast.makeText(ReportEmergencyActivity.this, "Ajouter une photo s'il vous plait", Toast.LENGTH_SHORT).show();
                }
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Initialize the nearest emergency unit and the smallest distance
                        double smallestDistance = Double.MAX_VALUE;
                        // Iterate over all the emergency units
                        for (DataSnapshot unitSnapshot : snapshot.getChildren()) {
                            // Get the latitude and longitude of the emergency unit
                            double unitLatitude = unitSnapshot.child("latitude").getValue(Double.class);
                            double unitLongitude = unitSnapshot.child("longitude").getValue(Double.class);
                            String type = unitSnapshot.child("type").getValue(String.class);
                            if (Objects.equals(type, selectedUnit)){
                                // Calculate the distance to the emergency unit
                                double distance = distance(latitude,longitude, unitLatitude, unitLongitude);
                                // If the distance is smaller than the smallest distance so far, update the nearest emergency unit and the smallest distance
                                if (distance < smallestDistance) {
                                    nearestUnit = unitSnapshot.getKey();
                                    smallestDistance = distance;
                                }
                            }
                        }
                        Map<String,Object> map = new HashMap<>();
                        map.put("latitude",latitude);
                        map.put("longitude",longitude);
                        map.put("type",selectedCase);
                        map.put("description",descriptionET.getText().toString());
                        map.put("time",System.currentTimeMillis());
                        map.put("user_id",currentUserKey);
                        map.put("emergency_unit_id",nearestUnit);
                        map.put("image",pathComplet);
                    //    Log.d("imm",pathComplet);
                        FirebaseDatabase.getInstance().getReference().child("emergencyCases").push().setValue(map);
                        loadingDialog.dismissDialog();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReportEmergencyActivity.this);
                        builder.setTitle("envoyé avec succés");
                        builder.setMessage("Votre déclaration a  été envoyé au service d'urgence le plus proche de chez vous");
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            if (User1.getRole().equals("client")){
                                startActivity(new Intent(ReportEmergencyActivity.this, HomeActivity.class));
                                finish();
                            } else if (User1.getRole().equals("service provider")) {
                                startActivity(new Intent(ReportEmergencyActivity.this, HomeSpActivity.class));
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //Toast.makeText(ReportEmergencyActivity.this,"the nearest unit is : " + nearestUnit,Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            } else {
                Toast.makeText(ReportEmergencyActivity.this, "Please select an emergency unit and a case", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Method to calculate the distance between two geographic locations
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the earth in kilometers
        final int R = 6371;
        // Convert degrees to radians
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        // Apply the Haversine formula
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // Calculate the distance
        double d = R * c;
        return d;
    }
    // Method to update the "case" section based on the selected "emergency unit"
    private void updateCaseSection(String emergencyUnit,GradientDrawable normal, GradientDrawable selected) {
        // clear the case section
        caseLayout.removeAllViews();
        ImageView caseImageView1 = new ImageView(this);
        ImageView caseImageView2 = new ImageView(this);
        // Set the layout parameters for the ImageViews
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        caseImageView1.setLayoutParams(layoutParams);
        caseImageView2.setLayoutParams(layoutParams);
        switch (emergencyUnit) {
            case "police":
                caseImageView1.setImageResource(R.drawable.accident_icon);
                caseImageView1.setTag("accident");
                break;
            case "protection civile":
                caseImageView1.setImageResource(R.drawable.fire);
                caseImageView1.setTag("fire1");
                caseImageView2.setImageResource(R.drawable.fire);
                caseImageView2.setTag("fire2");
                break;
            case "gendarmerie":
                caseImageView1.setImageResource(R.drawable.earthquake_icon);
                caseImageView1.setTag("earthquake");
                break;
        }
        View.OnClickListener selectcaselistener = v -> {
            // If there was a previously selected ImageView, reset its state
            if (lastSelectedCase != null) {
                lastSelectedCase.setBackground(normal);
            }
            // Set the clicked ImageView to the selected state
            v.setBackground(selected);
            // Update the last selected ImageView
            lastSelectedCase = (ImageView) v;
        };
        caseImageView1.setBackground(normal);
        caseImageView1.setOnClickListener(selectcaselistener);
        caseImageView2.setBackground(normal);
        caseImageView2.setOnClickListener(selectcaselistener);
        caseLayout.addView(caseImageView1);
        caseLayout.addView(caseImageView2);
    }
    public void goToProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    TextWatcher reportwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @SuppressLint("UseCompatLoadingForColorStateLists")
        @Override
        public void afterTextChanged(Editable s) {
            String description = descriptionET.getText().toString().trim();
            signaler.setEnabled(!description.isEmpty() && lastSelectedCase!=null && lastSelectedUnit != null);
            if (!description.isEmpty() && lastSelectedCase != null && lastSelectedUnit != null){
                //fields are not empty
                signaler.setTextColor(Color.parseColor("#ffffff"));
                signaler.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.redE));
            }else {
                //both fields or one of them is empty
                signaler.setTextColor(Color.parseColor("#99ffffff"));
                signaler.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.redE2));
            }
        }
    };
    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null){
                    Geocoder geocoder = new Geocoder(ReportEmergencyActivity.this , Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        assert addresses != null;
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                        //Toast.makeText(ReportEmergencyActivity.this,Double.toString(latitude),Toast.LENGTH_LONG).show();
                        //Toast.makeText(ReportEmergencyActivity.this,Double.toString(longitude),Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }else {
            askPermission();
        }
    }
    public void askPermission(){
        ActivityCompat.requestPermissions(ReportEmergencyActivity.this , new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_CODE);
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_CODE){
//            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                getLastLocation();
//            }else {
//                Toast.makeText(this, "required permissions", Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    // of take photo
    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Permissions are required to use this feature", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.example.aman.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
         imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // The photo was successfully taken
            if (currentPhotoPath != null) {
                File photoFile = new File(currentPhotoPath);
                Uri photoUri = Uri.fromFile(photoFile);

                // Upload photo to Firebase Storage
                uploadPhotoToFirebase(photoUri);
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadPhotoToFirebase(Uri fileUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference photoRef = storageRef.child("emergencyPhotos/" + fileUri.getLastPathSegment());

        // Upload file to Firebase Storage
        photoRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL
                    photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        Log.d("imgg",downloadUrl);
                        pathComplet=downloadUrl;
                        //here
                        // Use downloadUrl (e.g., save to database, display image, etc.)
                        // Example: saveDownloadUrlToDatabase(downloadUrl);
                        // For now, let's just display the URL
                        Toast.makeText(ReportEmergencyActivity.this, "Upload successful. URL: " + downloadUrl, Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(exception -> {
                    // Handle failed upload
                    Toast.makeText(ReportEmergencyActivity.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

//    private void uploadPhotoToFirebase(Uri fileUri) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference photoRef = storageRef.child("emergencyPhotos/" + fileUri.getLastPathSegment());
//
//        photoRef.putFile(fileUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(ReportEmergencyActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Toast.makeText(ReportEmergencyActivity.this,  exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void uploadImagePathToFirebase(String imagePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Uri fileUri = Uri.fromFile(new File(imagePath));
        StorageReference photoRef = storageRef.child("emergencyPhotos").child(fileUri.getLastPathSegment());

        photoRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Handle successful upload
                    Toast.makeText(ReportEmergencyActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failed upload
                    Toast.makeText(ReportEmergencyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}