package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class Chat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText text;
    private FloatingActionButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle(getIntent().getExtras().getString("driverName"));

        recyclerView = findViewById(R.id.ChatMessages);
        text = findViewById(R.id.ChatET);
        sendButton = findViewById(R.id.ChatSendButton);

        ArrayList<Message> messages = new ArrayList<>();

        String senderRoomKey = CurrentUser.getInstance().getUid() + " " + getIntent().getExtras().getString("receiverID");
        String receiverRoomKey = getIntent().getExtras().getString("receiverID") + " " + CurrentUser.getInstance().getUid();

        DatabaseReference senderChatsRef = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoomKey);
        DatabaseReference receiverChatsRef = FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoomKey);

        MessageRecViewAdapter messageRecViewAdapter = new MessageRecViewAdapter();
        recyclerView.setAdapter(messageRecViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));

        senderChatsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    messages.add(childSnapshot.getValue(Message.class));
                }
                messageRecViewAdapter.setMessages(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendButton.setOnClickListener(view -> {
            DatabaseReference senderMessageRef = senderChatsRef.push();
            DatabaseReference receiverMessageRef = receiverChatsRef.push();

            if (!text.getText().toString().equals("")) {

                HashMap<String, Object> m = new HashMap<>();

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
                String[] date = spf.format(calendar.getTime()).split("-");

                m.put("message", text.getText().toString());
                m.put("authorID", CurrentUser.getInstance().getUid());
                m.put("dia", date[0]);
                m.put("hora", date[1]);

                text.setText("");
                senderMessageRef.updateChildren(m);
                receiverMessageRef.updateChildren(m);
            }
        });
    }
}