package com.example.ecocar;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecViewAdapter extends RecyclerView.Adapter<CommentRecViewAdapter.ViewHolder> {

    public ArrayList<Comment> comments = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        CommentRecViewAdapter.ViewHolder holder = new CommentRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(comments.get(position).getUserID());
        userRef.get().addOnCompleteListener(task -> {
            String imageURL = task.getResult().child("photoURL").getValue(String.class);
            if (!imageURL.equals("")) holder.profileImage.setImageURI(Uri.parse(imageURL));
            holder.ratingBar.setRating(comments.get(position).getStars());
            holder.username.setText(task.getResult().child("username").getValue(String.class));
            holder.commentText.setText(comments.get(position).getComment());
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profileImage;
        private TextView commentText, username;
        private RatingBar ratingBar;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.CommentCard_userProfile);
            commentText = itemView.findViewById(R.id.CommentCard_text);
            username = itemView.findViewById(R.id.CommentCard_userName);
            ratingBar = itemView.findViewById(R.id.CommentCard_rating);
        }
    }
}
