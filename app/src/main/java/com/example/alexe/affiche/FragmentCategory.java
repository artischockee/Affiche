package com.example.alexe.affiche;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentCategory extends Fragment {
    private static AdapterUniversal adapter;
    public RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<ListItem> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();
        for (int i = 0; i < DataCategory.nameArray.length; i++) {
            data.add(new CardModelCategory(
                    DataCategory.nameArray[i],
                    DataCategory.drawableArray[i]
            ));
        }

        removedItems = new ArrayList<>();

        adapter = new AdapterUniversal(data);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);

        return view;
    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            selectItem(v);
        }

        private void selectItem(View v) {
            int select = recyclerView.getChildAdapterPosition(v);
            RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(select);
            TextView textView = vh.itemView.findViewById(R.id.textViewName);
            String title = (String) textView.getText();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            switch (title)
            {
                case "Кино" : {
                    Cinema fragment = new Cinema();
                    transaction.replace(R.id.frgmCont, fragment);
                    transaction.addToBackStack(fragment.getClass().getName());
                    transaction.commit();
                    break;
                }
                case "Театр" : {
                    Theater fragment = new Theater();
                    transaction.replace(R.id.frgmCont, fragment);
                    transaction.addToBackStack(fragment.getClass().getName());
                    transaction.commit();
                    break;
                }
                case "Для детей" : {
                    Children fragment = new Children();
                    transaction.replace(R.id.frgmCont, fragment);
                    transaction.addToBackStack(fragment.getClass().getName());
                    transaction.commit();
                    break;
                }
                case "Мероприятия" : {
                    Events fragment = new Events();
                    transaction.replace(R.id.frgmCont, fragment);
                    transaction.addToBackStack(fragment.getClass().getName());
                    transaction.commit();
                    break;
                }
            }
        }
    }
}