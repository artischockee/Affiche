package com.example.alexe.affiche;

import android.media.Image;

public class CategoryModel {

    private String name;
    private int img;

    public CategoryModel(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return img;
    }
}