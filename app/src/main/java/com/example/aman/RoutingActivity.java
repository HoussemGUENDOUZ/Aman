package com.example.aman;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.reader.header.MapFileException;

import java.io.File;

public class RoutingActivity extends AppCompatActivity {
    MapView mapView ;
    TileCache tileCache;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_routing);
        AndroidGraphicFactory.createInstance(this.getApplication());


        checkFilePermission();

        mapView= (MapView) findViewById(R.id.mapView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mapView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }

        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setZoomLevelMin((byte) 10);
        mapView.setZoomLevelMax((byte) 20);

        tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(),1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor()
        );






// Inside your activity's onCreate() or any method where you need the permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // You should show an explanation here if needed, then request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        } else {
            // Permission has already been granted
            // Proceed with file access
        }
    }
    public void checkFilePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 90);
        } else { // permission autorisée déjà... }

        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 80 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getBestLocationProvider();
        } else {
            Log.e("Routing", "Location permission denied");
        }
    }
//     @Override
//     public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
//
//        if (requestCode == REQUEST_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//        } else {
//            Log.e("Routing", "Location permission denied");
//        }
//    }



    TileRendererLayer tileRendererLayer;
    @Override
    protected void onStart() {
        super.onStart();

        try {
            // Attempt to read the map file
            // MapFile mapFile1 = new MapFile("/storage/emulated/0/maps/");
            // Proceed with map file operations
            File file = new File("/storage/emulated/0/maps/planet_5.74,35.923_7.482,36.849-mapsforge-osm/planet_5.74,35.923_7.482,36.849.map");
            MapDataStore mapDataStore = new MapFile(file);
            tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition,
                    AndroidGraphicFactory.INSTANCE);
            mapView.getLayerManager().getLayers().add(tileRendererLayer);

            mapView.setCenter(new LatLong(36.245138, 6.570929));
            mapView.setZoomLevel((byte) 19);

        } catch (MapFileException e) {
            // Handle the exception more gracefully
            e.printStackTrace();
            // For example, show an error dialog to the user
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error")
                    .setMessage("Failed to read map file: " + e.getMessage())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // You can handle the click event here
                        }
                    })
                    .show();
        }
    }






    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Context context;
    public RoutingActivity(){

    }
    public RoutingActivity(Context context) {
        this.context = context;
    }


    public void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 80);
        } else { // permission autorisée déjà... }
            getBestLocationProvider();
        }
    }




    // creteria fournisseur de position
    public void getBestLocationProvider() {
        LocationManager lm =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        Criteria cr = new Criteria();
        cr.setAccuracy(Criteria.ACCURACY_FINE);
        cr.setAltitudeRequired(true);
        cr.setBearingRequired(true);
        cr.setCostAllowed(false);

        cr.setPowerRequirement(Criteria.POWER_HIGH);
        cr.setSpeedRequired(true);

        String provider = lm.getBestProvider(cr, false);
        if (provider != null) {
            try {
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extra) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                int minTime = 5000;
                float minDistance = 5;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                lm.requestLocationUpdates(provider, minTime, minDistance, locationListener);

                Location loc = lm.getLastKnownLocation(provider);
                if(loc != null){
                    // call last known location !!!!!1
                    Double alt = loc.getAltitude();
                    Double log = loc.getLongitude();
                    Double lat = loc.getLatitude();
                    Toast.makeText(RoutingActivity.this, "ur position " + alt + log + lat , Toast.LENGTH_SHORT).show();
                }

            }catch (SecurityException e){
                e.printStackTrace();
                Log.e("Routing" , "SecurityExeption" + e.getMessage());
            }

        } else {
            Log.i("Routing ", "No location provider available");
        }
        if (provider != null) {
            //  if provider not available ,get last position
            //getLastKnownLocation check
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location loc = lm.getLastKnownLocation(provider);
            Log.i("GeoFragment", "Le provider " + provider + " a été sélectionné");
            if (loc != null) {
                Log.i("GeoFragment", "position trouvée");
                double longitude = loc.getLongitude();
                double latitude = loc.getLatitude();
                double altitude = loc.getAltitude();
                Toast.makeText(RoutingActivity.this, "ur place" + longitude + latitude + altitude, Toast.LENGTH_SHORT).show();
            } else {
                Log.i("GeoFragment", "aucune position connue");
            }
        } else
            Log.i("GeoFragment", "aucune fournisseur de localisation disponible");
    }

    public void getPosition(){

    }
}