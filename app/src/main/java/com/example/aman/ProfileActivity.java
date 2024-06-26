package com.example.aman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        TextView textView = findViewById(R.id.information_personnel);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPersonalInformation(v);
            }
        });
    }
    public void goToPersonalInformation(View view) {
        Intent intent = new Intent(this, PersonalInformationActivity.class);
        startActivity(intent);
    }
}