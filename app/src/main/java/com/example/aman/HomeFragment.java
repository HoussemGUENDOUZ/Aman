package com.example.aman;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private boolean statusAnimation = false;
    private Handler handlerAnimation = new Handler();
    private ImageView imgAnimation1, imgAnimation2;
    private Button sos_button;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imgAnimation1 = view.findViewById(R.id.imgAnimation1);
        imgAnimation2 = view.findViewById(R.id.imgAnimation2);
        sos_button = view.findViewById(R.id.sos_button);
        //sos button on click listener
        sos_button.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ReportEmergencyActivity.class);
            startActivity(intent);
        });
        startPulse();
        //  adapter
        RecyclerView recyclerView =  view.findViewById(R.id.lv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<ServicesAvailable> items = new ArrayList<>();
        items.add(new ServicesAvailable(R.drawable.mecanicien_img, "Mecanicien"));
        items.add(new ServicesAvailable(R.drawable.electericien_img, "Electricien"));
        //items.add(new ServicesAvailable(R.drawable.depanneur_img, "Depanneur"));
        //items.add(new ServicesAvailable(R.drawable.hotel_img, "Hotel"));
        ServiceAvailableAdapter adapter = new ServiceAvailableAdapter(getContext(), R.layout.item_service_availabale, items, service -> {
            Intent intent = new Intent(getContext(), MecServiceProviderActivity.class);
            intent.putExtra("serviceType", service.getService_type());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        /*recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ServicesAvailable servicesAvailable = (ServicesAvailable) parent.getItemAtPosition(position);
                String serviceType = servicesAvailable.getService_type();
                Intent intent = new Intent(getContext(), MecServiceProviderActivity.class);
                intent.putExtra("serviceType", serviceType);
                // to send item id
                //intent.putExtra("key", servicesAvailable.getData());
                startActivity(intent);
//                switch (position){
//                    case 0:
//                        startActivity( new Intent(HomeActivity.this, MecServiceProviderActivity.class));
//                        break;
//                    case 1:
//                        startActivity( new Intent(HomeActivity.this, RoutingActivity.class));
//                        break;
//                }
            }
        });*/

// Location service
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
//                    1);
//        } else {
//            startLocationService();
//        }


RecyclerView recyclerView1 = view.findViewById(R.id.adsRV);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
       ArrayList<String> dataSource;
       AdsAdapter adsAdapter;

       dataSource= new ArrayList<>();
        dataSource.add("first ads");
        dataSource.add("second ads");
        dataSource.add("third ads");



        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        adsAdapter= new AdsAdapter(dataSource);
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView1.setAdapter(adsAdapter);

        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            }
        }
    }

    private void startLocationService() {
        Intent serviceIntent = new Intent(requireContext(), LocationService.class);
        ContextCompat.startForegroundService(requireContext(), serviceIntent);
    }

    private void startPulse() {
        handlerAnimation.post(runnable);
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imgAnimation1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                    .withEndAction(() -> {
                        imgAnimation1.setScaleX(1f);
                        imgAnimation1.setScaleY(1f);
                        imgAnimation1.setAlpha(1f);
                    });
            imgAnimation2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700)
                    .withEndAction(() -> {
                        imgAnimation2.setScaleX(1f);
                        imgAnimation2.setScaleY(1f);
                        imgAnimation2.setAlpha(1f);
                    });
            handlerAnimation.postDelayed(this, 1500);
        }
    };
    public void goToSpList(View view) {
        Intent intent = new Intent(getContext(), MecServiceProviderActivity.class);
        startActivity(intent);
    }
}