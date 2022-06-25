package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class EditEvent extends AppCompatActivity implements View.OnClickListener{

    private EditText name, description, iniTime, endTime;
    private TextInputLayout nameL, descriptionL, iniTimeL, endTimeL;
    private Button save;
    private ProgressBar pg;
    private ArrayList<Event> events;
    private String d;
    MaterialTimePicker materialTimePickerINI, materialTimePickerEND;

    private boolean check () {

        nameL.setErrorEnabled(false);
        descriptionL.setErrorEnabled(false);
        iniTimeL.setErrorEnabled(false);
        endTimeL.setErrorEnabled(false);

        if (name.getText().toString().equals("")) {
            nameL.setErrorEnabled(true);
            nameL.setError("¡Este campo es obligatorio!");
            return false;
        }
        if (description.getText().toString().equals("")) {
            descriptionL.setErrorEnabled(true);
            descriptionL.setError("¡Este campo es obligatorio!");
            return false;
        }
        if (iniTime.getText().toString().equals("")) {
            iniTimeL.setErrorEnabled(true);
            iniTimeL.setError("¡Este campo es obligatorio!");
            return false;
        }
        if (endTime.getText().toString().equals("")) {
            endTimeL.setErrorEnabled(true);
            endTimeL.setError("¡Este campo es obligatorio!");
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        name = findViewById(R.id.EditEventName);
        nameL = findViewById(R.id.EditEventNameL);
        description = findViewById(R.id.EditEventDescription);
        descriptionL = findViewById(R.id.EditEventDescriptionL);
        iniTime = findViewById(R.id.EditEventIniTime);
        iniTimeL = findViewById(R.id.EditEventIniTimeL);
        endTime = findViewById(R.id.EditEventEndTime);
        endTimeL = findViewById(R.id.EditEventEndTimeL);
        save = findViewById(R.id.EditEventSave);
        pg = findViewById(R.id.EditEventPB);

        iniTime.setFocusable(false);
        endTime.setFocusable(false);

        save.setOnClickListener(this);
        iniTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        builder.setTimeFormat(TimeFormat.CLOCK_24H);
        builder.setTitleText("Selecciona la hora");
        materialTimePickerINI = builder.build();
        materialTimePickerEND = builder.build();

        materialTimePickerINI.addOnPositiveButtonClickListener(view -> {
            String hour = String.format("%02d", materialTimePickerINI.getHour());
            String minute = String.format("%02d", materialTimePickerINI.getMinute());
            String time = hour + ":" + minute;
            iniTime.setText(time);
        });

        materialTimePickerEND.addOnPositiveButtonClickListener(view -> {
            String hour = String.format("%02d", materialTimePickerEND.getHour());
            String minute = String.format("%02d", materialTimePickerEND.getMinute());
            String time = hour + ":" + minute;
            endTime.setText(time);
        });

        events = getIntent().getExtras().getParcelableArrayList("events");

        if (getIntent().getExtras().getString("option").equals("edit")) {
            Event e = getIntent().getExtras().getParcelable("event");
            name.setText(e.getTaskName());
            description.setText(e.getTaskDescription());
            iniTime.setText(e.getIniTime());
            endTime.setText(e.getEndTime());
            d = e.getDate();
        }
        else if (getIntent().getExtras().getString("option").equals("create")) {
            d = getIntent().getExtras().getString("date");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.EditEventIniTime:
                materialTimePickerINI.show(getSupportFragmentManager(), "TIME_PICKER");
                break;

            case R.id.EditEventEndTime:
                materialTimePickerEND.show(getSupportFragmentManager(), "TIME_PICKER");
                break;

            case R.id.EditEventSave:
                if (check()) {
                    pg.setVisibility(View.VISIBLE);
                    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("events").child(CurrentUser.getInstance().getUid()).child(d);
                    String event_time = iniTime.getText().toString() + " - " + endTime.getText().toString();
                    DatabaseReference eventRef = eventsRef.child(event_time);

                    HashMap<String, Object> info = new HashMap<>();
                    info.put("taskName", name.getText().toString());
                    info.put("taskDescription", description.getText().toString());
                    info.put("finished", false);

                    Event e = new Event(d, iniTime.getText().toString(), endTime.getText().toString(), name.getText().toString(), description.getText().toString(), false);
                    eventRef.updateChildren(info).addOnCompleteListener(task -> {
                        pg.setVisibility(View.INVISIBLE);
                        events.add(e);
                        Intent intent = new Intent();
                        intent.putExtra("events", events);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    });
                }
                break;

            default:
                break;
        }
    }
}