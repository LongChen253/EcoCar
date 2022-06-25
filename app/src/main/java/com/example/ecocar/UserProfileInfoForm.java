package com.example.ecocar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class UserProfileInfoForm extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout userNameL, phoneL, dateTimeL, sexL, contactMailL, locationL;
    private EditText userName, phone, dateTime, contactMail, location;
    private AutoCompleteTextView sex;
    private Button createUser;
    private MaterialDatePicker<Long> materialDatePicker;
    private ActivityResultLauncher<Intent> SearchResultLauncherLocation;
    private ProgressBar prgCreateUser;

    private boolean validateForm () {
        if (userName.getText().toString().equals("")) {
            userNameL.setErrorEnabled(true);
            userNameL.setError("Obligatorio*");
            return false;
        }
        if (phone.getText().toString().trim().equals("")) {
            phoneL.setErrorEnabled(true);
            phoneL.setError("Obligatorio*");
            return false;
        }
        if (dateTime.getText().toString().equals("")) {
            dateTimeL.setErrorEnabled(true);
            dateTimeL.setError("Obligatorio*");
            return false;
        }
        if (sex.getText().toString().equals("")) {
            sexL.setErrorEnabled(true);
            sexL.setError("Obligatorio*");
            return false;
        }
        if (contactMail.getText().toString().trim().equals("")) {
            contactMailL.setErrorEnabled(true);
            contactMailL.setError("Obligatorio*");
            return false;
        }
        if (location.getText().toString().equals("")) {
            locationL.setErrorEnabled(true);
            locationL.setError("Obligatorio*");
            return false;
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_info_form);

        userNameL = findViewById(R.id.UPIF_usernameLayout);
        phoneL = findViewById(R.id.UPIF_phoneLayout);
        dateTimeL = findViewById(R.id.UPIF_birthdayLayout);
        sexL = findViewById(R.id.UPIF_sexLayout);
        contactMailL = findViewById(R.id.UPIF_emailLayout);
        locationL = findViewById(R.id.UPIF_locationLayout);

        userName = findViewById(R.id.UPIF_username);
        phone = findViewById(R.id.UPIF_phone);
        dateTime = findViewById(R.id.UPIF_birthday);
        sex = findViewById(R.id.UPIF_sex);
        contactMail = findViewById(R.id.UPIF_email);
        location = findViewById(R.id.UPIF_location);

        createUser = findViewById(R.id.UPIF_createUser);
        prgCreateUser = findViewById(R.id.UPIF_prgCreateUser);

        dateTime.setFocusable(false);
        sex.setFocusable(false);
        location.setFocusable(false);

        dateTime.setOnClickListener(this);
        sex.setOnClickListener(this);
        location.setOnClickListener(this);
        createUser.setOnClickListener(this);

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Fecha de nacimiento");
        materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            dateTime.setText(spf.format(calendar.getTime()));
        });

        String[] gender = new String[] {"hombre", "mujer", "no binario"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.single_item_counter, gender);
        sex.setAdapter(adapter);


        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyBCONLHepCtD83QEQhsXqMPEZ0g86rsE4s", new Locale("es", "ES"));
        }

        SearchResultLauncherLocation = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Intent data = result.getData();
                            Place place = Autocomplete.getPlaceFromIntent(data);
                            location.setText(place.getAddress());
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UPIF_birthday:
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                break;
            case R.id.UPIF_location:
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS))
                        .build(this);
                SearchResultLauncherLocation.launch(intent);
                break;
            case R.id.UPIF_createUser:
                locationL.setErrorEnabled(false);
                if (validateForm()) {
                    prgCreateUser.setVisibility(View.VISIBLE);

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String userUID = getIntent().getExtras().getString("userUID");

                    mAuth.signInWithEmailAndPassword("test@qq.com", "123456").addOnCompleteListener(task -> {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference UsersRef = mDatabase.child("users").child(userUID);

                        HashMap<String, Object> UserInfo = new HashMap<>();
                        UserInfo.put("username", userName.getText().toString());
                        UserInfo.put("gender", sex.getText().toString());
                        UserInfo.put("birthday", dateTime.getText().toString());
                        UserInfo.put("phone", Integer.parseInt(phone.getText().toString()));
                        UserInfo.put("address", location.getText().toString());
                        UserInfo.put("email", contactMail.getText().toString());
                        UserInfo.put("photoURL", "");
                        UserInfo.put("biography", "");
                        UserInfo.put("preferences", "");
                        UserInfo.put("carSelected", "");
                        UserInfo.put("star", 5.0);

                        UsersRef.updateChildren(UserInfo).addOnCompleteListener(task1 -> {
                            prgCreateUser.setVisibility(View.INVISIBLE);
                            Intent intent1 = new Intent(UserProfileInfoForm.this, Login.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent1);
                            Toast.makeText(UserProfileInfoForm.this, "Usuario creado con éxito!", Toast.LENGTH_LONG).show();
                        });
                    });
                }
                break;
            default:
                break;
        }
    }
}