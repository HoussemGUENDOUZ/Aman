package com.example.aman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoadingDialog {
    private Context context;
    private AlertDialog dialog;
    private DatabaseReference ordersRef;

    LoadingDialog(Context context){
        this.context = context;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");

    }
    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        builder.setView(inflater.inflate(R.layout.custom_progress_bar,null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();


    }


    void dismissDialog(){
if (dialog!= null &&dialog.isShowing()){
    dialog.dismiss();
}


    }
}
