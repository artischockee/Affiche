package com.example.alexe.affiche;

public class CardModelCategory implements ListItem {
    private String name;
    private int img;

    public CardModelCategory(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return img;
    }

    @Override
    public int getListItemType() {
        return ListItem.TYPE_CUSTOM;
    }
}