package com.example.aman;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceProviderAdapter extends ArrayAdapter<ServiceProvider> {
    private Context context;
    private int resourceLayout;
    List<ServiceProvider> items;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String currentUserKey;
    String userPosition , serviceProviderPosition;
    LoadingDialog loadingDialog ;
    User user1;

    public ServiceProviderAdapter(@NonNull Context context, int resource, List<ServiceProvider> items) {
        super(context, resource, items);
        this.context= context;
        this.resourceLayout = resource;
        this.items = items;
        loadingDialog = new LoadingDialog(context,"En attente d'une reponse du fournniseur ...",true);
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
                        user1 = dataSnapshot.getValue(User.class);
                        userPosition = user1.getPosition();

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        FirebaseDatabase.getInstance().getReference().child("users").child(serviceProvider.getUser_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ServiceProvider serviceProvider = snapshot.getValue(ServiceProvider.class);
                    serviceProviderPosition = serviceProvider.getPosition(); // Get the position of the service provider

                    Toast.makeText(context,"positions : user"+ userPosition +", sp: "+ serviceProviderPosition ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ImageView imageView = layout.findViewById(R.id.sp_img);
        TextView name = layout.findViewById(R.id.spName);
        TextView exprience = layout.findViewById(R.id.experience);
        ImageView star = layout.findViewById(R.id.star1);
        TextView distnace = layout.findViewById(R.id.distanceTv);
        // demander un service providere
        Button demander = layout.findViewById(R.id.demander);

      //  imageView.setImageResource(serviceProvider.getService_img());
        name.setText(serviceProvider.getFirstName()+ " " +serviceProvider.getLastName());
        exprience.setText(serviceProvider.getExperience() + " years of experience ");
//        imageView.setImageResource(items.get(position).service_img);
//        textView.setText(items.get(position).service_type);



         Toast.makeText(context,"user P : " + "userPosition" +" , sp P" + serviceProviderPosition,Toast.LENGTH_SHORT).show();
        demander.setOnClickListener(v -> {
            loadingDialog.startLoadingDialog();
            Handler handler = new Handler();
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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismissDialog();
                    FirebaseDatabase.getInstance().getReference().child("orders").child(orderId).child("status").setValue("refused");
                    Toast.makeText(context,"Aucune réponse du fournisseur de service",Toast.LENGTH_SHORT).show();
                }
            },90000);
            // get order statut
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Order order= dataSnapshot.getValue(Order.class);
                        String Oid=order.getId();
                        //Toast.makeText(context, "this is " + Oid, Toast.LENGTH_SHORT).show();
                        if (order != null && order.getId().equals(orderId) && order.getStatus().equals("accepted")){
                            loadingDialog.dismissDialog();
                            Intent intent = new Intent(getContext(),Contact.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("spId",order.getService_provider_id());
                            bundle.putString("firstName",serviceProvider.getFirstName());
                            bundle.putString("lastName",serviceProvider.getLastName());
                            bundle.putString("type",serviceProvider.getType());
                            bundle.putString("phoneNumber",serviceProvider.getPhoneNumber());
                            bundle.putString("description",serviceProvider.getDescription());
                            bundle.putString("orderId",orderId);
                            //String rate=(String) serviceProvider.getRating();
                            //bundle.putString("rating",rate);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            // add finish()
                        } else if (order != null && order.getId().equals(orderId) && order.getStatus().equals("refused")) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(context,"fournisseur de service a refusé votre demande",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        });


        // volley bib , opernRouteService API

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://api.openrouteservice.org/v2/matrix/driving-car";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("response", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray durations = jsonResponse.getJSONArray("durations");
                            //JSONArray distances = jsonResponse.getJSONArray("destinations");
                             // Iterate through the durations array
                            for (int i = 0; i < durations.length(); i++) {
                                JSONArray durationArray = durations.getJSONArray(i);
                                // Check if the second element of the inner array is 5697.92
                                if (durationArray.getDouble(1) != 0 ) {
                                    // Extract the index and duration value
                                    double duration = durationArray.getDouble(1);
                                    Log.d("Duration", String.valueOf(duration/60));
                                    //Log.d("destination", String.valueOf(distance));
                                    Toast.makeText(context, Double.toString(duration),Toast.LENGTH_SHORT).show();
                                    distnace.setText(String.format("%.2f min", duration / 60));
                                  //  distnace.setText(String.format( duration / 60 + "min"));
                                    break; // Exit the loop once the duration is found
                                }
                            }
                            // Your parsing logic goes here
//                            Log.d("responce", response);
//                            //Toast.makeText(context, response,Toast.LENGTH_SHORT).show();
//                            JSONObject jsonResponse = new JSONObject(response);
//                            JSONArray durations = jsonResponse.getJSONArray("durations");
//                            double duration = durations.getJSONArray(0).getDouble(1);  // replace with the correct indices
//                            Log.d("Duration", String.valueOf(duration));
//                            distnace.setText(String.format("%.2f minutes", duration / 60));
////                            JSONArray distances = jsonResponse.getJSONArray("durations");
////                            double distance = distances.getJSONArray(0).getDouble(1);  // replace with the correct indices
////                            Log.d("Distance", String.valueOf(distance));
////                            distnace.setText(String.format("%.2f km", distance / 1000));
//                        }
                       } catch (JSONException e) {
                            Log.e("Error", "Failed to parse JSON", e);
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "5b3ce3597851110001cf62481352b3a3d95d44c0a62d3bd4c9c2de8d");
                headers.put("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    // Your JSON payload here
                    JSONObject jsonBody = new JSONObject("{\"locations\":[[9.70093,48.477473],[9.207916,49.153868]]}");
                    final String requestBody = jsonBody.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    Log.e("Unsupported Encoding", "Could not encode: " + uee.getMessage());
                    return null;
                }
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);





        return layout;


    }

}
//    private void showProgressBar() {
//        // Create and show the progress bar
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View progressBarView = inflater.inflate(R.layout.progress_bar_layout, null);
//        ProgressBar progressBar = progressBarView.findViewById(R.id.progressBar);
//        ViewGroup rootView = (ViewGroup) ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
//        rootView.addView(progressBarView);
//    }

