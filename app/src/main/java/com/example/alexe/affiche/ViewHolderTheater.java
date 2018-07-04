package com.example.alexe.affiche;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

// TODO: 7/4/18 Write comments here

public class ViewHolderTheater extends ViewHolder {
    TextView textViewName;
    TextView textViewTime;
    TextView textViewDate;
    TextView textViewPlace;
    ImageView imageViewIcon;

    public ViewHolderTheater(View itemView) {
        super(itemView);
        this.textViewName = itemView.findViewById(R.id.textViewName);
        //this.textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
        this.textViewDate = itemView.findViewById(R.id.textViewDate);
        //this.textViewPlace = (TextView) itemView.findViewById(R.id.textViewPlace);
        this.imageViewIcon = itemView.findViewById(R.id.imageView);
    }

    @Override
    public void bindType(ListItem item) {
        Log.d("ViewHolderTheater", "bindType: " + ((CardModelTheater) item).getTitle());

        ImageView imageView = imageViewIcon;

        textViewName.setText(((CardModelTheater) item).getTitle());
        //textViewTime.setText(dataSet.get(listPosition).getTime());
        textViewDate.setText(((CardModelTheater) item).getDate());
        //textViewPlace.setText(dataSet.get(listPosition).getPlace());
        Picasso.with(itemView.getContext()) //передаем контекст приложения
                .load(((CardModelTheater) item).getImage()) //адрес изображения
                .into(imageView);
    }
}