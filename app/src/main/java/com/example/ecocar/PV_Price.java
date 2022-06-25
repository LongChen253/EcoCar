package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PV_Price extends AppCompatActivity implements View.OnClickListener{

    private TextView price;
    private ImageButton addP, removeP;
    private FloatingActionButton fab;
    private int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pv_price);

        price = findViewById(R.id.price);
        addP = findViewById(R.id.PV_price_add);
        removeP = findViewById(R.id.PV_price_sub);
        fab = findViewById(R.id.PV_Price_fab);

        addP.setOnClickListener(this);
        removeP.setOnClickListener(this);
        fab.setOnClickListener(this);

        removeP.setEnabled(false);

        String aux = price.getText().toString();
        n = Integer.parseInt(aux.substring(0, aux.indexOf("€")));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.PV_price_add:
                ++n;
                String res = n + "€";
                price.setText(res);
                if(n == 15) {
                    addP.setVisibility(View.INVISIBLE);
                    addP.setEnabled(false);
                }
                removeP.setVisibility(View.VISIBLE);
                removeP.setEnabled(true);
                break;

            case R.id.PV_price_sub:
                --n;
                res = n + "€";
                price.setText(res);
                if (n == 3) {
                    removeP.setVisibility(View.INVISIBLE);
                    removeP.setEnabled(false);
                }
                addP.setVisibility(View.VISIBLE);
                addP.setEnabled(true);
                break;

            case R.id.PV_Price_fab:
                Intent intent = new Intent(PV_Price.this, DataForm.class);
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
                intent.putExtra("preferences", getIntent().getExtras().getBooleanArray("preferences"));
                intent.putExtra("price", n);

                startActivity(intent);
                break;

            default:
                break;
        }
    }
}