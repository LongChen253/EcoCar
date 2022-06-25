package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

public class TravelsFilter extends AppCompatActivity {

    ArrayList<Travel> original_travels;
    ArrayList<Travel> filtered_travels;
    private RadioGroup rg1;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6;
    private Button buscar;

    private boolean AnyRestrictionsChecked () {
        return cb1.isChecked() || cb2.isChecked() || cb3.isChecked() || cb4.isChecked() || cb5.isChecked() || cb6.isChecked();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_filter);
        setTitle("Filtro de viaje");

        buscar = findViewById(R.id.Filter_buscar);
        rg1 = findViewById(R.id.Filter_RG1);
        cb1 = findViewById(R.id.Filter_CB1);
        cb2 = findViewById(R.id.Filter_CB2);
        cb3 = findViewById(R.id.Filter_CB3);
        cb4 = findViewById(R.id.Filter_CB4);
        cb5 = findViewById(R.id.Filter_CB5);
        cb6 = findViewById(R.id.Filter_CB6);

        original_travels = getIntent().getExtras().getParcelableArrayList("travels");
        filtered_travels = new ArrayList<>(original_travels);

        buscar.setOnClickListener(view -> {
            boolean b = AnyRestrictionsChecked();
            if (rg1.getCheckedRadioButtonId() != -1 || b) {
                Log.i("INFO: ", "INFO1: " + filtered_travels.size() + " " + filtered_travels);
                if (b) {
                    Iterator<Travel> iterator = filtered_travels.iterator();
                    while (iterator.hasNext()){
                        Travel t = iterator.next();
                        if (cb1.isChecked() && !t.isPet()) iterator.remove();
                        else if (cb2.isChecked() && !t.isLuggage()) iterator.remove();
                        else if (cb3.isChecked() && !t.isSmoke()) iterator.remove();
                        else if (cb4.isChecked() && !t.isFace_mask()) iterator.remove();
                        else if (cb5.isChecked() && !t.isMusic()) iterator.remove();
                        else if (cb6.isChecked() && !t.isTalk()) iterator.remove();
                    }
                }
                Log.i("INFO: ", "INFO2: " + filtered_travels.size() + " " + filtered_travels);

                RadioButton rb = findViewById(rg1.getCheckedRadioButtonId());
                if (rb != null) {
                    switch (rb.getId()) {
                        case R.id.Filter_RB1:
                            Collections.sort(filtered_travels, (t1, t2) -> t1.getPrice() - t2.getPrice());
                            break;
                        case R.id.Filter_RB2:
                            Collections.sort(filtered_travels, (t1, t2) -> {
                                SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
                                int time1 = 0, time2 = 0;
                                try {
                                    Date ini_date_t1 = spf.parse(t1.getIni_time());
                                    Date ini_date_t2 = spf.parse(t2.getIni_time());
                                    time1 = (int) ini_date_t1.getTime();
                                    time2 = (int) ini_date_t2.getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return time1 - time2;
                            });
                            break;
                        case R.id.Filter_RB3:
                            Collections.sort(filtered_travels, (t1, t2) -> (int) (t1.getDistanceORI() - t2.getDistanceORI()));
                            break;
                        case R.id.Filter_RB4:
                            Collections.sort(filtered_travels, (t1, t2) -> (int) (t1.getDistanceDES() - t2.getDistanceDES()));
                            break;
                        case R.id.Filter_RB5:
                            Collections.sort(filtered_travels, (t1, t2) -> t1.getDuration() - t2.getDuration());
                            break;
                        default:
                            break;
                    }
                }
                else Collections.sort(filtered_travels, (t1, t2) -> (int) (t1.getDistanceORI() - t2.getDistanceORI()));

                Intent intent = new Intent(this, TravelSearchResults.class);
                intent.putExtra("filter_actived", true);
                intent.putExtra("original_travels", original_travels);
                intent.putExtra("filtered_travels", filtered_travels);
                TravelSearchResults.fa.finish();
                startActivity(intent);
            }
            finish();
        });
    }
}