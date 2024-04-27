package com.example.aman;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Handler handlerAnimation = new Handler();
    private boolean statusAnimation = false;
    private ImageView imgAnimation1, imgAnimation2;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        imgAnimation1 = findViewById(R.id.imgAnimation1);
        imgAnimation2 = findViewById(R.id.imgAnimation2);
        //button = findViewById(R.id.button);
        startPulse();
//        ImageView imageView = findViewById(R.id.iv1);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle click event programmatically
//                goToSpList(v);
//            }
//        });
        //first change


       //  adapter
        ListView listView =  findViewById(R.id.lv1);
        List<ServicesAvailable> items = new ArrayList<>();
        items.add(new ServicesAvailable(R.drawable.mecanicien_img, "Mecanicien"));
        items.add(new ServicesAvailable(R.drawable.electericien_img, "Electricien"));
        //items.add(new ServicesAvailable(R.drawable.depanneur_img, "Depanneur"));
        //items.add(new ServicesAvailable(R.drawable.hotel_img, "Hotel"));


        ServiceAvailableAdapter adapter = new ServiceAvailableAdapter(HomeActivity.this, R.layout.item_service_availabale, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ServicesAvailable servicesAvailable = (ServicesAvailable) parent.getItemAtPosition(position);
                String serviceType = servicesAvailable.getService_type();

                Intent intent = new Intent(HomeActivity.this, MecServiceProviderActivity.class);
                intent.putExtra("serviceType", serviceType);

                // to send item id
                //intent.putExtra("key", servicesAvailable.getData());
                startActivity(intent);
//                switch (position){
//                    case 0:
//                        startActivity( new Intent(HomeActivity.this, MecServiceProviderActivity.class));
//                        break;
//                    case 1:
//                        startActivity( new Intent(HomeActivity.this, RoutingActivity.class));
//                        break;
//                }

            }
        });
    }

    private void startPulse() {
        handlerAnimation.post(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imgAnimation1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            imgAnimation1.setScaleX(1f);
                            imgAnimation1.setScaleY(1f);
                            imgAnimation1.setAlpha(1f);
                        }
                    });

            imgAnimation2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            imgAnimation2.setScaleX(1f);
                            imgAnimation2.setScaleY(1f);
                            imgAnimation2.setAlpha(1f);
                        }
                    });

            handlerAnimation.postDelayed(this, 1500);
        }
    };


    public void goToReportEmaergency(View view) {
        Intent intent = new Intent(this, ReportEmergencyActivity.class);
        startActivity(intent);
    }


    public void goToSpList(View view) {
        Intent intent = new Intent(this, MecServiceProviderActivity.class);
        startActivity(intent);
    }
}