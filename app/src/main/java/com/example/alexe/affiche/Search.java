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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class Search extends Fragment {
    private static AdapterUniversal adapter;
    private static ArrayList<ListItem> arrayList = new ArrayList<>();
    public RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    static View.OnClickListener myOnClickListener;
//    private static ArrayList<Integer> removedItems;

    EditText searchLine;
    ImageView searchButton;

    private SQLiteDatabase sqLiteDatabase;

    private String searchQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        removedItems = new ArrayList<>();

        searchLine = getActivity().findViewById(R.id.editSearch);

        Context context = getActivity();
        DBHelper dbhelper = new DBHelper(context);
        sqLiteDatabase = dbhelper.getReadableDatabase();

        EditText editText = getActivity().findViewById(R.id.editSearch);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return false;

                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        performSearchOperation();
                        return true;
                    default:
                        break;
                }

                return false;
            }
        });

        return view;
    }

    private ArrayList<ListItem> queryTableCinema(Cursor cursor) {
        Log.d("Search", "queryTableCinema");

        if (!cursor.moveToFirst())
            return null;

        ArrayList<ListItem> list = new ArrayList<>();

        do {
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            String databaseString = cursor.getString(titleIndex).toLowerCase();

            if (!databaseString.contains(searchQuery))
                continue;

            int infoIndex = cursor.getColumnIndex(DBHelper.KEY_INFO);
            int yearIndex = cursor.getColumnIndex(DBHelper.KEY_YEAR);
            int durationIndex = cursor.getColumnIndex(DBHelper.KEY_DURATION);
            int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMG);
            int urlIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS);

            list.add(new CardModelCinema(
                cursor.getString(titleIndex),
                cursor.getString(infoIndex),
                cursor.getString(imageIndex),
                cursor.getString(yearIndex),
                cursor.getString(durationIndex),
                cursor.getString(urlIndex))
            );
        } while (cursor.moveToNext());

        cursor.close();

        return list;
    }

    private ArrayList<ListItem> queryTableOthers(Cursor cursor, String tableName) {
        Log.d("Search", "queryTableOthers: " + tableName);

        if (!cursor.moveToFirst())
            return null;

        ArrayList<ListItem> list = new ArrayList<>();

        do {
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE_1);

            String databaseString = cursor.getString(titleIndex).toLowerCase();
            if (!databaseString.contains(searchQuery))
                continue;

            int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME_1);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE_1);
            int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMG_1);
            int addressIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS_1);
            int placeIndex = cursor.getColumnIndex(DBHelper.KEY_PLACE_1);

            switch (tableName) {
                // Theater:
                case DBHelper.TABLE_NAME_1:
                    list.add(new CardModelTheater(
                        cursor.getString(titleIndex),
                        cursor.getString(timeIndex),
                        cursor.getString(dateIndex),
                        cursor.getString(imageIndex),
                        cursor.getString(addressIndex),
                        cursor.getString(placeIndex))
                    );
                    break;
                // Children:
                case DBHelper.TABLE_NAME_2:
                    list.add(new CardModelChildren(
                        cursor.getString(titleIndex),
                        cursor.getString(timeIndex),
                        cursor.getString(dateIndex),
                        cursor.getString(imageIndex),
                        cursor.getString(addressIndex),
                        cursor.getString(placeIndex))
                    );
                    break;
                // Events:
                case DBHelper.TABLE_NAME_3:
                    list.add(new CardModelEvent(
                        cursor.getString(titleIndex),
                        cursor.getString(timeIndex),
                        cursor.getString(dateIndex),
                        cursor.getString(imageIndex),
                        cursor.getString(addressIndex),
                        cursor.getString(placeIndex))
                    );
                    break;
            }
        } while (cursor.moveToNext());

        cursor.close();

        return list;
    }

    private void performSearchOperation() {
        Log.d("Search", "performSearchOperation");

        searchQuery = searchLine.getText().toString().toLowerCase();

        Cursor cursor;

        // Cinema:
        cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME,
            null, null, null, null, null, null);
        arrayList.addAll(queryTableCinema(cursor));

        // Theater:
        cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_1,
            null, null, null, null, null, null);
        arrayList.addAll(queryTableOthers(cursor, DBHelper.TABLE_NAME_1));

        // Children:
        cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_2,
            null, null, null, null, null, null);
        arrayList.addAll(queryTableOthers(cursor, DBHelper.TABLE_NAME_2));

        // Events:
        cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_3,
            null, null, null, null, null, null);
        arrayList.addAll(queryTableOthers(cursor, DBHelper.TABLE_NAME_3));

        adapter = new AdapterUniversal(arrayList);
        recyclerView.setAdapter(adapter);

        searchQuery = null;
    }

    private class MyOnClickListener implements View.OnClickListener {
        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

        }
    }
}