package com.example.alexe.affiche;

public class CardModel extends CardModelGeneral {
    private String info;
    private String year;
    private String duration;

    public CardModel(String title, String info, String img, String year, String duration, String address) {
        super(title, img, address);

        this.info = info;
        this.year = year;
        this.duration = duration;
    }

    public String getInfo() { return info; }
    public String getYear() { return  year; }
    public String getDuration() { return duration; }
}
