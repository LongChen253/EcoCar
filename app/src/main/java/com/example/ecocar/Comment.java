package com.example.ecocar;

import android.widget.RatingBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment {

    private String comment, userID;
    private float stars;

    public Comment(String comment, String userID, float stars) {
        this.comment = comment;
        this.userID = userID;
        this.stars = stars;
    }

    public Comment() {

    }

    public String getComment() {
        return comment;
    }

    public String getUserID() {
        return userID;
    }

    public float getStars() {
        return stars;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}


