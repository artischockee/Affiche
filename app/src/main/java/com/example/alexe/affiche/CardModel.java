package com.example.alexe.affiche;

import android.media.Image;

public class CardModel {

    private String title;
    private String info;
    private String img;
    private String year;
    private String duration;
    private String address;

    public CardModel(String title, String info, String img, String year, String duration, String address) {
        this.title = title;
        this.info = info;
        this.img = img;
        this.year =year;
        this.duration = duration;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }
    public String getInfo() { return info; }
    public String getImage() {
        return img;
    }
    public String getYear() { return  year; }
    public String getDuration() { return duration; }
    public String getAddress() { return address; }
}
