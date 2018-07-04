package com.example.alexe.affiche;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

// The universal adapter. Allows to store ViewHolders
// of different configurations in one RecyclerView.

public class AdapterUniversal extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<ListItem> data;

    public AdapterUniversal(ArrayList<ListItem> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getListItemType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view;
        switch (type) {
            case ListItem.TYPE_CUSTOM:
                view = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.cards_layout, viewGroup, false);
                view.setOnClickListener(FragmentCategory.myOnClickListener);
                return new ViewHolderCustom(view);
            case ListItem.TYPE_CINEMA:
                view = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.info_cards_layout, viewGroup, false);
                view.setOnClickListener(Cinema.myOnClickListener);
                return new ViewHolderCinema(view);
            case ListItem.TYPE_THEATER:
                view = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.info_cards_theater, viewGroup, false);
                view.setOnClickListener(Theater.myOnClickListener);
                return new ViewHolderTheater(view);
            case ListItem.TYPE_CHILDREN:
                view = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.info_cards_theater, viewGroup, false);
                view.setOnClickListener(Children.myOnClickListener);
                return new ViewHolderChildren(view);
            case ListItem.TYPE_EVENT:
                view = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.info_cards__event, viewGroup, false);
                view.setOnClickListener(Events.myOnClickListener);
                return new ViewHolderEvent(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        ListItem item = data.get(pos);
        viewHolder.bindType(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}