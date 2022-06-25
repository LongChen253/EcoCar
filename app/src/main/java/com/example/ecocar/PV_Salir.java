package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

public class PV_Salir extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pv_salir);

        SearchView sv = findViewById(R.id.salir_searchView);
        sv.setFocusable(false);
        Intent intent = new Intent(PV_Salir.this, PV_Origin.class);
        intent.putExtra("tripType", getIntent().getExtras().getString("tripType"));
        sv.setOnClickListener(view -> startActivity(intent));
    }
}