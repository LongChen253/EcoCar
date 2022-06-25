package com.example.ecocar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavouriteRecViewAdapter extends RecyclerView.Adapter<FavouriteRecViewAdapter.ViewHolder> {

    public ArrayList<CurrentUser> favouriteDrivers;
    private Context ctx;

    public FavouriteRecViewAdapter(Context ctx) {
        favouriteDrivers = new ArrayList<>();
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite, parent, false);
        FavouriteRecViewAdapter.ViewHolder holder = new FavouriteRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!favouriteDrivers.get(position).getPhotoURL().equals("")) holder.userImage.setImageURI(Uri.parse(favouriteDrivers.get(position).getPhotoURL()));
        holder.username.setText(favouriteDrivers.get(position).getUsername());

        DecimalFormat aux = new DecimalFormat("0.0");
        holder.star.setText(aux.format(favouriteDrivers.get(position).getStar()));

        holder.chat.setOnClickListener(view -> {
            Intent intent = new Intent(ctx, Chat.class);
            intent.putExtra("receiverID", favouriteDrivers.get(position).getUid());
            intent.putExtra("driverName", favouriteDrivers.get(position).getUsername());
            ctx.startActivity(intent);
        });

        holder.search.setOnClickListener(view -> {
            Intent intent = new Intent(ctx, OtherUserProfile.class);
            intent.putExtra("otherUser", favouriteDrivers.get(position));
            intent.putExtra("favourite", true);
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favouriteDrivers.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addFavouriteDrivers(CurrentUser favouriteDriver) {
        this.favouriteDrivers.add(favouriteDriver);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView username, star;
        private CircleImageView userImage;
        private ImageButton chat, search;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.FavouriteName);
            star = itemView.findViewById(R.id.FavouriteStars);
            userImage = itemView.findViewById(R.id.FavouriteImage);
            chat = itemView.findViewById(R.id.FavouriteChat);
            search = itemView.findViewById(R.id.FavouriteSearch);
        }
    }
}
