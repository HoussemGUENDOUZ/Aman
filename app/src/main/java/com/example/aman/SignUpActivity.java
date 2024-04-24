package com.example.aman;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText emailET,phone_numberET,passwordET,confirm_passwordET;
    Button signup_btn;
    boolean passwordVisible,confirmpasswordVisible;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(intent);
            //finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        confirm_passwordET = findViewById(R.id.confirm_password);
        phone_numberET= findViewById(R.id.phonenumber);
        signup_btn = findViewById(R.id.signup_btn);

        //show and hide password
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
        //show and hide confirm password
        confirm_passwordET.setOnTouchListener((v, event) -> {
            final int right = 2;
            if (event.getAction()== MotionEvent.ACTION_UP){
                if (event.getRawX()>=confirm_passwordET.getRight()-confirm_passwordET.getCompoundDrawables()[right].getBounds().width()){
                    int selection = confirm_passwordET.getSelectionEnd();
                    if (confirmpasswordVisible){
                        confirm_passwordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                        confirm_passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        confirmpasswordVisible = false;
                    }else {
                        confirm_passwordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                        confirm_passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        confirmpasswordVisible = true;
                    }
                    confirm_passwordET.setSelection(selection);
                    return true;
                }
            }
            return false;
        });


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String phone_number = phone_numberET.getText().toString();
                String password = passwordET.getText().toString();
                String confirm_password = confirm_passwordET.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication succeeded.",
                                            Toast.LENGTH_SHORT).show();


                                    Map<String,Object> map =new HashMap<>();
                                    map.put("phone_number",phone_number);
                                    map.put("email",email);
                                    FirebaseDatabase.getInstance().getReference().child("users").push().setValue(map);

                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                Map<String,Object> map =new HashMap<>();
                map.put("phone_number",phone_number);
                FirebaseDatabase.getInstance().getReference().child("users").push().setValue(map);
            }
        });


    }

}