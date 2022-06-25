package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.ecocar.R;

public class PV_IR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pv_ir);

        String address = getIntent().getExtras().getString("address");
        Double latitude = getIntent().getExtras().getDouble("latitude");
        Double longitude = getIntent().getExtras().getDouble("longitude");

        SearchView sv = findViewById(R.id.ir_searchView);
        sv.setFocusable(false);
        Intent intent = new Intent(PV_IR.this, PV_Destination.class);
        intent.putExtra("address", address);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("tripType", getIntent().getExtras().getString("tripType"));
        sv.setOnClickListener(view -> startActivity(intent));
    }
}