package com.example.aman;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class ReportEmergencyActivity extends AppCompatActivity {
    ImageView imageView , police , civilProtection , gendarmerie, fire , crash , earthquake;
    EditText description;
    Button addImage , signaler;
    boolean policeSelected, civileProtectionSelected , gendarmerieSelected , fireSelected , crashSelected , earthquakeSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_emergency);
        imageView = findViewById(R.id.profile_imgRE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile(v);
            }
        });

        // report emergency
        police = findViewById(R.id.policeIV);
        civilProtection = findViewById(R.id.protectionCivileIV);
        gendarmerie = findViewById(R.id.gendarmerieIV);
        fire = findViewById(R.id.fireIV);
        crash = findViewById(R.id.fireIV);
        earthquake = findViewById(R.id.earthquakeIV);
        description = findViewById(R.id.descriptionET);
        addImage = findViewById(R.id.addImg_btn);
        signaler = findViewById(R.id.signaler_btn);
        Map<String,Object> map =new HashMap<>();

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBorderAndDeselectOthers(police);
                policeSelected = true;



            }
        });

        civilProtection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
setBorderAndDeselectOthers(civilProtection);
                civileProtectionSelected= true;


            }
        });


        gendarmerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBorderAndDeselectOthers(gendarmerie);

                gendarmerieSelected = true;



            }
        });
    }

    private void setBorderAndDeselectOthers(ImageView selectedImageView) {
        // Reset borders and selection for all image views
        clearAllBordersAndSelection();

        // Set bold border to the selected image view
        int borderWidth = 10; // Adjust as needed
        int borderColor = Color.RED; // Adjust as needed
        setBoldBorderToImage(selectedImageView, borderWidth, borderColor);
    }
    private void clearAllBordersAndSelection() {
        clearBorderAndSelection(police);
        clearBorderAndSelection(civilProtection);
        clearBorderAndSelection(gendarmerie);

        // Reset selection flags
        policeSelected = false;
        civileProtectionSelected = false;
        gendarmerieSelected = false;
    }
    private void clearBorderAndSelection(ImageView imageView) {
        // Remove border
        imageView.setImageResource(0);
    }
    public static void setBoldBorderToImage(ImageView imageView, int borderWidth, int borderColor) {
        // Get the current image drawable
        Drawable imageDrawable = imageView.getDrawable();

        // Create a new ShapeDrawable for the border
        ShapeDrawable borderDrawable = new ShapeDrawable(new RectShape());
        borderDrawable.getPaint().setColor(borderColor);
        borderDrawable.getPaint().setStyle(Paint.Style.STROKE);
        borderDrawable.getPaint().setStrokeWidth(borderWidth);

        // Create a LayerDrawable to combine the image and border
        Drawable[] layers = {imageDrawable, borderDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        // Set the LayerDrawable as the image for the ImageView
        imageView.setImageDrawable(layerDrawable);
    }
    public void goToProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}