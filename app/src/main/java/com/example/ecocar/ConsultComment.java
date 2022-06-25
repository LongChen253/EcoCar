package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConsultComment extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_comment);

        recyclerView = findViewById(R.id.ConsultComment_rv);

        userID = getIntent().getExtras().getString("userID");
        String option = getIntent().getExtras().getString("option");
        ArrayList<Comment> comments = new ArrayList<>();
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("comments");

        if (option.equals("mine")) {
            setTitle("Mis comentarios escritos");
            commentsRef.orderByChild("sender").equalTo(userID).get().addOnCompleteListener(task -> {
                for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                    Comment comment = childSnapshot.getValue(Comment.class);
                    comment.setUserID(childSnapshot.child("receiver").getValue(String.class));
                    comments.add(comment);
                }
                CommentRecViewAdapter adapter = new CommentRecViewAdapter();
                adapter.setComments(comments);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            });
        }
        else if (option.equals("other")) {
            setTitle("Mis comentarios recibidos");
            commentsRef.orderByChild("receiver").equalTo(userID).get().addOnCompleteListener(task -> {
                for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                    Comment comment = childSnapshot.getValue(Comment.class);
                    comment.setUserID(childSnapshot.child("sender").getValue(String.class));
                    comments.add(comment);
                }
                CommentRecViewAdapter adapter = new CommentRecViewAdapter();
                adapter.setComments(comments);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            });
        }
    }
}