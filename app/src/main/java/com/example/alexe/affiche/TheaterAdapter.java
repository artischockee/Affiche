package com.example.alexe.affiche;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.MyViewHolder> {

    private ArrayList<CardModelEvent> dataSet;
    AdapterView.OnItemClickListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewTime;
        TextView textViewDate;
        TextView textViewPlace;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            //this.textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            //this.textViewPlace = (TextView) itemView.findViewById(R.id.textViewPlace);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public interface OnItemClickListener {
        void onCardPressed(CardModelEvent cardModelTheater);
    }

    public TheaterAdapter(ArrayList<CardModelEvent> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info_cards_theater, parent, false);

        view.setOnClickListener(Theater.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView textViewName = holder.textViewName;
        //TextView textViewTime = holder.textViewTime;
        TextView textViewDate = holder.textViewDate;
        //TextView textViewPlace = holder.textViewPlace;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getTitle());
        //textViewTime.setText(dataSet.get(listPosition).getTime());
        textViewDate.setText(dataSet.get(listPosition).getDate());
        //textViewPlace.setText(dataSet.get(listPosition).getPlace());
        Picasso.with(holder.itemView.getContext()) //передаем контекст приложения
                .load(dataSet.get(listPosition).getImage()) //адрес изображения
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}