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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText nameET,emailET,phone_numberET,passwordET,confirm_passwordET;
    Button signup_btn;
    boolean passwordVisible,confirmpasswordVisible;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    LoadingDialog dialog;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        nameET = findViewById(R.id.name);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        confirm_passwordET = findViewById(R.id.confirm_password);
        phone_numberET= findViewById(R.id.phonenumber);
        signup_btn = findViewById(R.id.signup_btn);
        //initialize disabled button
        signup_btn.setEnabled(false);
        //disable and enable login button
        nameET.addTextChangedListener(signuptextwatcher);
        emailET.addTextChangedListener(signuptextwatcher);
        passwordET.addTextChangedListener(signuptextwatcher);
        phone_numberET.addTextChangedListener(signuptextwatcher);
        confirm_passwordET.addTextChangedListener(signuptextwatcher);
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
        //signup button onclick listener
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new LoadingDialog(SignUpActivity.this,"veuillez patienter...",false);
                dialog.startLoadingDialog();
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String phone_number = phone_numberET.getText().toString();
                String password = passwordET.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Send verification email
                                    /*user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, "email sent to user", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });*/
                                    //dialog.setMessage("veuillez vérifier votre email");
                                    Map<String,Object> map =new HashMap<>();
                                    map.put("name",name);
                                    map.put("phone_number",phone_number);
                                    map.put("email",email);
                                    map.put("role","client");
                                    FirebaseDatabase.getInstance().getReference().child("users").push().setValue(map);
                                    dialog.dismissDialog();
                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                    Toast.makeText(SignUpActivity.this, "Authentication succeeded.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    TextWatcher signuptextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = nameET.getText().toString().trim();
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            String phone_number =phone_numberET.getText().toString().trim();
            String confirm_password = confirm_passwordET.getText().toString().trim();
            signup_btn.setEnabled(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.length()>6 && !confirm_password.isEmpty() && !phone_number.isEmpty() && phone_number.length()==10 && password.equals(confirm_password) && isValidEmail(email));
            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.length()>6 && !confirm_password.isEmpty() && !phone_number.isEmpty() && password.equals(confirm_password) && phone_number.length()==10 && isValidEmail(email)){
                //fields are not empty
                signup_btn.setTextColor(Color.parseColor("#ffffff"));
                signup_btn.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.redE));
            }else {
                //both fields or one of them is empty
                signup_btn.setTextColor(Color.parseColor("#99ffffff"));
                signup_btn.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.redE2));
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}