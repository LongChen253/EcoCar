package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NumPassangers extends AppCompatActivity implements View.OnClickListener{

    private TextView numPassangers;
    private ImageButton addB, removeB;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_passangers);

        numPassangers = findViewById(R.id.numPassangers);
        addB = findViewById(R.id.addPas);
        removeB = findViewById(R.id.removePas);
        fab = findViewById(R.id.PV_NP_fab);

        addB.setOnClickListener(this);
        removeB.setOnClickListener(this);
        fab.setOnClickListener(this);

        removeB.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPas:
                int n = Integer.parseInt(numPassangers.getText().toString());
                ++n;
                numPassangers.setText(String.valueOf(n));
                if (n == 8) {
                    addB.setVisibility(View.INVISIBLE);
                    addB.setEnabled(false);
                }
                removeB.setVisibility(View.VISIBLE);
                removeB.setEnabled(true);
                break;

            case R.id.removePas:
                n = Integer.parseInt(numPassangers.getText().toString());
                --n;
                numPassangers.setText(String.valueOf(n));
                if (n == 1) {
                    removeB.setVisibility(View.INVISIBLE);
                    removeB.setEnabled(false);
                }
                addB.setVisibility(View.VISIBLE);
                addB.setEnabled(true);
                break;

            case R.id.PV_NP_fab:
                Intent intent = new Intent(NumPassangers.this, PV_Preferences.class);
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
                intent.putExtra("numPassangers", Integer.parseInt(numPassangers.getText().toString()));

                startActivity(intent);
                break;

            default:
                break;
        }
    }
}