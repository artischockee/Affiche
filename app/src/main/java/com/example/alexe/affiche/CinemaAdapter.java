package com.example.alexe.affiche;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.MyViewHolder> {

    private ArrayList<CardModel> dataSet;
    AdapterView.OnItemClickListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewInfo;
        TextView textViewYear;
        TextView textViewDuration;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.textViewName);
            this.textViewInfo = itemView.findViewById(R.id.textViewInfo);
            this.textViewYear = itemView.findViewById(R.id.textViewYear);
            this.textViewDuration = itemView.findViewById(R.id.textViewDuration);
            this.imageViewIcon = itemView.findViewById(R.id.imageView);
        }
    }

    public interface OnItemClickListener {
        void onCardPressed(CardModel cardModel);
    }

    public CinemaAdapter(ArrayList<CardModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info_cards_layout, parent, false);

        view.setOnClickListener(Cinema.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView textViewName = holder.textViewName;
        TextView textViewInfo = holder.textViewInfo;
        TextView textViewYear = holder.textViewYear;
        TextView textViewDuration = holder.textViewDuration;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getTitle());
        textViewInfo.setText(dataSet.get(listPosition).getInfo());
        textViewYear.setText(dataSet.get(listPosition).getYear());
        textViewDuration.setText(dataSet.get(listPosition).getDuration());
        Picasso.with(holder.itemView.getContext()) //передаем контекст приложения
                .load(dataSet.get(listPosition).getImage()) //адрес изображения
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}