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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText passwordET, emailET;
    Button loginbtn;
    boolean passwordVisible;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    FirebaseUser currentUser;

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
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        loginbtn = findViewById(R.id.login_btn);
        //initialize disabled button
        loginbtn.setEnabled(false);
        //login button onclick listener
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    database = FirebaseDatabase.getInstance().getReference().child("users");
                                    database.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot datasnapshot : snapshot.getChildren()){
                                                User user = datasnapshot.getValue(User.class);
                                                if (user.getEmail().equals(email)){
                                                    if (user.getRole().equals("client")){
                                                        Toast.makeText(LoginActivity.this, "Authentication succeeded.",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }else {
                                                        Toast.makeText(LoginActivity.this, "Authentication succeeded.",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), RoutingActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
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
            loginbtn.setEnabled(!email.isEmpty() && !password.isEmpty() && password.length()>6);
            if (!email.isEmpty() && !password.isEmpty() && password.length()>6){
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
}