package com.example.ecocar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class MultiSelectionRecViewAdapter extends RecyclerView.Adapter<MultiSelectionRecViewAdapter.ViewHolder> {

    public ArrayList<MultiDate> multiDates = new ArrayList<>();
    private Context ctx;

    public MultiSelectionRecViewAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habitual, parent, false);
        MultiSelectionRecViewAdapter.ViewHolder holder = new MultiSelectionRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] days = new String[] {"Todos los lunes","Todos los martes","Todos los miércoles","Todos los jueves","Todos los viernes","Todos los sábados","Todos los domingos","Día concreto"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, R.layout.single_item_counter, days);
        holder.type.setAdapter(adapter);

        holder.type.setOnItemClickListener((adapterView, view, i, l) -> {
            String value = holder.type.getText().toString();
            if (value.equals("Día concreto")) {
                MaterialDatePicker<Long> materialDatePicker;
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Introduce el día");
                materialDatePicker = builder.build();

                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    holder.type.setText(spf.format(calendar.getTime()));
                    multiDates.get(position).setType(spf.format(calendar.getTime()));
                });

                materialDatePicker.show(((FragmentActivity) ctx).getSupportFragmentManager(), "DATE_PICKER");
            }
            else multiDates.get(position).setType(value);
        });

        holder.iniTime.setFocusable(false);
        holder.iniTime.setOnClickListener(view -> {
            MaterialTimePicker materialTimePicker;
            MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
            builder.setTimeFormat(TimeFormat.CLOCK_24H);
            builder.setTitleText("Selecciona la hora");
            materialTimePicker = builder.build();

            materialTimePicker.addOnPositiveButtonClickListener(view1 -> {
                String hour = String.format("%02d", materialTimePicker.getHour());
                String minute = String.format("%02d", materialTimePicker.getMinute());
                String time = hour + ":" + minute;
                holder.iniTime.setText(time);
                multiDates.get(position).setIniTime(time);
            });

            materialTimePicker.show(((FragmentActivity) ctx).getSupportFragmentManager(), "DATE_PICKER");
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void addMultiDates(MultiDate multiDate) {
        this.multiDates.add(multiDate);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return multiDates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private AutoCompleteTextView type;
        private EditText iniTime;
        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.Hab_day);
            iniTime = itemView.findViewById(R.id.Hab_time);
        }
    }
}
