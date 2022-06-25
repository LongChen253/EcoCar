package com.example.ecocar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CircleImageView profileImage;
    private TextView username, phone, email, birthday, address, star, biography, preferences;
    private RatingBar ratingBar;
    private ActivityResultLauncher<Intent> SearchResultLauncherProfileImage, SearchResultLauncherLocation, SearchResultLauncherCars;
    private TextInputLayout phoneL, mailL, birthL, locationL, biographyL, preferencesL;
    private EditText phoneET, mailET, birthET, locationET, biographyET, preferencesET;
    private ImageButton editInfo, editBio, editPref, addCar;
    private Button saveInfo, saveBio, savePref;
    private Boolean saveInfoB, saveBioB, savePrefB;
    private AutoCompleteTextView mycar;
    private MaterialDatePicker<Long> materialDatePicker;
    private DatabaseReference userProfileRef, userCarsRef;
    private ArrayList<String> plates;
    private RelativeLayout publishR, reserveR, commentsR, otherCommentsR;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveInfoB = false;
        saveBioB = false;
        savePrefB = false;
        plates = new ArrayList<>();

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users");
        userCarsRef = FirebaseDatabase.getInstance().getReference().child("userCars");

        profileImage = view.findViewById(R.id.Profile_image);
        username = view.findViewById(R.id.Profile_username);
        star = view.findViewById(R.id.Profile_star);
        ratingBar = view.findViewById(R.id.Profile_rating);
        phone = view.findViewById(R.id.Profile_phone);
        email = view.findViewById(R.id.Profile_contactEmail);
        birthday = view.findViewById(R.id.Profile_birthday);
        address = view.findViewById(R.id.Profile_address);
        biography = view.findViewById(R.id.Profile_biography);
        preferences = view.findViewById(R.id.Profile_preferences);
        phoneL = view.findViewById(R.id.Profile_editPhoneLayout);
        mailL = view.findViewById(R.id.Profile_editMailLayout);
        birthL = view.findViewById(R.id.Profile_editBirthdayLayout);
        locationL = view.findViewById(R.id.Profile_editDirectionLayout);
        biographyL = view.findViewById(R.id.Profile_editBiographyLayout);
        preferencesL = view.findViewById(R.id.Profile_editPrefLayout);
        phoneET = view.findViewById(R.id.Profile_editPhone);
        mailET = view.findViewById(R.id.Profile_editMail);
        birthET = view.findViewById(R.id.Profile_editBirthday);
        locationET = view.findViewById(R.id.Profile_editDirection);
        biographyET = view.findViewById(R.id.Profile_editBiography);
        preferencesET = view.findViewById(R.id.Profile_editPref);
        mycar = view.findViewById(R.id.Profile_cars);
        editInfo = view.findViewById(R.id.Profile_editInfo);
        editBio = view.findViewById(R.id.Profile_editBio);
        editPref = view.findViewById(R.id.Profile_editPre);
        addCar = view.findViewById(R.id.Profile_addCar);
        saveInfo = view.findViewById(R.id.Profile_save_info);
        saveBio = view.findViewById(R.id.Profile_save_biography);
        savePref = view.findViewById(R.id.Profile_save_pref);
        publishR = view.findViewById(R.id.Profile_myPublishedTravels);
        reserveR = view.findViewById(R.id.Profile_myReservedTrips);
        commentsR = view.findViewById(R.id.Profile_opinions);
        otherCommentsR = view.findViewById(R.id.Profile_myOpinions);

        if (!CurrentUser.getInstance().getPhotoURL().equals("")) profileImage.setImageURI(Uri.parse(CurrentUser.getInstance().getPhotoURL()));
        username.setText(CurrentUser.getInstance().getUsername());
        if (CurrentUser.getInstance().getGender().equals("hombre")) username.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_male, 0);
        else if (CurrentUser.getInstance().getGender().equals("mujer")) username.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_female, 0);
        star.setText(String.valueOf(CurrentUser.getInstance().getStar()));
        ratingBar.setRating(CurrentUser.getInstance().getStar());
        phone.setText(String.valueOf(CurrentUser.getInstance().getPhone()));
        email.setText(CurrentUser.getInstance().getEmail());
        birthday.setText(CurrentUser.getInstance().getBirthday());
        address.setText(CurrentUser.getInstance().getAddress());
        if (!CurrentUser.getInstance().getBiography().equals("")) biography.setText(CurrentUser.getInstance().getBiography());
        if (!CurrentUser.getInstance().getPreferences().equals("")) preferences.setText(CurrentUser.getInstance().getPreferences());

        birthET.setFocusable(false);
        locationET.setFocusable(false);
        mycar.setFocusable(false);

        profileImage.setOnClickListener(this);
        birthET.setOnClickListener(this);
        locationET.setOnClickListener(this);
        editInfo.setOnClickListener(this);
        editBio.setOnClickListener(this);
        editPref.setOnClickListener(this);
        addCar.setOnClickListener(this);
        saveInfo.setOnClickListener(this);
        saveBio.setOnClickListener(this);
        savePref.setOnClickListener(this);
        publishR.setOnClickListener(this);
        reserveR.setOnClickListener(this);
        commentsR.setOnClickListener(this);
        otherCommentsR.setOnClickListener(this);

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Fecha de nacimiento");
        materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            birthET.setText(spf.format(calendar.getTime()));
            birthday.setText(spf.format(calendar.getTime()));
        });


        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyBCONLHepCtD83QEQhsXqMPEZ0g86rsE4s", new Locale("es", "ES"));
        }

        SearchResultLauncherLocation = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Place place = Autocomplete.getPlaceFromIntent(data);
                        address.setText(place.getAddress());
                        locationET.setText(place.getAddress());
                    }
                });

        SearchResultLauncherProfileImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        profileImage.setImageURI(Uri.parse(CurrentUser.getInstance().getPhotoURL()));
                    }
                });

        SearchResultLauncherCars = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mycar.setFocusable(true);
                        String aux = result.getData().getExtras().getString("plate");
                        mycar.setText(aux);
                        CurrentUser.getInstance().setCarSelected(aux);
                        plates.add(aux);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.single_item_counter, plates);
                        mycar.setAdapter(adapter);
                    }
                });

        userCarsRef.orderByChild("userID").equalTo(CurrentUser.getInstance().getUid()).get().addOnCompleteListener(task -> {
            for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                plates.add(childSnapshot.child("plate").getValue(String.class));
            }
            if (plates.size() == 0) mycar.setText("¡Registra tu primer coche!");
            else {
                mycar.setText(CurrentUser.getInstance().getCarSelected());
                mycar.setFocusable(true);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.single_item_counter, plates);
                mycar.setAdapter(adapter);
                mycar.setOnItemClickListener((adapterView, view1, i1, l) -> {
                    CurrentUser.getInstance().setCarSelected(mycar.getText().toString());
                    userProfileRef.child(CurrentUser.getInstance().getUid()).child("carSelected").setValue(mycar.getText().toString());
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Profile_image:
                SearchResultLauncherProfileImage.launch(new Intent(getActivity(), EditProfileImage.class));
                break;

            case R.id.Profile_editBirthday:
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
                break;

            case R.id.Profile_editDirection:
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS))
                        .build(getContext());
                SearchResultLauncherLocation.launch(intent);
                break;

            case R.id.Profile_editInfo:
                if (!saveInfoB) {
                    phoneET.setText(String.valueOf(CurrentUser.getInstance().getPhone()));
                    mailET.setText(CurrentUser.getInstance().getEmail());
                    birthET.setText(CurrentUser.getInstance().getBirthday());
                    locationET.setText(CurrentUser.getInstance().getAddress());
                    startInfoEdit();
                }
                else closeInfoEdit();
                saveInfoB = !saveInfoB;
                break;

            case R.id.Profile_editBio:
                if (!saveBioB) {
                    if (!CurrentUser.getInstance().getBiography().equals("")) biographyET.setText(CurrentUser.getInstance().getBiography());
                    startBioEdit();
                }
                else closeBioEdit();
                saveBioB = !saveBioB;
                break;

            case R.id.Profile_editPre:
                if (!savePrefB) {
                    if (!CurrentUser.getInstance().getPreferences().equals("")) preferencesET.setText(CurrentUser.getInstance().getPreferences());
                    startPrefEdit();
                }
                else closePrefEdit();
                savePrefB = !savePrefB;
                break;

            case R.id.Profile_save_info:
                CurrentUser.getInstance().setPhone(Integer.parseInt(phoneET.getText().toString()));
                CurrentUser.getInstance().setEmail(mailET.getText().toString());
                CurrentUser.getInstance().setBirthday(birthET.getText().toString());
                CurrentUser.getInstance().setAddress(locationET.getText().toString());
                phone.setText(phoneET.getText().toString());
                email.setText(mailET.getText().toString());
                birthday.setText(birthET.getText().toString());
                address.setText(locationET.getText().toString());

                HashMap<String, Object> i = new HashMap<>();
                i.put("phone", Integer.parseInt(phoneET.getText().toString()));
                i.put("email", mailET.getText().toString());
                i.put("birthday", birthET.getText().toString());
                i.put("address", locationET.getText().toString());

                userProfileRef.child(CurrentUser.getInstance().getUid()).updateChildren(i).addOnCompleteListener(task -> {
                    closeInfoEdit();
                });
                break;

            case R.id.Profile_save_biography:
                CurrentUser.getInstance().setBiography(biographyET.getText().toString());
                biography.setText(biographyET.getText().toString());

                userProfileRef.child(CurrentUser.getInstance().getUid()).child("biography").setValue(biographyET.getText().toString()).addOnCompleteListener(task -> {
                    closeBioEdit();
                });
                break;

            case R.id.Profile_save_pref:
                CurrentUser.getInstance().setPreferences(preferencesET.getText().toString());
                preferences.setText(preferencesET.getText().toString());

                userProfileRef.child(CurrentUser.getInstance().getUid()).child("preferences").setValue(preferencesET.getText().toString()).addOnCompleteListener(task -> closePrefEdit());
                break;

            case R.id.Profile_addCar:
                SearchResultLauncherCars.launch(new Intent(getActivity(), AddCarForm.class));
                break;

            case R.id.Profile_myPublishedTravels:
                intent = new Intent(getActivity(), MyPubResTrips.class);
                intent.putExtra("option", "publish");
                intent.putExtra("userID",  CurrentUser.getInstance().getUid());
                startActivity(intent);
                break;

            case R.id.Profile_myReservedTrips:
                intent = new Intent(getActivity(), MyPubResTrips.class);
                intent.putExtra("option", "reserve");
                intent.putExtra("userID",  CurrentUser.getInstance().getUid());
                startActivity(intent);
                break;

            case R.id.Profile_opinions:
                intent = new Intent(getActivity(), ConsultComment.class);
                intent.putExtra("option", "mine");
                intent.putExtra("userID", CurrentUser.getInstance().getUid());
                startActivity(intent);
                break;

            case R.id.Profile_myOpinions:
                intent = new Intent(getActivity(), ConsultComment.class);
                intent.putExtra("option", "other");
                intent.putExtra("userID", CurrentUser.getInstance().getUid());
                startActivity(intent);
            default:
                break;
        }
    }

    private void startInfoEdit () {
        phone.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        birthday.setVisibility(View.GONE);
        address.setVisibility(View.GONE);
        phoneL.setVisibility(View.VISIBLE);
        mailL.setVisibility(View.VISIBLE);
        birthL.setVisibility(View.VISIBLE);
        locationL.setVisibility(View.VISIBLE);
        saveInfo.setVisibility(View.VISIBLE);
    }

    private void closeInfoEdit () {
        phone.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        birthday.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
        phoneL.setVisibility(View.GONE);
        mailL.setVisibility(View.GONE);
        birthL.setVisibility(View.GONE);
        locationL.setVisibility(View.GONE);
        saveInfo.setVisibility(View.GONE);
    }

    private void startBioEdit () {
        biography.setVisibility(View.GONE);
        biographyL.setVisibility(View.VISIBLE);
        saveBio.setVisibility(View.VISIBLE);
    }

    private void closeBioEdit () {
        biography.setVisibility(View.VISIBLE);
        biographyL.setVisibility(View.GONE);
        saveBio.setVisibility(View.GONE);
    }

    private void startPrefEdit () {
        preferences.setVisibility(View.GONE);
        preferencesL.setVisibility(View.VISIBLE);
        savePref.setVisibility(View.VISIBLE);
    }

    private void closePrefEdit () {
        preferences.setVisibility(View.VISIBLE);
        preferencesL.setVisibility(View.GONE);
        savePref.setVisibility(View.GONE);
    }
}