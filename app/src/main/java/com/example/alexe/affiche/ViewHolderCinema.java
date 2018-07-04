package com.example.alexe.affiche;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

// TODO: 7/4/18 Write comments here

public class ViewHolderCinema extends ViewHolder {
    TextView textViewName;
    TextView textViewInfo;
    TextView textViewYear;
    TextView textViewDuration;
    ImageView imageViewIcon;

    public ViewHolderCinema(View itemView) {
        super(itemView);
        this.textViewName = itemView.findViewById(R.id.textViewName);
        this.textViewInfo = itemView.findViewById(R.id.textViewInfo);
        this.textViewYear = itemView.findViewById(R.id.textViewYear);
        this.textViewDuration = itemView.findViewById(R.id.textViewDuration);
        this.imageViewIcon = itemView.findViewById(R.id.imageView);
    }

    @Override
    public void bindType(ListItem item) {
        Log.d("ViewHolderCinema", "bindType: " + ((CardModelCinema) item).getTitle());

        ImageView imageView = imageViewIcon;

        textViewName.setText(((CardModelCinema) item).getTitle());
        textViewInfo.setText(((CardModelCinema) item).getInfo());
        textViewYear.setText(((CardModelCinema) item).getYear());
        textViewDuration.setText(((CardModelCinema) item).getDuration());
        Picasso.with(itemView.getContext()) //передаем контекст приложения
                .load(((CardModelCinema) item).getImage()) //адрес изображения
                .into(imageView);
    }
}