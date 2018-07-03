package com.example.alexe.affiche;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class Children extends Fragment {
    private static RecyclerView.Adapter adapter;
    private static ArrayList<CardModelEvent> data;
    private static ArrayList<Integer> removedItems;
    public RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private Document htmlDocumentTUZ;
    private Document htmlDocumentSKOM;
    private DBHelper dbhelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_children, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Дети");
        myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //http://tuz-tomsk.ru/afisha
        //http://www.skomoroh.tomsk.ru/events.html
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

            Log.d("title представления", title);

            ChildrenPageWebview fragment = new ChildrenPageWebview();
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

        //массивы для считывания инфы о представлении
        private Integer[] id;
        private String[] name;
        private String[] time;
        private String[] date;
        private String[] imageUrl;
        private String[] addressUrl;
        private String[] place;

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
            htmlDocumentTUZ = null;
          //  htmlDocumentSKOM = null;

            try {
                //коннектимся к сайтам
                htmlDocumentTUZ = Jsoup.connect("http://tuz-tomsk.ru/afisha").get();
               // htmlDocumentSKOM = Jsoup.connect("http://www.skomoroh.tomsk.ru/events.html").get();

                //считываем ТЮЗ
                Elements namesTUZ = htmlDocumentTUZ.getElementsByAttributeValue("class", "title");
                Elements datesTUZ = htmlDocumentTUZ.getElementsByAttributeValue("class", "date");
                Elements timesTUZ = htmlDocumentTUZ.getElementsByAttributeValue("class", "time");

                //количество представлений в скоморохе
               // final Integer SKOM_NUMBER = 5;

                final Integer ALL_NUMBER = namesTUZ.size();
                //final Integer ALL_NUMBER = 9;

                name = new String[ALL_NUMBER];
                time = new String[ALL_NUMBER];
                date = new String[ALL_NUMBER];
                imageUrl = new String[ALL_NUMBER];
                addressUrl = new String[ALL_NUMBER];
                place = new String[ALL_NUMBER];

                //забиваем ТЮЗ
                int name_TUZ_ind = 0, date_TUZ_ind = 0, time_TUZ_ind = 0;

                for(Element nameTUZ : namesTUZ)
                {
                    name[name_TUZ_ind] = nameTUZ.select("a").text();
                    addressUrl[name_TUZ_ind] = nameTUZ.select("a").attr("href");
                    Log.w("urls", addressUrl[name_TUZ_ind]);
                    imageUrl[name_TUZ_ind] = "http://tuz-tomsk.ru/template/tuz/img/logotuz-hover.png";
                    place[name_TUZ_ind] = "ТЮЗ";
                    name_TUZ_ind++;
                }

                for(Element dateTUZ : datesTUZ)
                {
                    date[date_TUZ_ind] = dateTUZ.text();
                    date_TUZ_ind++;
                }

                for(Element timeTUZ : timesTUZ)
                {
                    time[time_TUZ_ind] = timeTUZ.text();
                    time_TUZ_ind++;
                }

//                //считываем и забиваем СКОМОРОХ
//                int name_SKOM_ind = name_TUZ_ind, date_SKOM_ind = date_TUZ_ind;
//                for (int i = 6; i<9; i++)
//                {
//                    Elements namesSKOM = htmlDocumentSKOM.select("#leftside > div.bull-content > table > tbody > tr:nth-child(" + i + ") > td:nth-child(3) > p > strong:nth-child(1) > a");
//                    Elements datesSKOM = htmlDocumentSKOM.select("#leftside > div.bull-content > table > tbody > tr:nth-child(" + i + ") > td:nth-child(1) > p:nth-child(1)");
//                    Elements addressesSKOM = htmlDocumentSKOM.select("#leftside > div.bull-content > table > tbody > tr:nth-child(" + i + ") > td:nth-child(3) > p > strong:nth-child(1) > a");
//
//                    for (Element nameSKOM : namesSKOM)
//                    {
//                        name[name_SKOM_ind] = nameSKOM.text();
//                    }
//
//                    for (Element addressSKOM : addressesSKOM)
//                    {
//                        addressUrl[name_SKOM_ind] = "http://skomoroh.tomsk.ru" + addressSKOM.attr("href");
//                        Log.w("urls", addressUrl[name_SKOM_ind]);
//                    }
//
//                    for (Element dateSKOM : datesSKOM)
//                    {
//                        String datetime = dateSKOM.text();
//                        String[] datetimes = datetime.split(" ");
//                        date[date_SKOM_ind] = datetimes[0] + datetimes[1];
//                        time[date_SKOM_ind] = datetimes[2] + datetimes[3];
//                    }
//
//                    place[name_SKOM_ind] = "СКОМОРОХ";
//                    imageUrl[name_SKOM_ind] = "http://skomoroh.tomsk.ru/resources/img/logo_work.png";
//
//                    name_SKOM_ind++;
//                    date_SKOM_ind++;
//                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void insertChildren() {
            // Считываем данные из текстовых полей
            ContentValues contentValue = new ContentValues();
            for (int i = 0; i<name.length-1; i++) {
                contentValue.put(DBHelper.KEY_ID_1, (i+1));
                contentValue.put(DBHelper.KEY_TITLE_1, name[i]);
                contentValue.put(DBHelper.KEY_TIME_1, time[i]);
                contentValue.put(DBHelper.KEY_DATE_1, date[i]);
                contentValue.put(DBHelper.KEY_IMG_1, imageUrl[i]);
                contentValue.put(DBHelper.KEY_PLACE_1, place[i]);
                contentValue.put(DBHelper.KEY_ADDRESS_1, addressUrl[i]);
                sqLiteDatabase.insert(DBHelper.TABLE_NAME_2, null, contentValue);
            }
        }

        public void readChildren() {
            Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_2, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE_1);
                    int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME_1);
                    int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE_1);
                    int placeIndex = cursor.getColumnIndex(DBHelper.KEY_PLACE_1);
                    int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMG_1);
                    int urlIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS_1);

                    data.add(new CardModelEvent(cursor.getString(titleIndex), cursor.getString(timeIndex), cursor.getString(dateIndex),
                            cursor.getString(imageIndex), cursor.getString(urlIndex), cursor.getString(placeIndex)));
                    adapter = new ChildrenAdapter(data);
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
            data = new ArrayList<CardModelEvent>();
            insertChildren();
            readChildren();
            mDialog.dismiss();
        }
    }
}
