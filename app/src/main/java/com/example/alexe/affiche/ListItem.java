package com.example.alexe.affiche;

public interface ListItem {
    int TYPE_CUSTOM = 0;
    int TYPE_CINEMA = 1;
    int TYPE_THEATER = 2;
    int TYPE_CHILDREN = 3;
    int TYPE_EVENT = 4;

    int getListItemType();
}