package com.example.alexe.affiche;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

// TODO: 7/4/18 Write comments here

public class ViewHolderChildren extends ViewHolder {
    TextView textViewName;
    TextView textViewTime;
    TextView textViewDate;
    TextView textViewPlace;
    ImageView imageViewIcon;

    public ViewHolderChildren(View itemView) {
        super(itemView);
        this.textViewName = itemView.findViewById(R.id.textViewName);
        this.textViewTime = itemView.findViewById(R.id.textViewTime);
        this.textViewDate = itemView.findViewById(R.id.textViewDate);
        this.textViewPlace = itemView.findViewById(R.id.textViewPlace);
        this.imageViewIcon = itemView.findViewById(R.id.imageView);
    }

    @Override
    public void bindType(ListItem item) {
        Log.d("ViewHolderChildren", "bindType: " + ((CardModelChildren) item).getTitle());

        ImageView imageView = imageViewIcon;

        textViewName.setText(((CardModelChildren) item).getTitle());
        textViewTime.setText(((CardModelChildren) item).getTime());
        textViewDate.setText(((CardModelChildren) item).getDate());
        textViewPlace.setText(((CardModelChildren) item).getPlace());
        Picasso.with(itemView.getContext()) //передаем контекст приложения
                .load(((CardModelChildren) item).getImage()) //адрес изображения
                .into(imageView);
    }
}