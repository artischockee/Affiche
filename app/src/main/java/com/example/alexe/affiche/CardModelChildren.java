package com.example.alexe.affiche;

public class CardModelChildren implements ListItem {
    private String title;
    private String time;
    private String date;
    private String img;
    private String address;
    private String place;

    public CardModelChildren(String title, String time, String date, String img, String address, String place) {
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

    @Override
    public int getListItemType() {
        return ListItem.TYPE_CHILDREN;
    }
}
