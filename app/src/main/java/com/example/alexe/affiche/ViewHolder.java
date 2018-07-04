package com.example.alexe.affiche;

import android.support.v7.widget.RecyclerView;
import android.view.View;

// TODO: 7/4/18 Write comments here

public abstract class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindType(ListItem item);
}