package com.example.ecocar;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ecocar.databinding.ActivityMainPageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainPage extends AppCompatActivity {

    private ActivityMainPageBinding binding;
    private boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_reserve, R.id.navigation_publicate, R.id.navigation_agenda, R.id.navigation_favourite, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        DateFormat df = new SimpleDateFormat("dd/MM/yyyy-HH:mm");

        DatabaseReference pubRef = FirebaseDatabase.getInstance().getReference().child("published_trips");
        pubRef.orderByChild("driver").equalTo(CurrentUser.getInstance().getUid()).get().addOnCompleteListener(task -> {
            int n = 0;
            String end_date;
            for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                Travel t = childSnapshot.getValue(Travel.class);
                end_date = t.getEnd_date() + "-" + t.getEnd_time();
                try {
                    if (!t.isFinished() && Calendar.getInstance().getTime().after(df.parse(end_date))) {
                        ++n;
                        pubRef.child(childSnapshot.getKey()).child("finished").setValue(true);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (n > 0) {
                String message;
                if (n == 1) message = "¡Tienes " + n  + " viaje publicado finalizado!";
                else message = "¡Tienes " + n  + " viajes publicados finalizados!";

                new AlertDialog.Builder(this)
                        .setTitle("Notificación")
                        .setMessage(message)
                        .setIcon(R.drawable.ic_notificaction)
                        .setPositiveButton("De acuerdo", (dialogInterface, i) -> {})
                        .show();
            }
        });

        DatabaseReference reserveRef = FirebaseDatabase.getInstance().getReference().child("reserved_trips");
        reserveRef.orderByChild("userID").equalTo(CurrentUser.getInstance().getUid()).get().addOnCompleteListener(task -> {
            for (DataSnapshot chilSnapshot : task.getResult().getChildren()) {
                if (!chilSnapshot.child("notified").getValue(Boolean.class)) {
                    String key = chilSnapshot.child("tripID").getValue(String.class);
                    pubRef.child(key).get().addOnCompleteListener(task1 -> {
                        Travel t = task1.getResult().getValue(Travel.class);
                        String end_date = t.getEnd_date() + "-" + t.getEnd_time();
                        try {
                            if (Calendar.getInstance().getTime().before(df.parse(end_date))) {
                                pubRef.child(key).child("finished").setValue(true);
                                reserveRef.child(chilSnapshot.getKey()).child("notified").setValue(true);
                                if (!b) {
                                    new AlertDialog.Builder(this)
                                            .setTitle("Notificación")
                                            .setMessage("¡Se finalizan algunos de tus viajes reservados!")
                                            .setIcon(R.drawable.ic_notificaction)
                                            .setPositiveButton("De acuerdo", (dialogInterface, i) -> {})
                                            .show();
                                    b = true;
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                }
            }
        });
    }

}