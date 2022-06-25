package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TravelSearchResults extends AppCompatActivity {

    private RecyclerView travelsRecView;
    private TextView message;
    private String date, numPassangers;
    private ExtendedFloatingActionButton fab;
    private ProgressBar pb;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_search_results);
        setTitle("Viajes encontrados");

        travelsRecView = findViewById(R.id.TSR_trips);
        message = findViewById(R.id.TSR_message);
        fab = findViewById(R.id.TSR_fab);
        pb = findViewById(R.id.TSR_pb);

        fa = this;
        boolean filter_actived = getIntent().getExtras().getBoolean("filter_actived");

        if (filter_actived) {
            ArrayList<Travel> original_travels = getIntent().getExtras().getParcelableArrayList("original_travels");
            ArrayList<Travel> filtered_travels = getIntent().getExtras().getParcelableArrayList("filtered_travels");
            pb.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            TravelRecViewAdapter adapter = new TravelRecViewAdapter(this);
            adapter.setTravels(filtered_travels);
            travelsRecView.setAdapter(adapter);
            travelsRecView.setLayoutManager(new LinearLayoutManager(this));

            fab.setOnClickListener(view -> {
                Intent intent = new Intent(TravelSearchResults.this, TravelsFilter.class);
                intent.putExtra("travels", original_travels);
                startActivity(intent);
            });

            if (filtered_travels.size() == 0) message.setVisibility(View.VISIBLE);
        }
        else {
            double latitude_ori = getIntent().getExtras().getDouble("latitude_ori");
            double longitude_ori = getIntent().getExtras().getDouble("longitude_ori");
            double latitude_des = getIntent().getExtras().getDouble("latitude_des");
            double longitude_des = getIntent().getExtras().getDouble("longitude_des");
            date = getIntent().getExtras().getString("date");
            numPassangers = getIntent().getExtras().getString("numPassangers");

            ArrayList<Travel> simple_info_travels = new ArrayList<>();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference TripsRef = mDatabase.child("published_trips");
            Query query = TripsRef.orderByChild("ini_date").equalTo(date);

            query.get().addOnCompleteListener(task -> {
                for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                    Travel t = childSnapshot.getValue(Travel.class);
                    if (t.getNumPassangers() >= Integer.parseInt(numPassangers)) {
                        float res_ori_km, res_des_km;

                        float[] result = new float[1];
                        Location.distanceBetween(latitude_ori, longitude_ori, t.getLatitude_ori(), t.getLongitude_ori(), result);
                        res_ori_km = result[0] / 1000;
                        Location.distanceBetween(latitude_des, longitude_des, t.getLatitude_des(), t.getLongitude_des(), result);
                        res_des_km = result[0] / 1000;

                        if (res_ori_km <= 10 && res_des_km <= 10) {
                            t.setDistanceORI(res_ori_km);
                            t.setDistanceDES(res_des_km);
                            t.setTravelID(childSnapshot.getKey());
                            simple_info_travels.add(t);
                        }
                    }
                }
                pb.setVisibility(View.GONE);
                Collections.sort(simple_info_travels, (t1, t2) -> (int) (t1.getDistanceORI() - t2.getDistanceORI()));
                TravelRecViewAdapter adapter = new TravelRecViewAdapter(this);
                adapter.setTravels(simple_info_travels);
                travelsRecView.setAdapter(adapter);
                travelsRecView.setLayoutManager(new LinearLayoutManager(this));

                fab.setOnClickListener(view -> {
                    Intent intent = new Intent(TravelSearchResults.this, TravelsFilter.class);
                    intent.putExtra("travels", simple_info_travels);
                    startActivity(intent);
                });

                if (simple_info_travels.size() == 0) message.setVisibility(View.VISIBLE);
                else if (simple_info_travels.size() > 1) fab.setVisibility(View.VISIBLE);
            });
        }
    }
}