package com.example.alexe.affiche;

public class CardModelGeneral {
    protected String title;
    protected String img;
    protected String address;

    public CardModelGeneral(String title, String img, String address) {
        this.title = title;
        this.img = img;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }
    public String getImage() {
        return img;
    }
    public String getAddress() { return address; }
}
