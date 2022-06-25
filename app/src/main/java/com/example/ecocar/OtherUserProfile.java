package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserProfile extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView profileImage;
    private TextView username, star, phone, email, biography, preferences;
    private RatingBar ratingBar;
    private AutoCompleteTextView mycar;
    private RelativeLayout publishR, commentsR;
    private CurrentUser otherUser;
    private Button follow;
    private Boolean clicked;
    private DatabaseReference favouriteRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        setTitle("Pefil");

        otherUser = getIntent().getExtras().getParcelable("otherUser");
        favouriteRef = FirebaseDatabase.getInstance().getReference().child("favourite").child(CurrentUser.getInstance().getUid()).child(otherUser.getUid());

        profileImage = findViewById(R.id.OU_image);
        username = findViewById(R.id.OU_username);
        star = findViewById(R.id.OU_star);
        phone = findViewById(R.id.OU_phone);
        email = findViewById(R.id.OU_contactEmail);
        biography = findViewById(R.id.OU_biography);
        preferences = findViewById(R.id.OU_preferences);
        ratingBar = findViewById(R.id.OU_rating);
        mycar = findViewById(R.id.OU_cars);
        publishR = findViewById(R.id.OU_myPublishedTravels);
        commentsR = findViewById(R.id.OU_opinions);
        follow = findViewById(R.id.OU_follow);

        if (!otherUser.getPhotoURL().equals("")) profileImage.setImageURI(Uri.parse(otherUser.getPhotoURL()));
        username.setText(otherUser.getUsername());
        if (otherUser.getGender().equals("hombre")) username.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_male, 0);
        else if (otherUser.getGender().equals("mujer")) username.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_female, 0);
        star.setText(String.valueOf(otherUser.getStar()));
        ratingBar.setRating(otherUser.getStar());
        phone.setText(String.valueOf(otherUser.getPhone()));
        email.setText(otherUser.getEmail());
        if (!otherUser.getBiography().equals("")) biography.setText(otherUser.getBiography());
        if (!otherUser.getPreferences().equals("")) preferences.setText(otherUser.getPreferences());

        if (otherUser.getCarSelected().equals("")) mycar.setText("No tiene coche todavía");
        else mycar.setText(otherUser.getCarSelected());

        mycar.setFocusable(false);

        publishR.setOnClickListener(this);
        commentsR.setOnClickListener(this);
        follow.setOnClickListener(this);

        if (getIntent().getExtras().getBoolean("favourite")) publishR.setVisibility(View.VISIBLE);

        favouriteRef.get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                clicked = true;
                follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check2, 0, 0, 0);
                follow.setText("SEGUIDO");
            }
            else {
                clicked = false;
                follow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                follow.setText("SEGUIR");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.OU_myPublishedTravels:
                Intent intent = new Intent(this, MyPubResTrips.class);
                Log.i("asd", "asd" + otherUser.getUid());
                intent.putExtra("option", "publish");
                intent.putExtra("userID",  otherUser.getUid());
                startActivity(intent);
                break;

            case R.id.OU_opinions:
                intent = new Intent(this, ConsultComment.class);
                intent.putExtra("option", "other");
                intent.putExtra("userID", otherUser.getUid());
                startActivity(intent);
                break;

            case R.id.OU_follow:
                if (!otherUser.getUid().equals(CurrentUser.getInstance().getUid())) {

                    if (!clicked) {
                        follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check2, 0, 0, 0);
                        follow.setText("SEGUIDO");
                        favouriteRef.setValue("");
                    }
                    else {
                        follow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        follow.setText("SEGUIR");
                        favouriteRef.removeValue();
                    }

                    clicked = !clicked;
                }
                else Toast.makeText(this, "¡No te puedes seguir a ti mismo!", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}