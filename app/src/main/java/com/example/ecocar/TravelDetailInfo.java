package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TravelDetailInfo extends AppCompatActivity implements View.OnClickListener{

    private TextView date, oriAddress, desAddress, iniCity, endCity, iniTime, endTime, oriDistance,
            endDistance, price, driverName, driverStars, writeDriver, pet, luggage, smoke, faceMask, music, talk, numPassangers, travelCar;
    private CircleImageView profileImage;
    private RelativeLayout writeMessage, writeComment;
    private MaterialDivider commentDivider;
    private ConstraintLayout profileAccess;
    private Button reserve;
    private Travel travel;
    private CurrentUser driverOfTravel;
    DatabaseReference mDatabase, ReservedTripsRef, UsersRef, CarsRef, EventsRef;
    private RecyclerView passengersRV;
    private int reservedPassengers = 0;

    private void reserveTravel () {
        String userID = CurrentUser.getInstance().getUid();
        String tripID = travel.getTravelID();
        String key = userID + " " + tripID;

        ReservedTripsRef.orderByKey().equalTo(key).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) Toast.makeText(TravelDetailInfo.this, "¡Ya has reservado este viaje!", Toast.LENGTH_LONG).show();
            else if (reservedPassengers == travel.getNumPassangers()) Toast.makeText(TravelDetailInfo.this, "¡Este viaje ya está lleno de pasajeros!", Toast.LENGTH_LONG).show();
            else if (driverOfTravel.getUid().equals(userID)) Toast.makeText(TravelDetailInfo.this, "¡No puedes reservar tu viaje publicado!", Toast.LENGTH_LONG).show();
            else {
                String eventKey = travel.getIni_time() + " - " + travel.getEnd_time();
                String iniEvent = travel.getIni_date();
                iniEvent = iniEvent.substring(0, 2) + "-" + iniEvent.substring(3, 5) + "-" + iniEvent.substring(6);
                EventsRef = mDatabase.child("events").child(CurrentUser.getInstance().getUid()).child(iniEvent).child(eventKey);

                HashMap<String, Object> info = new HashMap<>();
                info.put("taskName", "Viaje reservado");
                String text = "De: " + travel.getAddress_ori() + "\n" + "A: " + travel.getAddress_des();
                info.put("taskDescription", text);
                info.put("finished", false);
                EventsRef.updateChildren(info);

                HashMap<String, Object> ReserveInfo = new HashMap<>();
                ReserveInfo.put("userID", userID);
                ReserveInfo.put("tripID", tripID);
                ReserveInfo.put("distanceORI", travel.getDistanceORI());
                ReserveInfo.put("distanceDES", travel.getDistanceDES());
                ReserveInfo.put("notified", false);
                ReservedTripsRef.child(key).updateChildren(ReserveInfo).addOnCompleteListener(task2 -> Toast.makeText(TravelDetailInfo.this, "¡VIAJE RESERVADO CORRECTAMENTE!", Toast.LENGTH_LONG).show());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail_info);
        setTitle("Información del viaje");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ReservedTripsRef = mDatabase.child("reserved_trips");
        UsersRef = mDatabase.child("users");
        CarsRef = mDatabase.child("cars");

        travel = getIntent().getExtras().getParcelable("travel");
        driverOfTravel = getIntent().getExtras().getParcelable("driver");

        date = findViewById(R.id.DetailDate);
        oriAddress = findViewById(R.id.DetailOriAddress);
        desAddress = findViewById(R.id.DetailDesAddress);
        iniCity = findViewById(R.id.DetailIniCity);
        endCity = findViewById(R.id.DetailEndCity);
        iniTime = findViewById(R.id.DetailIniTime);
        endTime = findViewById(R.id.DetailEndTime);
        oriDistance = findViewById(R.id.DetailDistanceOri);
        endDistance = findViewById(R.id.DetailDistanceDes);
        price = findViewById(R.id.DetailPrice);
        driverName = findViewById(R.id.DetailDriverName);
        driverStars = findViewById(R.id.DetailDriverlStars);
        writeDriver = findViewById(R.id.DetailWriteDriver);
        writeComment = findViewById(R.id.DetailWriteOpinion);
        commentDivider = findViewById(R.id.DetailDivider4);
        pet = findViewById(R.id.DetailPet);
        luggage = findViewById(R.id.DetailLuggage);
        smoke = findViewById(R.id.DetailSmoke);
        faceMask = findViewById(R.id.DetailFaceM);
        music = findViewById(R.id.DetailMusic);
        talk = findViewById(R.id.DetailTalk);
        numPassangers = findViewById(R.id.DetailNumPas);
        travelCar = findViewById(R.id.DetailTravelCar);
        profileImage = findViewById(R.id.DetailUserImage);
        writeMessage = findViewById(R.id.DetailWriteMessage);
        profileAccess = findViewById(R.id.DetailProfileAccess);
        reserve = findViewById(R.id.DetailReserveButton);
        passengersRV = findViewById(R.id.DetailListOfPassengers);

        writeMessage.setOnClickListener(this);
        writeComment.setOnClickListener(this);
        profileAccess.setOnClickListener(this);
        reserve.setOnClickListener(this);

        String aux;
        date.setText(travel.getIni_date());
        oriAddress.setText(travel.getAddress_ori());
        desAddress.setText(travel.getAddress_des());
        iniCity.setText(travel.getCity_ori());
        endCity.setText(travel.getCity_des());
        iniTime.setText(travel.getIni_time());
        endTime.setText(travel.getEnd_time());
        if (travel.getDistanceORI() == 0.0) oriDistance.setText("");
        else {
            aux = "A " + String.format("%.2f", travel.getDistanceORI()) + " km de tu punto de salida";
            oriDistance.setText(aux);
        }

        if (travel.getDistanceDES() == 0.0) endDistance.setText("");
        else {
            aux = "A " + String.format("%.2f", travel.getDistanceDES()) + " km de tu punto de llegada";
            endDistance.setText(aux);
        }

        aux = travel.getPrice() + ",00 €";
        price.setText(aux);

        driverName.setText(driverOfTravel.getUsername());
        profileImage.setImageURI(Uri.parse(driverOfTravel.getPhotoURL()));
        driverStars.setText(String.valueOf(driverOfTravel.getStar()));

        if (travel.isFinished() && !travel.getDriver().equals(CurrentUser.getInstance().getUid())) {
            writeComment.setVisibility(View.VISIBLE);
            commentDivider.setVisibility(View.VISIBLE);
        }

        aux = "Contactar con " + driverOfTravel.getUsername();
        writeDriver.setText(aux);

        if (travel.isPet()) pet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pet,0,R.drawable.ic_check, 0);
        else pet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pet,0,R.drawable.ic_clear, 0);

        if (travel.isLuggage()) luggage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_luggage,0,R.drawable.ic_check, 0);
        else luggage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_luggage,0,R.drawable.ic_clear, 0);

        if (travel.isSmoke()) smoke.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smoke,0,R.drawable.ic_check, 0);
        else smoke.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smoke,0,R.drawable.ic_clear, 0);

        if (travel.isFace_mask()) faceMask.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_face_mask,0,R.drawable.ic_check, 0);
        else faceMask.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_face_mask,0,R.drawable.ic_clear, 0);

        if (travel.isMusic()) music.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_music,0,R.drawable.ic_check, 0);
        else music.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_music,0,R.drawable.ic_clear, 0);

        if (travel.isTalk()) talk.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_talk,0,R.drawable.ic_check, 0);
        else talk.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_talk,0,R.drawable.ic_clear, 0);

        CarsRef.child(travel.getTravelCar()).get().addOnCompleteListener(task -> {
            DataSnapshot result = task.getResult();
            String text = result.getKey() + " " + result.child("brand").getValue(String.class) + " " + result.child("color").getValue(String.class);
            travelCar.setText(text);
        });

        PassengerRecViewAdapter passengerRecViewAdapter= new PassengerRecViewAdapter(TravelDetailInfo.this);
        passengersRV.setAdapter(passengerRecViewAdapter);
        passengersRV.setLayoutManager(new LinearLayoutManager(TravelDetailInfo.this));

        ReservedTripsRef.orderByChild("tripID").equalTo(travel.getTravelID()).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                    String userKEY = childSnapshot.child("userID").getValue(String.class);
                    UsersRef.child(userKEY).get().addOnCompleteListener(task1 -> {
                        CurrentUser passenger = task1.getResult().getValue(CurrentUser.class);
                        passenger.setUid(userKEY);
                        passengerRecViewAdapter.addTravel(passenger);
                    });
                    ++reservedPassengers;
                }
            }
            String p = reservedPassengers + "/" + travel.getNumPassangers();
            numPassangers.setText(p);
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.DetailWriteMessage:
                if (travel.getDriver().equals(CurrentUser.getInstance().getUid())) Toast.makeText(this, "¡No te puedes escribir mensajes a ti mismo!", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent(this, Chat.class);
                    intent.putExtra("receiverID", travel.getDriver());
                    intent.putExtra("driverName", driverOfTravel.getUsername());
                    startActivity(intent);
                }
                break;

            case R.id.DetailWriteOpinion:
                Intent intent = new Intent(this, WriteComment.class);
                intent.putExtra("user", driverOfTravel);
                intent.putExtra("travelID",  travel.getTravelID());
                startActivity(intent);
                break;

            case R.id.DetailProfileAccess:
                intent = new Intent(this, OtherUserProfile.class);
                intent.putExtra("otherUser", driverOfTravel);
                intent.putExtra("favourite", false);
                startActivity(intent);
                break;

            case R.id.DetailReserveButton:
                new AlertDialog.Builder(this)
                        .setMessage("¿Estás seguro de reservar este viaje?")
                        .setPositiveButton("SÍ", (dialogInterface, i) -> {
                            reserveTravel();
                            Intent in = new Intent(TravelDetailInfo.this, MainPage.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                        })
                        .setNegativeButton("NO", (dialogInterface, i) -> {})
                        .show();
                break;

            default:
                break;
        }
    }
}