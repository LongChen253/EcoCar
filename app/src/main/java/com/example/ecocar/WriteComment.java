package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class WriteComment extends AppCompatActivity {

    private EditText commentText;
    private RatingBar ratingBar;;
    private Button submit;

    private boolean checkFields() {
        if (commentText.getText().equals("") || ratingBar.getRating() == 0.0) return false;
        else return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        setTitle("Valoración del viaje");

        commentText = findViewById(R.id.Comment_editText);
        ratingBar = findViewById(R.id.Comment_rating);
        submit = findViewById(R.id.Comment_submit);

        CurrentUser user = getIntent().getExtras().getParcelable("user");
        String travelID = getIntent().getExtras().getString("travel");

        submit.setOnClickListener(view -> {
            if (checkFields()) {
                String key = CurrentUser.getInstance().getUid() + " " + travelID;
                DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("comments").child(key);

                commentRef.get().addOnCompleteListener(task -> {
                    if (task.getResult().exists())
                        Toast.makeText(this, "Ya has valorado este viaje", Toast.LENGTH_SHORT).show();
                    else {
                        HashMap<String, Object> info = new HashMap<>();
                        info.put("stars", ratingBar.getRating());
                        info.put("comment", commentText.getText().toString());
                        info.put("sender", CurrentUser.getInstance().getUid());
                        info.put("receiver", user.getUid());
                        commentRef.updateChildren(info);

                        int total = user.getNumComments();
                        ++total;
                        float stars = user.getStar();
                        float new_stars = (stars + ratingBar.getRating()) / total;
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                        userRef.child("numComments").setValue(total);
                        userRef.child("star").setValue(new_stars);

                        finish();
                    }
                });
            }
            else Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
        });

    }
}