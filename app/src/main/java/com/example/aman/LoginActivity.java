package com.example.aman;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText passwordET, emailET;
    Button loginbtn;
    boolean passwordVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        loginbtn = findViewById(R.id.login_btn);
        //initialize disabled button
        loginbtn.setEnabled(false);
        //disable and enable login button
        emailET.addTextChangedListener(logintextwatcher);
        passwordET.addTextChangedListener(logintextwatcher);
        //touch listener to show and hide password
        passwordET.setOnTouchListener((v, event) -> {
            final int right = 2;
            if (event.getAction()==MotionEvent.ACTION_UP){
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
    TextWatcher logintextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            loginbtn.setEnabled(!email.isEmpty() && !password.isEmpty());
            if (!email.isEmpty() && !password.isEmpty()){
                //fields are not empty
                loginbtn.setTextColor(Color.parseColor("#ffffff"));
                loginbtn.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.bleu));
            }else {
                //both fields or one of them is empty
                loginbtn.setTextColor(Color.parseColor("#99ffffff"));
                loginbtn.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.bleu2));
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public void connect(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}