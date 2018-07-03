package com.example.alexe.affiche;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class Search extends Fragment {
    private static RecyclerView.Adapter adapter01;
    private static RecyclerView.Adapter adapter02;
    private static RecyclerView.Adapter adapter03;
    private static RecyclerView.Adapter adapter04;
    public RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CardModel> data01 = new ArrayList<>();
    private static ArrayList<CardModelEvent> data02 = new ArrayList<>();
    private static ArrayList<CardModelEvent> data03 = new ArrayList<>();
    private static ArrayList<CardModelEvent> data04 = new ArrayList<>();
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    EditText et;
    ImageView te;

    private DBHelper dbhelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        removedItems = new ArrayList<Integer>();

        et = (EditText) view.findViewById(R.id.editText);

        te = (ImageView) view.findViewById(R.id.search_button);
        te.setOnClickListener(myOnClickListener);

        Context context = getActivity();
        dbhelper = new DBHelper(context);
        sqLiteDatabase = dbhelper.getReadableDatabase();

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

        private void searchTableCinema(Cursor cursor, ArrayList data, String searchQuery) {
            Log.d("ФУНКЦИЯ", "ЗАШЛИ В ПОИСК КИНО");
            if (!cursor.moveToFirst())
                return;

            do {
                int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                String databaseString = cursor.getString(titleIndex).toLowerCase();
                searchQuery = searchQuery.toLowerCase();

                if (!databaseString.contains(searchQuery))
                    continue;

                int infoIndex = cursor.getColumnIndex(DBHelper.KEY_INFO);
                int yearIndex = cursor.getColumnIndex(DBHelper.KEY_YEAR);
                int durationIndex = cursor.getColumnIndex(DBHelper.KEY_DURATION);
                int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMG);
                int urlIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS);
                data.add(new CardModel(cursor.getString(titleIndex), cursor.getString(infoIndex), cursor.getString(imageIndex),
                        cursor.getString(yearIndex), cursor.getString(durationIndex), cursor.getString(urlIndex)));
            } while (cursor.moveToNext());

            cursor.close();
        }

        private void searchTableOthers(Cursor cursor, ArrayList data, String searchQuery) {
            Log.d("ФУНКЦИЯ", "ЗАШЛИ В ПОИСК ГОВНА");
            if (!cursor.moveToFirst())
                return;

            do {
                int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE_1);
                String databaseString = cursor.getString(titleIndex).toLowerCase();
                searchQuery = searchQuery.toLowerCase();

                if (!databaseString.contains(searchQuery))
                    continue;

                int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME_1);
                int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE_1);
                int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMG_1);
                int placeIndex = cursor.getColumnIndex(DBHelper.KEY_PLACE_1);
                int addressIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS_1);

                data.add(new CardModelEvent(
                        cursor.getString(titleIndex),
                        cursor.getString(timeIndex),
                        cursor.getString(placeIndex),
                        cursor.getString(dateIndex),
                        cursor.getString(imageIndex),
                        cursor.getString(addressIndex)));

            } while (cursor.moveToNext());

            cursor.close();
        }

        private void selectItem(View v) {
            Log.d("button", "НАЖАЛИ");
            String searchQuery = et.getText().toString();

            Cursor cursor01 = sqLiteDatabase.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
            Cursor cursor02 = sqLiteDatabase.query(DBHelper.TABLE_NAME_1, null, null, null, null, null, null);
            Cursor cursor03 = sqLiteDatabase.query(DBHelper.TABLE_NAME_2, null, null, null, null, null, null);
            Cursor cursor04 = sqLiteDatabase.query(DBHelper.TABLE_NAME_3, null, null, null, null, null, null);

            searchTableCinema(cursor01, data01, searchQuery);
            searchTableOthers(cursor02, data02, searchQuery);
            searchTableOthers(cursor03, data03, searchQuery);
            searchTableOthers(cursor04, data04, searchQuery);

            adapter01 = new CinemaAdapter(data01);
            recyclerView.setAdapter(adapter01);
//            adapter02 = new TheaterAdapter(data02);
//            recyclerView.setAdapter(adapter02);
            //adapter03 = new ChildrenAdapter(data03);
            //recyclerView.setAdapter(adapter03);
            //adapter04 = new EventAdapter(data04);
            //recyclerView.setAdapter(adapter04);

//            dbhelper.close();
//            sqLiteDatabase.close();

        }
    }
}