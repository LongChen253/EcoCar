package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class HabitualDate extends AppCompatActivity implements View.OnClickListener{

    private EditText start, end;
    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton fab;
    MaterialDatePicker<Long> materialDatePickerIni, materialDatePickerEnd;
    private MultiSelectionRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitual_date);

        start = findViewById(R.id.Habitual_startDate);
        end = findViewById(R.id.Habitual_endDate);
        recyclerView = findViewById(R.id.Habitual_dates);
        fab = findViewById(R.id.Habitual_fab);

        start.setFocusable(false);
        end.setFocusable(false);

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Introduce el día");
        materialDatePickerIni = builder.build();
        materialDatePickerEnd = builder.build();

        materialDatePickerIni.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            start.setText(spf.format(calendar.getTime()));
        });

        materialDatePickerEnd.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            end.setText(spf.format(calendar.getTime()));
        });

        adapter = new MultiSelectionRecViewAdapter(this);
        ArrayList<MultiDate> multiDates = new ArrayList<>();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        start.setOnClickListener(this);
        end.setOnClickListener(this);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Habitual_startDate:
                materialDatePickerIni.show(getSupportFragmentManager(), "DATE_PICKER");
                break;

            case R.id.Habitual_endDate:
                materialDatePickerEnd.show(getSupportFragmentManager(), "DATE_PICKER");
                break;

            case R.id.Habitual_fab:
                adapter.addMultiDates(new MultiDate());
                break;

            default:
                break;
        }
    }
}