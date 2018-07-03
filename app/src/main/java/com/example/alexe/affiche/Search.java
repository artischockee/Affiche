package com.example.alexe.affiche;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class Search extends Fragment {
    private static RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CardModel> data01;
    private static ArrayList<CardModel> data02;
    private static ArrayList<CardModel> data03;
    private static ArrayList<CardModel> data04;
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

    public void searchItem(String itemQuery) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

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

        private void testikk(Cursor cursor) {
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
                data01.add(new CardModel(cursor.getString(titleIndex), cursor.getString(infoIndex), cursor.getString(imageIndex),
                        cursor.getString(yearIndex), cursor.getString(durationIndex), cursor.getString(urlIndex)));
            } while (cursor.moveToNext());
        }

        private void selectItem(View v) {
            String searchQuery = et.getText().toString();
            data01 = new ArrayList<>();
            data02 = new ArrayList<>();
            data03 = new ArrayList<>();
            data04 = new ArrayList<>();
            Cursor cursor01 = sqLiteDatabase.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
            Cursor cursor02 = sqLiteDatabase.query(DBHelper.TABLE_NAME_1, null, null, null, null, null, null);
            Cursor cursor03 = sqLiteDatabase.query(DBHelper.TABLE_NAME_2, null, null, null, null, null, null);
            Cursor cursor04 = sqLiteDatabase.query(DBHelper.TABLE_NAME_3, null, null, null, null, null, null);



            cursor01.close();
            adapter = new CinemaAdapter(data01);
            recyclerView.setAdapter(adapter);

//            dbhelper.close();
//            sqLiteDatabase.close();

        }
    }
}