package com.example.ecocar;

public class Message {
    private String dia, hora, authorID, message;

    public Message(String dia, String hora, String senderID, String message) {
        this.dia = dia;
        this.hora = hora;
        this.authorID = authorID;
        this.message = message;
    }

    public Message() {

    }

    public String getDia() {
        return dia;
    }

    public String getHora() {
        return hora;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getMessage() {
        return message;
    }

    public void setSenderID(String authorID) {
        this.authorID = authorID;
    }
}
