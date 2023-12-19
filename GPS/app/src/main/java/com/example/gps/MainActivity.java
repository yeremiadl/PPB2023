package com.example.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LocationManager mylocationManager;
    private LocationListener mylocationListener;
    private GoogleMap mMap;
    private TextView txtLat, txtLong, txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        mylocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mylocationListener = new LokasiListener();

        txtLat = findViewById(R.id.tvLat);
        txtLong = findViewById(R.id.tvLong);
        txtTime = findViewById(R.id.tvUpdate);

        Button start = findViewById(R.id.btnStart);
        start.setOnClickListener(op);

        Button stop = findViewById(R.id.btnStop);
        stop.setOnClickListener(op);
    }

    private class LokasiListener implements LocationListener {
        private boolean updatesAllowed = true;

        @Override
        public void onLocationChanged(@NonNull Location location) {
            updateCameraAndTextViews(location);

            // Place a new marker in the new location only if updates are allowed
            if (updatesAllowed) {
                placeMarker(location);

                updatesAllowed = false; // Prevent further updates
            }
        }
        public boolean areUpdatesAllowed() {
            return updatesAllowed;
        }
        public void allowUpdates() {
            updatesAllowed = true;
        }
        public void preventFurtherUpdates() {
            updatesAllowed = false;
        }

        private void updateCameraAndTextViews(Location location) {
            txtLat.setText("Lat: " + String.valueOf(location.getLatitude()));
            txtLong.setText("Long: " + String.valueOf(location.getLongitude()));

            Date currentTime = Calendar.getInstance().getTime();
            txtTime.setText("Last Update: " + String.valueOf(currentTime));

            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            Float zoom = 20F;

            // Update the camera
            LatLng newLocation = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, zoom));
        }

        private void placeMarker(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            // Place a new marker in the new location
            LatLng newLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(newLocation).title("New Marker"));
        }

    }



    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnStart) {
                updateGPS();
            } else if (view.getId() == R.id.btnStop) {
                stopGPS();
            }
        }
    };

    private void stopGPS() {
        mylocationManager.removeUpdates(mylocationListener);

        // Allow updates when the "Start Update" button is pressed again
        if (mylocationListener instanceof LokasiListener) {
            LokasiListener lokasiListener = (LokasiListener) mylocationListener;
            lokasiListener.allowUpdates();
        }

        // Get the last known location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = mylocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                // Place a new marker in the last known location
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();
                Float zoom = 20F;

                LatLng lastKnownLocation = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(lastKnownLocation).title("Last Known Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, zoom));
            } else {
                Toast.makeText(this, "Last known location not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mylocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mylocationListener);

    }

    private void gotoPeta(Double lat, Double lng, Float z) {
        LatLng LokasiBaru = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(LokasiBaru).title("Marker in " + lat + ": " + lng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LokasiBaru, z));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in ITS
        LatLng ITS = new LatLng(-7.2819705, 112.795323);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS, 15));
    }
}