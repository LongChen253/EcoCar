package com.example.ecocar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;

import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reserve#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reserve extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView numPassangers;
    private EditText ET_DE, ET_A, DateTime;
    private Button search;
    private Intent intent;
    private MaterialDatePicker<Long> materialDatePicker;
    private ActivityResultLauncher<Intent> SearchResultLauncherDE;
    private ActivityResultLauncher<Intent> SearchResultLauncherA;
    private double latitude_ori, longitude_ori, latitude_des, longitude_des;

    public Reserve() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reserve.
     */
    // TODO: Rename and change types and number of parameters
    public static Reserve newInstance(String param1, String param2) {
        Reserve fragment = new Reserve();
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

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyBCONLHepCtD83QEQhsXqMPEZ0g86rsE4s", new Locale("es", "ES"));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserve, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ET_DE = view.findViewById(R.id.RVOri_search);
        ET_A = view.findViewById(R.id.RVDes_search);
        DateTime = view.findViewById(R.id.RV_Date);
        search = view.findViewById(R.id.RV_Buscar);
        numPassangers = view.findViewById(R.id.num_passanger);

        ET_DE.setFocusable(false);
        ET_A.setFocusable(false);
        DateTime.setFocusable(false);

        ET_DE.setOnClickListener(this);
        ET_A.setOnClickListener(this);
        DateTime.setOnClickListener(this);
        search.setOnClickListener(this);

        SearchResultLauncherDE = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Intent data = result.getData();
                            Place place = Autocomplete.getPlaceFromIntent(data);
                            ET_DE.setText(place.getAddress());
                            LatLng l = place.getLatLng();
                            latitude_ori = l.latitude;
                            longitude_ori = l.longitude;
                        }
                    }
                });

        SearchResultLauncherA = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {

                                Intent data = result.getData();
                                Place place = Autocomplete.getPlaceFromIntent(data);
                                ET_A.setText(place.getAddress());
                                LatLng l = place.getLatLng();
                                latitude_des = l.latitude;
                                longitude_des = l.longitude;
                            }
                        }
                });

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(today);
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(calendar.getTimeInMillis());
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("¿Cuándo vas a viajar?");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintsBuilder.build());
        materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
            calendar.setTimeInMillis(selection);
            DateTime.setText(spf.format(calendar.getTime()));
        });

        String[] num_pass = new String[] {"1","2","3","4","5","6","7","8"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.single_item_counter, num_pass);
        numPassangers.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RVOri_search:
                intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS))
                        .build(getContext());
                SearchResultLauncherDE.launch(intent);
                break;

            case R.id.RVDes_search:
                intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS))
                        .build(getContext());
                SearchResultLauncherA.launch(intent);
                break;

            case R.id.RV_Date:
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
                break;

            case R.id.RV_Buscar:
                if (!ET_DE.getText().toString().equals("") && !ET_A.getText().toString().equals("") && !DateTime.getText().toString().equals("") && !numPassangers.getText().toString().equals("")) {
                    Intent intent = new Intent(getActivity(), TravelSearchResults.class);
                    intent.putExtra("latitude_ori", latitude_ori);
                    intent.putExtra("longitude_ori", longitude_ori);
                    intent.putExtra("latitude_des", latitude_des);
                    intent.putExtra("longitude_des", longitude_des);
                    intent.putExtra("date", DateTime.getText().toString());
                    intent.putExtra("numPassangers", numPassangers.getText().toString());
                    intent.putExtra("filter_actived", false);
                    startActivity(intent);
                }
                else Toast.makeText(getActivity(), "¡Debe llenar todos los campos!", Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }
    }
}