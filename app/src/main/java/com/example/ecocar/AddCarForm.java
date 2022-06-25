package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddCarForm extends AppCompatActivity {

    private TextInputLayout plateL, brandL, modelL, colorL;
    private EditText plate, brand, model, color;
    private Button save;

    private boolean check () {
        plateL.setErrorEnabled(false);
        brandL.setErrorEnabled(false);
        modelL.setErrorEnabled(false);
        colorL.setErrorEnabled(false);

        if (plate.getText().toString().equals("")) {
            plateL.setErrorEnabled(true);
            plateL.setError("¡Este campo es obligatorio!");
            return false;
        }

        if (brand.getText().toString().equals("")) {
            brandL.setErrorEnabled(true);
            brandL.setError("¡Este campo es obligatorio!");
            return false;
        }

        if (model.getText().toString().equals("")) {
            modelL.setErrorEnabled(true);
            modelL.setError("¡Este campo es obligatorio!");
            return false;
        }

        if (color.getText().toString().equals("")) {
            colorL.setErrorEnabled(true);
            colorL.setError("¡Este campo es obligatorio!");
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_form);
        setTitle("Tu vehículo");

        plateL = findViewById(R.id.CarFormPlateLayout);
        brandL = findViewById(R.id.CardFormBrandLayout);
        modelL = findViewById(R.id.CardFormModelLayout);
        colorL = findViewById(R.id.CardFormColorLayout);
        plate = findViewById(R.id.CardFormPlate);
        brand = findViewById(R.id.CardFormBrand);
        model = findViewById(R.id.CardFormModel);
        color = findViewById(R.id.CardFormColor);
        save = findViewById(R.id.CardFormSave);

        save.setOnClickListener(view -> {
            String s = plate.getText().toString();

            if (check()) {
                DatabaseReference CarsRef = FirebaseDatabase.getInstance().getReference().child("cars").child(s);
                CarsRef.get().addOnCompleteListener(task -> {
                    if (!task.getResult().exists()) {
                        HashMap<String, Object> carInfo = new HashMap<>();
                        carInfo.put("brand", brand.getText().toString());
                        carInfo.put("model", model.getText().toString());
                        carInfo.put("color", color.getText().toString());
                        CarsRef.updateChildren(carInfo);
                    }
                });

                String key = s + " " + CurrentUser.getInstance().getUid();
                DatabaseReference UserCarsRef = FirebaseDatabase.getInstance().getReference().child("userCars").child(key);
                UserCarsRef.get().addOnCompleteListener(task -> {
                    if (task.getResult().exists()) Toast.makeText(this, "¡Ya has registrado este coche!", Toast.LENGTH_SHORT).show();
                    else {
                        HashMap<String, Object> ucInfo = new HashMap<>();
                        ucInfo.put("plate", s);
                        ucInfo.put("userID", CurrentUser.getInstance().getUid());
                        UserCarsRef.updateChildren(ucInfo).addOnCompleteListener(task1 -> {
                            Intent intent = new Intent();
                            intent.putExtra("plate", s);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        });
                    }
                });
            }
        });
    }
}