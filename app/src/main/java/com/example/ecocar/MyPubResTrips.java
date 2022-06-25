package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyPubResTrips extends AppCompatActivity {

    private RecyclerView recyclerViewDoing, recyclerViewDone;
    private DatabaseReference pubRef, resRef;
    private TextView doingTitle, doneTitle;
    private ProgressBar pg;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pub_res_trips);

        userID = getIntent().getExtras().getString("userID");

        recyclerViewDoing = findViewById(R.id.PubReDoingRecycler);
        recyclerViewDone = findViewById(R.id.PubReDoneRecycler);
        doingTitle = findViewById(R.id.PubReDoingTitle);
        doneTitle = findViewById(R.id.PubReDoneTitle);
        pg = findViewById(R.id.PubRes_pg);

        pubRef = FirebaseDatabase.getInstance().getReference().child("published_trips");
        resRef = FirebaseDatabase.getInstance().getReference().child("reserved_trips");

        ArrayList<Travel> myTravelsDone = new ArrayList<>();
        ArrayList<Travel> myTravelsDoing = new ArrayList<>();
        String option = getIntent().getExtras().getString("option");
        TravelRecViewAdapter adapterDoing = new TravelRecViewAdapter(this);
        TravelRecViewAdapter adapterDone = new TravelRecViewAdapter(this);

        if (option.equals("publish")) {
            setTitle("Mis viajes publicados");
            pubRef.orderByChild("driver").equalTo(userID).get().addOnCompleteListener(task -> {
                for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                    Travel t = childSnapshot.getValue(Travel.class);
                    t.setTravelID(childSnapshot.getKey());
                    if (t.isFinished()) myTravelsDone.add(t);
                    else myTravelsDoing.add(t);
                }
                pg.setVisibility(View.INVISIBLE);
                adapterDoing.setTravels(myTravelsDoing);
                adapterDone.setTravels(myTravelsDone);
                recyclerViewDoing.setAdapter(adapterDoing);
                recyclerViewDone.setAdapter(adapterDone);
                recyclerViewDoing.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewDone.setLayoutManager(new LinearLayoutManager(this));

                if (!myTravelsDoing.isEmpty()) {
                    doingTitle.setVisibility(View.VISIBLE);
                    recyclerViewDoing.setVisibility(View.VISIBLE);
                }
                if (!myTravelsDone.isEmpty()) {
                    doneTitle.setVisibility(View.VISIBLE);
                    recyclerViewDone.setVisibility(View.VISIBLE);
                }
            });
        }
        else if (option.equals("reserve")) {
            setTitle("Mis viajes reservados");
            resRef.orderByChild("userID").equalTo(userID).get().addOnCompleteListener(task -> {
                recyclerViewDoing.setAdapter(adapterDoing);
                recyclerViewDone.setAdapter(adapterDone);
                recyclerViewDoing.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewDone.setLayoutManager(new LinearLayoutManager(this));

                for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                    String TripId = childSnapshot.child("tripID").getValue(String.class);
                    pubRef.child(TripId).get().addOnCompleteListener(task1 -> {
                        Travel t = task1.getResult().getValue(Travel.class);
                        t.setTravelID(TripId);
                        t.setDistanceORI(childSnapshot.child("distanceORI").getValue(Float.class));
                        t.setDistanceDES(childSnapshot.child("distanceDES").getValue(Float.class));

                        if (t.isFinished()) {
                            doneTitle.setVisibility(View.VISIBLE);
                            recyclerViewDone.setVisibility(View.VISIBLE);
                            adapterDone.addTravels(t);
                        }
                        else {
                            doingTitle.setVisibility(View.VISIBLE);
                            recyclerViewDoing.setVisibility(View.VISIBLE);
                            adapterDoing.addTravels(t);
                        }
                    });
                }
                pg.setVisibility(View.INVISIBLE);
            });
        }
    }
}