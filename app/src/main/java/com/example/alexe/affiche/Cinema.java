package com.example.alexe.affiche;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Cinema extends Fragment {
    private static AdapterUniversal adapter;
    private static ArrayList<ListItem> data;
    private static ArrayList<Integer> removedItems;
    public RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private Document htmlDocument;
    private DBHelper dbhelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Кино");
        myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //taking info from goodwincinema
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        removedItems = new ArrayList<Integer>();

        Context context = getActivity();
        dbhelper = new DBHelper(context);
        sqLiteDatabase = dbhelper.getWritableDatabase();

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
            TextView textView = (TextView)vh.itemView.findViewById(R.id.textViewName);
            String title = (String) textView.getText();

            CinemaPageWebview fragment = new CinemaPageWebview();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frgmCont, fragment);
            transaction.addToBackStack(fragment.getClass().getName());
            transaction.commit();
        }
    }

    class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        //массивы для считывания инфы о фильме
        private String[] name;
        private String[] info;
        private String[] year;
        private String[] duration;
        private String[] imageUrl;
        private String[] addressUrl;

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
            mDialog.setMessage("Идет загрузка");
            mDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            htmlDocument = null;

            try {
                //считываем весь сайт и разбиваем на элементы
                htmlDocument = Jsoup.connect("http://goodwincinema.ru/affiche").get();
                //название, год, продолжительность, инфа
                Elements names = htmlDocument.getElementsByAttributeValue("class", "film-title");
                Elements film_tables = htmlDocument.getElementsByAttributeValue("class", "film-table");
                Elements images = htmlDocument.getElementsByAttributeValue("class", "left");

                name = new String[film_tables.size()];
                info = new String[film_tables.size()];
                year = new String[film_tables.size()];
                duration = new String[film_tables.size()];
                imageUrl = new String[film_tables.size()];
                addressUrl = new String[film_tables.size()];

                int name_ind = 0, image_ind = 0, table_ind = 0;

                for(Element ElementName : names)
                {
                    Element aElement = ElementName.child(0);
                    name[name_ind] = aElement.text();
                    name_ind++;
                }

                for(Element ElementImage : images)
                {
                    Elements aElement = ElementImage.getElementsByClass("img");
                    for (Element bElement : aElement) {
                        imageUrl[image_ind] = "http://goodwincinema.ru" + bElement.select("a").select("img").attr("src");
                        addressUrl[image_ind] = "http://goodwincinema.ru" + bElement.select("a").attr("href");
                    }
                    image_ind++;
                }

                for(Element ElementTable : film_tables)
                {
                    Element SubFElement = ElementTable.child(0);
                    Element SubDElement = SubFElement.child(0);
                    for(Element SubTElement : SubDElement.children()) {

                        String label = SubTElement.text();
                        String[] labels = label.split(" ");

                        switch (labels[0]) {
                            case "Год": {
                                year[table_ind] = label;
                                break;
                            }

                            case "Жанр": {
                                info[table_ind] = label;
                                break;
                            }

                            case "Продолжительность": {
                                duration[table_ind] = label;
                                break;
                            }
                        }
                    }
                    table_ind++;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void insertCinema() {
            // Считываем данные из текстовых полей
            ContentValues contentValue = new ContentValues();
            for (int i = 0; i<name.length; i++) {
                contentValue.put(DBHelper.KEY_ID_1, (i+1));
                contentValue.put(DBHelper.KEY_TITLE, name[i]);
                contentValue.put(DBHelper.KEY_INFO, info[i]);
                contentValue.put(DBHelper.KEY_YEAR, year[i]);
                contentValue.put(DBHelper.KEY_DURATION, duration[i]);
                contentValue.put(DBHelper.KEY_IMG, imageUrl[i]);
                contentValue.put(DBHelper.KEY_ADDRESS, addressUrl[i]);
                sqLiteDatabase.insert(DBHelper.TABLE_NAME, null, contentValue);
            }
        }

        public void readCinema() {
            Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                    int infoIndex = cursor.getColumnIndex(DBHelper.KEY_INFO);
                    int yearIndex = cursor.getColumnIndex(DBHelper.KEY_YEAR);
                    int durationIndex = cursor.getColumnIndex(DBHelper.KEY_DURATION);
                    int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMG);
                    int urlIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS);

                    data.add(new CardModelCinema(
                            cursor.getString(titleIndex),
                            cursor.getString(infoIndex),
                            cursor.getString(imageIndex),
                            cursor.getString(yearIndex),
                            cursor.getString(durationIndex),
                            cursor.getString(urlIndex))
                    );
                    adapter = new AdapterUniversal(data);
                    recyclerView.setAdapter(adapter);
                } while (cursor.moveToNext());
                cursor.close();
            }
            dbhelper.close();
            sqLiteDatabase.close();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            data = new ArrayList<>();
            insertCinema();
            readCinema();
            mDialog.dismiss();
        }
    }
}
