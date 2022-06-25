package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PV_Preferences extends AppCompatActivity {

    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, rb11, rb12;
    private FloatingActionButton fab;

    private boolean checkPreference() {
        return (rb1.isChecked() || rb2.isChecked()) && (rb3.isChecked() || rb4.isChecked()) && (rb5.isChecked()
                || rb6.isChecked()) && (rb7.isChecked() || rb8.isChecked()) && (rb9.isChecked()
                || rb10.isChecked()) && (rb11.isChecked() || rb12.isChecked());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pv_preferences);
        getSupportActionBar().setTitle("Elige tus preferencias");

        rb1 = findViewById(R.id.PV_Pref_RB1);
        rb2 = findViewById(R.id.PV_Pref_RB2);
        rb3 = findViewById(R.id.PV_Pref_RB3);
        rb4 = findViewById(R.id.PV_Pref_RB4);
        rb5 = findViewById(R.id.PV_Pref_RB5);
        rb6 = findViewById(R.id.PV_Pref_RB6);
        rb7 = findViewById(R.id.PV_Pref_RB7);
        rb8 = findViewById(R.id.PV_Pref_RB8);
        rb9 = findViewById(R.id.PV_Pref_RB9);
        rb10 = findViewById(R.id.PV_Pref_RB10);
        rb11 = findViewById(R.id.PV_Pref_RB11);
        rb12 = findViewById(R.id.PV_Pref_RB12);
        fab = findViewById(R.id.PV_Pref_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPreference()) {
                    boolean [] preferences = {false, false, false, false, false, false};
                    preferences[0] = rb1.isChecked();
                    preferences[1] = rb3.isChecked();
                    preferences[2] = rb5.isChecked();
                    preferences[3] = rb7.isChecked();
                    preferences[4] = rb9.isChecked();
                    preferences[5] = rb11.isChecked();

                    Intent intent = new Intent(PV_Preferences.this, PV_Price.class);
                    intent.putExtra("address_ori", getIntent().getExtras().getString("address_ori"));
                    intent.putExtra("latitude_ori", getIntent().getExtras().getDouble("latitude_ori"));
                    intent.putExtra("longitude_ori", getIntent().getExtras().getDouble("longitude_ori"));
                    intent.putExtra("address_des", getIntent().getExtras().getString("address_des"));
                    intent.putExtra("latitude_des", getIntent().getExtras().getDouble("latitude_des"));
                    intent.putExtra("longitude_des", getIntent().getExtras().getDouble("longitude_des"));
                    intent.putExtra("distance", getIntent().getExtras().getString("distance"));
                    intent.putExtra("duration", getIntent().getExtras().getString("duration"));
                    intent.putExtra("date", getIntent().getExtras().getString("date"));
                    intent.putExtra("time", getIntent().getExtras().getString("time"));
                    intent.putExtra("numPassangers", getIntent().getExtras().getInt("numPassangers"));
                    intent.putExtra("preferences", preferences);


                    startActivity(intent);
                }
                else Toast.makeText(PV_Preferences.this, "Todos los campos deben estar seleccionados", Toast.LENGTH_LONG).show();
            }
        });
    }
}