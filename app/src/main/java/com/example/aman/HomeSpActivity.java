package com.example.aman;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.aman.databinding.ActivityHomeSpBinding;

public class HomeSpActivity extends AppCompatActivity {
    ActivityHomeSpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeSpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeSpFragment());
        binding.navigationbar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home){
                replaceFragment(new HomeSpFragment());
            } else if (itemId == R.id.orders) {
                replaceFragment(new OrdersFragment());
            } else if (itemId == R.id.store) {
                replaceFragment(new StoreFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentsFL,fragment);
        fragmentTransaction.commit();
    }
}