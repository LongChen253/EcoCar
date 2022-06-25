package com.example.ecocar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventsList extends AppCompatActivity {

    private RecyclerView eventsRecView;
    private TextView date, message;
    private ExtendedFloatingActionButton fab;
    private ProgressBar pb;
    private ActivityResultLauncher<Intent> EditEventLauncher;
    private ArrayList<Event> events;

    public ActivityResultLauncher<Intent> getEditEventLauncher() {
        return EditEventLauncher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        date = findViewById(R.id.EventListDate);
        eventsRecView = findViewById(R.id.EventListRV);
        message = findViewById(R.id.EventListMessage);
        fab = findViewById(R.id.EventListCreate);
        pb = findViewById(R.id.EventListPB);

        String events_date = getIntent().getExtras().getString("date");
        date.setText(events_date);

        events = new ArrayList<>();
        EventRecViewAdapter adapter = new EventRecViewAdapter(this);

        EditEventLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            ArrayList<Event> aux = result.getData().getExtras().getParcelableArrayList("events");
                            events = new ArrayList<>(aux);
                            adapter.setEvents(aux);
                        }
                    }
                });

        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(CurrentUser.getInstance().getUid()).child(events_date);
        eventRef.get().addOnCompleteListener(task -> {
            pb.setVisibility(View.INVISIBLE);

            for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                Event e = childSnapshot.getValue(Event.class);
                String [] time = childSnapshot.getKey().split(" ");
                e.setDate(events_date);
                e.setIniTime(time[0]);
                e.setEndTime(time[2]);
                events.add(e);
            }
            adapter.setEvents(events);
            eventsRecView.setAdapter(adapter);
            eventsRecView.setLayoutManager(new LinearLayoutManager(this));

            if (events.size() == 0) message.setVisibility(View.VISIBLE);

            fab.setOnClickListener(view -> {
                Intent intent = new Intent(this, EditEvent.class);
                intent.putExtra("option", "create");
                intent.putExtra("date", events_date);
                intent.putExtra("events", events);
                EditEventLauncher.launch(intent);
            });
        });
    }
}