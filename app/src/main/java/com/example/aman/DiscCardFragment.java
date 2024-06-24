package com.example.aman;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userKey;
    User user1;
    TextView name;

    private  final static int REQUEST_CODE=100;
    TextView address;

    public DiscCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscCardFragment newInstance(String param1, String param2) {
        DiscCardFragment fragment = new DiscCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_disc_card, container, false);
        ImageView imageView = view.findViewById(R.id.profile_img);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        address = view.findViewById(R.id.address);
        name = view.findViewById(R.id.name);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        user1 = dataSnapshot.getValue(User.class);
                    }
                }
                if (user1 != null) {
                    name.setText(user1.name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        getLastLocation();

//        address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLastLocation();
//            }
//        });

        return view;
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        Geocoder geocoder = new Geocoder(requireContext() , Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                          //  latitude.setText("latitude: " + addresses.get(0).getLatitude());
                          //  longitude.setText("longitude: " + addresses.get(0).getLongitude());
                            //address.setText("Address: " + addresses.get(0).getAddressLine(0));
                           // city.setText("city: " + addresses.get(0).getLocality());
                            //country.setText("country: " + addresses.get(0).getCountryName());


                            Double longitude = location.getLongitude();
                            Double latitude = location.getLatitude();
                            String position = String.valueOf(longitude+","+latitude);

                            FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            userKey = dataSnapshot.getKey();

                                        }

                                        FirebaseDatabase.getInstance().getReference().child("users").child(userKey).child("position").setValue(position);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });

                            //what this below !!
                            String street = addresses.get(0).getThoroughfare();
                            if (street != null) {
                                // Display street
                                //address.setText("Street: " + street);
                            } else {
                                // Street not available
                                //address.setText("Street: N/A");
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        String fullAddress = addresses.get(0).getAddressLine(0);
                        if (fullAddress.contains("+")) {
                            // This is a Plus Code, so let's display locality and country instead
                            address.setText(addresses.get(0).getLocality() +  ", "+addresses.get(0).getCountryName());
                        } else {
                            // This is a full address, so we can display it
                            address.setText( fullAddress);
                        }


                    }
                }
            });
        }else {
            askPermission();
        }
    }

    public void askPermission(){
        ActivityCompat.requestPermissions((Activity) requireContext(), new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(getContext(), "required permissions", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
