package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.IDNA;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DataForm extends AppCompatActivity implements View.OnClickListener{

    private EditText ET_origin, ET_destination, ET_distance, ET_duration, ET_date, ET_time, ET_numPas, ET_price;
    private Button publish, cancel;
    private Chip c1, c2, c3, c4, c5, c6;
    private String address_ori, address_des, distance, duration, ini_date, ini_time, end_date;
    private Double latitude_ori, latitude_des, longitude_ori, longitude_des;
    private int price, numPassangers;
    private boolean pet, luggage, smoke, face_mask, music, talk;

    private DatabaseReference mDatabase;
    private DatabaseReference PublishedTripsRef;
    private DatabaseReference TripsRef;
    private DatabaseReference EventsRef;

    private void insertData () {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        String[] duration_aux = duration.split(" ");
        String aux = ini_date + "-" + ini_time;
        try {
            calendar.setTime(spf.parse(aux));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(duration_aux[0]));
        calendar.add(Calendar.HOUR, Integer.parseInt(duration_aux[2]));
        calendar.add(Calendar.MINUTE, Integer.parseInt(duration_aux[4]));
        String res = spf.format(calendar.getTime());
        String[] result = res.split("-");

        int duration_second = Integer.parseInt(duration_aux[0]) * 86400 + Integer.parseInt(duration_aux[2]) * 3600 + Integer.parseInt(duration_aux[4]) * 60;

        Geocoder geo = new Geocoder(this, new Locale("es", "ES"));
        List<Address> add_ori = null;
        List<Address> add_des = null;
        try {
            add_ori = geo.getFromLocation(latitude_ori, longitude_ori, 1);
            add_des = geo.getFromLocation(latitude_des, longitude_des, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String city_ori = add_ori.get(0).getLocality();
        String city_des = add_des.get(0).getLocality();

        String key = ini_time + " - " + result[1];
        String iniEvent = ini_date.substring(0, 2) + "-" + ini_date.substring(3, 5) + "-" + ini_date.substring(6);
        EventsRef = mDatabase.child("events").child(CurrentUser.getInstance().getUid()).child(iniEvent).child(key);

        HashMap<String, Object> info = new HashMap<>();
        info.put("taskName", "Viaje publicado");
        String text = "De: " + address_ori + "\n" + "A: " + address_des;
        info.put("taskDescription", text);
        info.put("finished", false);
        EventsRef.updateChildren(info);

        HashMap<String, Object> TravelInfo = new HashMap<>();
        TravelInfo.put("driver", CurrentUser.getInstance().getUid());
        TravelInfo.put("travelCar", CurrentUser.getInstance().getCarSelected());
        TravelInfo.put("address_ori", address_ori);
        TravelInfo.put("city_ori", city_ori);
        TravelInfo.put("latitude_ori", latitude_ori);
        TravelInfo.put("longitude_ori", longitude_ori);
        TravelInfo.put("address_des", address_des);
        TravelInfo.put("city_des", city_des);
        TravelInfo.put("latitude_des", latitude_des);
        TravelInfo.put("longitude_des", longitude_des);
        TravelInfo.put("distance", Double.parseDouble(distance.substring(0, distance.indexOf(" "))));
        TravelInfo.put("duration", duration_second);
        TravelInfo.put("ini_date", ini_date);
        TravelInfo.put("ini_time", ini_time);
        TravelInfo.put("end_date", result[0]);
        TravelInfo.put("end_time", result[1]);
        TravelInfo.put("numPassangers", numPassangers);
        TravelInfo.put("price", price);
        TravelInfo.put("pet", pet);
        TravelInfo.put("luggage", luggage);
        TravelInfo.put("smoke", smoke);
        TravelInfo.put("face_mask", face_mask);
        TravelInfo.put("music", music);
        TravelInfo.put("talk", talk);
        TravelInfo.put("finished", false);

        TripsRef.updateChildren(TravelInfo).addOnCompleteListener(task -> Toast.makeText(DataForm.this, "¡VIAJE PUBLICADO CORRECTAMENTE!", Toast.LENGTH_LONG).show());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.DataF_Publish:
                mDatabase = FirebaseDatabase.getInstance().getReference();
                PublishedTripsRef = mDatabase.child("published_trips");
                TripsRef = PublishedTripsRef.push();

                insertData();

                Intent intent = new Intent(DataForm.this, MainPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.DataF_Cancel:
                intent = new Intent(DataForm.this, MainPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_form);

        ET_origin = findViewById(R.id.DataF_origin);
        ET_destination = findViewById(R.id.DataF_destination);
        ET_distance = findViewById(R.id.DataF_Distance);
        ET_duration = findViewById(R.id.DataF_Duration);
        ET_date = findViewById(R.id.DataF_Date);
        ET_time = findViewById(R.id.DataF_Time);
        ET_numPas = findViewById(R.id.DataF_NumPas);
        ET_price = findViewById(R.id.DataF_Price);
        publish = findViewById(R.id.DataF_Publish);
        cancel = findViewById(R.id.DataF_Cancel);
        c1 = findViewById(R.id.DataF_chip_1);
        c2 = findViewById(R.id.DataF_chip_2);
        c3 = findViewById(R.id.DataF_chip_3);
        c4 = findViewById(R.id.DataF_chip_4);
        c5 = findViewById(R.id.DataF_chip_5);
        c6 = findViewById(R.id.DataF_chip_6);

        ET_origin.setFocusable(false);
        ET_destination.setFocusable(false);
        ET_distance.setFocusable(false);
        ET_duration.setFocusable(false);
        ET_date.setFocusable(false);
        ET_time.setFocusable(false);
        ET_numPas.setFocusable(false);
        ET_price.setFocusable(false);
        publish.setOnClickListener(this);
        cancel.setOnClickListener(this);

        address_ori = getIntent().getExtras().getString("address_ori");
        latitude_ori = getIntent().getExtras().getDouble("latitude_ori");
        longitude_ori = getIntent().getExtras().getDouble("longitude_ori");
        address_des = getIntent().getExtras().getString("address_des");
        latitude_des = getIntent().getExtras().getDouble("latitude_des");
        longitude_des = getIntent().getExtras().getDouble("longitude_des");
        distance = getIntent().getExtras().getString("distance");
        duration = getIntent().getExtras().getString("duration");
        ini_date = getIntent().getExtras().getString("date");
        ini_time = getIntent().getExtras().getString("time");
        numPassangers = getIntent().getExtras().getInt("numPassangers");
        price = getIntent().getExtras().getInt("price");

        boolean[] aux = getIntent().getExtras().getBooleanArray("preferences");
        pet = aux[0];
        luggage = aux[1];
        smoke = aux[2];
        face_mask = aux[3];
        music = aux[4];
        talk = aux[5];

        ET_origin.setText(address_ori);
        ET_destination.setText(address_des);
        ET_distance.setText(distance);
        ET_duration.setText(duration);
        ET_date.setText(ini_date);
        ET_time.setText(ini_time);
        ET_numPas.setText(String.valueOf(numPassangers));
        String p = price + ",00 €";
        ET_price.setText(p);

        c1.setChecked(pet);
        c2.setChecked(luggage);
        c3.setChecked(smoke);
        c4.setChecked(face_mask);
        c5.setChecked(music);
        c6.setChecked(talk);
    }
}