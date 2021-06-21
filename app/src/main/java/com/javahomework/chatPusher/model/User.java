package com.javahomework.chatPusher.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User {
    private String email;
    private String room;

    public User() {
        this.email = "None";
        this.room = "None";
    }

    public User(String email,String room) {
        this.email = email;
        this.room =room;
    }

    public String getEmail() {
        return email;
    }

    public String getRoom() {
        return room;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public void setRoom(String room) {
        this.room=room;
    }
}
