package com.example.ecocar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventRecViewAdapter extends RecyclerView.Adapter<EventRecViewAdapter.ViewHolder> {

    public ArrayList<Event> events = new ArrayList<>();
    private EventsList el;

    public EventRecViewAdapter(EventsList el) {
        this.el = el;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false);
        EventRecViewAdapter.ViewHolder holder = new EventRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String date = events.get(position).getIniTime() + " - " + events.get(position).getEndTime();
        holder.eventTime.setText(date);
        holder.eventName.setText(events.get(position).getTaskName());
        holder.eventDescription.setText(events.get(position).getTaskDescription());

        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(CurrentUser.getInstance().getUid()).child(events.get(position).getDate()).child(date);

        if (!events.get(position).getFinished()) {
            holder.eventDelete.setOnClickListener(view -> {
                new AlertDialog.Builder(el)
                        .setMessage("¿Estás seguro de eliminar la tarea?")
                        .setPositiveButton("SÍ", (dialogInterface, i) -> {
                            eventRef.removeValue();
                            events.remove(events.get(position));
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("NO", (dialogInterface, i) -> {})
                        .show();
            });

            holder.eventFinish.setOnClickListener(view -> {
                new AlertDialog.Builder(el)
                        .setMessage("¿Estás seguro de finalizar la tarea?")
                        .setPositiveButton("SÍ", (dialogInterface, i) -> {
                            eventRef.child("finished").setValue(true);
                            events.get(position).setFinished(true);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("NO", (dialogInterface, i) -> {})
                        .show();
            });
            holder.parent.setOnClickListener(view -> {
                Intent intent = new Intent(el, EditEvent.class);
                ArrayList<Event> aux = new ArrayList<>(events);
                aux.remove(position);
                intent.putExtra("option", "edit");
                intent.putExtra("event", events.get(position));
                intent.putExtra("events", aux);
                el.getEditEventLauncher().launch(intent);
            });
        }
        else {
            holder.eventDelete.setVisibility(View.GONE);
            holder.eventFinish.setVisibility(View.GONE);
            holder.eventMessage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView eventTime, eventName, eventDescription, eventMessage;
        private ImageButton eventFinish, eventDelete;
        private CardView parent;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            eventTime = itemView.findViewById(R.id.EventTime);
            eventName = itemView.findViewById(R.id.EventName);
            eventDescription = itemView.findViewById(R.id.EventDescription);
            eventMessage = itemView.findViewById(R.id.EventFinishMessage);
            eventFinish = itemView.findViewById(R.id.EventFinish);
            eventDelete = itemView.findViewById(R.id.EventDelete);
            parent = itemView.findViewById(R.id.EventParent);
        }
    }
}
