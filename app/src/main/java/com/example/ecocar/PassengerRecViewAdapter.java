package com.example.ecocar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PassengerRecViewAdapter extends RecyclerView.Adapter<PassengerRecViewAdapter.ViewHolder> {
    public ArrayList<CurrentUser> passengers;
    private Context ctx;

    public PassengerRecViewAdapter(Context ctx) {
        passengers = new ArrayList<>();
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.PassengerUsername.setText(passengers.get(position).getUsername());
        if (!passengers.get(position).getPhotoURL().equals("")) holder.PassengerUserImage.setImageURI(Uri.parse(passengers.get(position).getPhotoURL()));
        holder.parent.setOnClickListener(view -> {
            Intent intent = new Intent(ctx, OtherUserProfile.class);
            intent.putExtra("otherUser", passengers.get(position));
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addTravel(CurrentUser passenger) {
        this.passengers.add(passenger);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView PassengerUsername;
        private CircleImageView PassengerUserImage;
        private ConstraintLayout parent;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            PassengerUsername = itemView.findViewById(R.id.DetailPassengerName);
            PassengerUserImage = itemView.findViewById(R.id.DetailPassengerImage);
            parent = itemView.findViewById(R.id.DetailPassengerAccess);

        }
    }

}
