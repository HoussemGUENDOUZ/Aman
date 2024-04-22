package com.example.aman;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText password;
    boolean passwordVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        password = findViewById(R.id.password);
        //touch listener to show and hide password
        password.setOnTouchListener((v, event) -> {
            final int right = 2;
            if (event.getAction()==MotionEvent.ACTION_UP){
                if (event.getRawX()>=password.getRight()-password.getCompoundDrawables()[right].getBounds().width()){
                    int selection = password.getSelectionEnd();
                    if (passwordVisible){
                        password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    }else {
                        password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    password.setSelection(selection);
                    return true;
                }
            }
            return false;
        });
    }
    public void connect(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}