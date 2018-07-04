package com.example.alexe.affiche;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

// TODO: 7/4/18 Write comments here

public class ViewHolderCustom extends ViewHolder {
    TextView textViewName;
    ImageView imageViewIcon;

    public ViewHolderCustom(View itemView) {
        super(itemView);
        this.textViewName = itemView.findViewById(R.id.textViewName);
        this.imageViewIcon = itemView.findViewById(R.id.imageView);
    }

    @Override
    public void bindType(ListItem item) {
        Log.d("ViewHolderCustom", "bindType");

        ImageView imageView = imageViewIcon;

        textViewName.setText(((CardModelCategory) item).getName());
        imageView.setImageResource(((CardModelCategory) item).getImage());
    }
}