package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateTimePicker extends AppCompatActivity {

    String date, time;

    private void setDateJourney () {
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(today);
        calendar.add(Calendar.DATE, 1);
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.from(calendar.getTimeInMillis()));

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("¿Cuándo vas a viajar?");
        builder.setSelection(calendar.getTimeInMillis());
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<Long> materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                setTimeJourney();
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                calendar.setTimeInMillis(selection);
                date = spf.format(calendar.getTime());
            }
        });

        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void setTimeJourney () {
        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        builder.setTimeFormat(TimeFormat.CLOCK_24H);
        builder.setTitleText("¿A qué hora recogerás a tus pasajeros?");
        MaterialTimePicker materialTimePicker = builder.build();

        materialTimePicker.addOnPositiveButtonClickListener(view -> {
            String hour = String.format("%02d", materialTimePicker.getHour());
            String minute = String.format("%02d", materialTimePicker.getMinute());
            time = hour + ":" + minute;

            Intent intent = new Intent(DateTimePicker.this, NumPassangers.class);
            intent.putExtra("address_ori", getIntent().getExtras().getString("address_ori"));
            intent.putExtra("latitude_ori", getIntent().getExtras().getDouble("latitude_ori"));
            intent.putExtra("longitude_ori", getIntent().getExtras().getDouble("longitude_ori"));
            intent.putExtra("address_des", getIntent().getExtras().getString("address_des"));
            intent.putExtra("latitude_des", getIntent().getExtras().getDouble("latitude_des"));
            intent.putExtra("longitude_des", getIntent().getExtras().getDouble("longitude_des"));
            intent.putExtra("distance", getIntent().getExtras().getString("distance"));
            intent.putExtra("duration", getIntent().getExtras().getString("duration"));
            intent.putExtra("date", date);
            intent.putExtra("time", time);

            startActivity(intent);
        });

        materialTimePicker.show(getSupportFragmentManager(), "TIME_PICKER");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);

        getSupportActionBar().setTitle("Fecha de tu viaje");
        setDateJourney();
    }
}