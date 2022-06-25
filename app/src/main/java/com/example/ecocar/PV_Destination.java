package com.example.ecocar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.ecocar.direction_helpers.FetchURL;
import com.example.ecocar.direction_helpers.TaskLoadedCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class PV_Destination extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private boolean isPermissionGranter;
    private View mapView;
    private MarkerOptions placeOri, placeDes;
    private Polyline currentPolyline;
    private String address, distance, duration;
    private FloatingActionButton fab;

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranter = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pv_destination);

        checkPermission();

        if (isPermissionGranter) {

            String address_ori = getIntent().getExtras().getString("address");
            double latitude = getIntent().getExtras().getDouble("latitude");
            double longitude = getIntent().getExtras().getDouble("longitude");
            placeOri = new MarkerOptions().position(new LatLng(latitude, longitude)).title(address_ori).snippet("LUGAR DE ORIGEN");

            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.PVDes_map);
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();

            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), "AIzaSyBCONLHepCtD83QEQhsXqMPEZ0g86rsE4s", new Locale("es", "ES"));
            }

            setupAutoCompleteFragment();

            fab = findViewById(R.id.PV_Des_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (address != null) {
                        Intent intent = null;
                        if (getIntent().getExtras().getString("tripType").equals("occasional")) intent = new Intent(PV_Destination.this, DateTimePicker.class);
                        else if (getIntent().getExtras().getString("tripType").equals("habitual")) intent = new Intent(PV_Destination.this, HabitualDate.class);
                        intent.putExtra("address_ori", address_ori);
                        intent.putExtra("latitude_ori", latitude);
                        intent.putExtra("longitude_ori", longitude);
                        intent.putExtra("address_des", address);
                        intent.putExtra("latitude_des", placeDes.getPosition().latitude);
                        intent.putExtra("longitude_des", placeDes.getPosition().longitude);
                        intent.putExtra("distance", distance);
                        intent.putExtra("duration", duration);
                        intent.putExtra("tripType", getIntent().getExtras().getString("tripType"));
                        startActivity(intent);
                    }
                }
            });
        }
        else finish();
    }

    private void setupAutoCompleteFragment() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.PVDes_search);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (place != null) {
                    address = place.getAddress();
                    placeDes = new MarkerOptions().position(place.getLatLng()).title(address).snippet("LUGAR DE DESTINO");
                    mapFragment.getMapAsync(PV_Destination.this);
                    new FetchURL(PV_Destination.this).execute(getUrl(placeOri.getPosition(), placeDes.getPosition(), "driving"), "driving");
                }
            }

            @Override
            public void onError(@NonNull Status status) {
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        mMap.clear();
        // Add a marker in Sydney and move the camera
        mMap.addMarker(placeOri).showInfoWindow();
        if (placeDes != null) {
            mMap.addMarker(placeDes).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeDes.getPosition(), 12.5f));
        }
        else mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeOri.getPosition(), 12.5f));

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 0, 0);
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyAnGNZtKagAhnNOhudkcWJR-xdRkNJn6tU";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);

        ArrayList<String> aux1 = (ArrayList<String>) values[1];
        ArrayList<String> aux2 = (ArrayList<String>) values[2];
        distance = aux1.get(0) + " km";
        duration = aux2.get(0);
    }
}