package com.example.aman;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class HomeSpFragment extends Fragment {
    private boolean statusAnimation = false;
    private Handler handlerAnimation = new Handler();
    private ImageView imgAnimation1, imgAnimation2;
    private Button sos_button;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button viewOrder;
    private String mParam1;
    private String mParam2;

    public HomeSpFragment() {
        // Required empty public constructor
    }

    public static HomeSpFragment newInstance(String param1, String param2) {
        HomeSpFragment fragment = new HomeSpFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_sp, container, false);
        imgAnimation1 = view.findViewById(R.id.imgAnimation1);
        imgAnimation2 = view.findViewById(R.id.imgAnimation2);
        sos_button = view.findViewById(R.id.sos_button);
        //sos button on click listener
        sos_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReportEmergencyActivity.class);
                startActivity(intent);
            }
        });
        startPulse();
        viewOrder = (Button) view.findViewById(R.id.viewOrder);
        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrdersFragment ordersFragment = new OrdersFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentsFL, ordersFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    private void startPulse() {
        handlerAnimation.post(runnable);
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imgAnimation1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            imgAnimation1.setScaleX(1f);
                            imgAnimation1.setScaleY(1f);
                            imgAnimation1.setAlpha(1f);
                        }
                    });
            imgAnimation2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            imgAnimation2.setScaleX(1f);
                            imgAnimation2.setScaleY(1f);
                            imgAnimation2.setAlpha(1f);
                        }
                    });
            handlerAnimation.postDelayed(this, 1500);
        }
    };
}