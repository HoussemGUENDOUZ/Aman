package com.example.aman;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth mAuth;
    FirebaseUser  user;
    String currentUserKey;
    ListView recent_ordersLV;
    List<Order> recentOrders;
    OrderAdapter adapter;
    DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
    private String mParam1;
    private String mParam2;

    public OrdersFragment() {
        // Required empty public constructor
    }

    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users");
        recent_ordersLV = view.findViewById(R.id.recentordersLV);
        recentOrders = new ArrayList<>();

        //
        adapter = new OrderAdapter(requireContext(),R.layout.item_order ,recentOrders);
        recent_ordersLV.setAdapter(adapter);
        assert user != null;
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
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recentOrders = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    assert order != null;
                    if (Objects.equals(order.service_provider_id, currentUserKey) && Objects.equals(order.status, "pending")){
                        recentOrders.add(order);
                    }
                }
                adapter = new OrderAdapter(requireContext(),R.layout.item_order ,recentOrders);
                recent_ordersLV.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });












        /*ordersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order newOrder = snapshot.getValue(Order.class);
                if (newOrder == null){
                    Log.d(TAG,"newOrder == null");
                }else {
                    Log.d(TAG,"newOrder not null");
                }

                if (newOrder.getService_provider_id().equals(null)){
                    Log.d(TAG,"getService_provider_id() ==null");
                }else {
                    Log.d(TAG,"sp_id not null");
                }

                if (newOrder.getStatus().equals(null)){
                    Log.d(TAG,"newOrder.getStatus()  ==null");
                }else {
                    Log.d(TAG,"newOrder status not null");
                }
                if (newOrder.getService_provider_id().equals(currentUserKey) && newOrder.getStatus().equals("pending")){
                    recentOrders.add(newOrder);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order newOrder = snapshot.getValue(Order.class);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });*/
            return view;
    }
//    //changed maked here 30/04| 00:58  from this
//    @Override
//    public  void onActivityCreated(Bundle savedInstanceState) {
//
//        super.onActivityCreated(savedInstanceState);
//
//        adapter = new OrderAdapter(requireContext(),R.layout.item_order,recentOrders);
//        recent_ordersLV.setAdapter(adapter);
//
//        ordersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                recentOrders.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Order order = dataSnapshot.getValue(Order.class);
//                    if (order != null && Objects.equals(order.getService_provider_id(), currentUserKey) && Objects.equals(order.getStatus(), "pending")) {
//                        recentOrders.add(order);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        //to this
    }
