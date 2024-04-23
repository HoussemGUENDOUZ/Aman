package com.example.aman;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {
    EditText emailET,phone_numberET,passwordET,confirm_passwordET;
    Button signup_btn;
    boolean passwordVisible,confirmpasswordVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        confirm_passwordET = findViewById(R.id.confirm_password);
        passwordET.setOnTouchListener((v, event) -> {
            final int right = 2;
            if (event.getAction()== MotionEvent.ACTION_UP){
                if (event.getRawX()>=passwordET.getRight()-passwordET.getCompoundDrawables()[right].getBounds().width()){
                    int selection = passwordET.getSelectionEnd();
                    if (passwordVisible){
                        passwordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                        passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    }else {
                        passwordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                        passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    passwordET.setSelection(selection);
                    return true;
                }
            }
            return false;
        });
    }

}