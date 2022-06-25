package com.example.ecocar;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRecViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<Message> messages = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent, parent, false);
            MessageRecViewAdapter.SentViewHolder holder = new SentViewHolder(view);
            return holder;
        }
        else if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received, parent, false);
            MessageRecViewAdapter.ReceivedViewHolder holder = new ReceivedViewHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass() == SentViewHolder.class) {
            ((SentViewHolder) holder).message.setText(messages.get(position).getMessage());
        }
        else if (holder.getClass() == ReceivedViewHolder.class) {
            ((ReceivedViewHolder) holder).message.setText(messages.get(position).getMessage());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message currentMessage = messages.get(position);
        if (currentMessage.getAuthorID().equals(CurrentUser.getInstance().getUid())) return 1;
        else return 2;
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        public SentViewHolder (@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.sent_message);
        }
    }

    public class ReceivedViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        public ReceivedViewHolder (@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.received_message);
        }
    }

}
