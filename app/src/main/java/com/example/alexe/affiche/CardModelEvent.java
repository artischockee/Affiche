package com.example.alexe.affiche;

import android.media.Image;

public class CardModelEvent {

    private String title;
    private String time;
    private String date;
    private String img;
    private String address;
    private String place;

    public CardModelEvent(String title, String time, String date, String img, String address, String place) {
        this.title = title;
        this.time = time;
        this.date = date;
        this.img = img;
        this.address = address;
        this.place = place;
    }

    public String getTitle() {
        return title;
    }
    public String getTime() { return time; }
    public String getDate() { return date; }
    public String getImage() {
        return img;
    }
    public String getAddress() { return address; }
    public String getPlace() { return place; }
}
