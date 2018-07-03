package com.example.alexe.affiche;

public class CardModelEvent extends CardModelGeneral {
    private String time;
    private String date;
    private String place;

    public CardModelEvent(String title, String time, String date, String img, String address, String place) {
        super(title, img, address);

        this.time = time;
        this.date = date;
        this.place = place;
    }

    public String getTime() { return time; }
    public String getDate() { return date; }
    public String getPlace() { return place; }
}
