package com.example.ecocar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TravelRecViewAdapter extends RecyclerView.Adapter<TravelRecViewAdapter.ViewHolder> {

    public ArrayList<Travel> travels = new ArrayList<>();
    private Context ctx;

    private String convertDuration (int seconds) {
        int duration_days = seconds / 86400;
        int duration_hours = (seconds % 86400) / 3600;
        int duration_minutes = ((seconds % 86400) % 3600) / 60;
        return (duration_days != 0 ? duration_days + "d" : "") + (duration_hours != 0 ? duration_hours + "h" : "") + (duration_minutes != 0 ? duration_minutes + "m" : "");
    }

    public TravelRecViewAdapter(Context c) {
        this.ctx = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.travel, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.TravelIniTime.setText(travels.get(position).getIni_time());
        holder.TravelEndTime.setText(travels.get(position).getEnd_time());
        holder.TravelDuration.setText(convertDuration(travels.get(position).getDuration()));
        holder.TravelOriAddress.setText(travels.get(position).getCity_ori());
        holder.TravelDesAddress.setText(travels.get(position).getCity_des());

        String price_euro = travels.get(position).getPrice() + ",00 €";
        holder.TravelPrice.setText(price_euro);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserRef = mDatabase.child("users").child(travels.get(position).getDriver());

        UserRef.get().addOnCompleteListener(task -> {
            CurrentUser driver = task.getResult().getValue(CurrentUser.class);
            driver.setUid(travels.get(position).getDriver());

            if (!driver.getPhotoURL().equals("")) holder.TravelUserImage.setImageURI(Uri.parse(driver.getPhotoURL()));
            holder.TravelDriverName.setText(driver.getUsername());
            holder.TravelDriverStars.setText(String.valueOf(driver.getStar()));

            holder.parent.setOnClickListener(view -> {
                Intent intent = new Intent(ctx, TravelDetailInfo.class);
                intent.putExtra("travel", travels.get(position));
                intent.putExtra("driver", driver);
                ctx.startActivity(intent);
            });
        });

        String distance_km = travels.get(position).getDistance() + " km";
        holder.TravelDistance.setText(distance_km);
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTravels(ArrayList<Travel> travels) {
        this.travels = travels;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addTravels(Travel travel) {
        this.travels.add(travel);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView TravelIniTime, TravelEndTime, TravelDuration, TravelOriAddress, TravelDesAddress, TravelPrice, TravelDriverName, TravelDistance, TravelDriverStars;
        private CircleImageView TravelUserImage;
        private CardView parent;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            TravelIniTime = itemView.findViewById(R.id.TravelIniTime);
            TravelEndTime = itemView.findViewById(R.id.TravelEndTime);
            TravelDuration = itemView.findViewById(R.id.TravelDuration);
            TravelOriAddress = itemView.findViewById(R.id.TravelOriAddress);
            TravelDesAddress = itemView.findViewById(R.id.TravelDesAddress);
            TravelPrice = itemView.findViewById(R.id.TravelPrice);
            TravelDriverName = itemView.findViewById(R.id.TravelDriverName);
            TravelDistance = itemView.findViewById(R.id.TravelDistance);
            TravelUserImage = itemView.findViewById(R.id.TravelUserImage);
            TravelDriverStars = itemView.findViewById(R.id.TravelDriverStars);
            parent = itemView.findViewById(R.id.TravelParent);

        }
    }
}
