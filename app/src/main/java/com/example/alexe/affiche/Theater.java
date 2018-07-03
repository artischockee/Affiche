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

public class Theater extends Fragment {
    private static RecyclerView.Adapter adapter;
    private static ArrayList<CardModelTheater> data;
    private static ArrayList<Integer> removedItems;
    public RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private Document htmlDocumentBKZ;
    private Document htmlDocumentDRAM;
    private DBHelper dbhelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theater, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Театр");
        myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //http://philharmonic.tomsk.ru/afisha1
        //http://www.tomskdrama.ru/playbill/index.php
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

            TheaterPageWebview fragment = new TheaterPageWebview();
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
       // private Integer[] id;
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
            htmlDocumentBKZ = null;
            htmlDocumentDRAM = null;

            try {
                //коннектимся к сайтам
                htmlDocumentBKZ = Jsoup.connect("http://philharmonic.tomsk.ru/afisha1").get();
                htmlDocumentDRAM = Jsoup.connect("http://www.tomskdrama.ru/playbill/index.php").get();

                //считываем БКЗ
                Elements namesBKZ = htmlDocumentBKZ.getElementsByAttributeValue("class", "nsp_header tleft fnone");
                Elements datesBKZ = htmlDocumentBKZ.getElementsByAttributeValue("class", "nsp_info  tleft fnone");
                Elements imagesBKZ = htmlDocumentBKZ.getElementsByAttributeValue("class", "nsp_image tleft fleft");
                Elements timesBKZ = htmlDocumentBKZ.getElementsByAttributeValue("class", "nsp_text tleft fnone");

                //количество представлений в драме
                final Integer DRAM_NUMBER = 9;

                final Integer ALL_NUMBER = namesBKZ.size() + DRAM_NUMBER;
                //final Integer ALL_NUMBER = 9;

                name = new String[ALL_NUMBER];
                time = new String[ALL_NUMBER];
                date = new String[ALL_NUMBER];
                imageUrl = new String[ALL_NUMBER];
                addressUrl = new String[ALL_NUMBER];
                place = new String[ALL_NUMBER];

                //забиваем БКЗ
                int name_BKZ_ind = 0, date_BKZ_ind = 0, image_BKZ_ind = 0, time_BKZ_ind = 0;

                for(Element nameBKZ : namesBKZ)
                {
                    name[name_BKZ_ind] = nameBKZ.select("a").text();
                    addressUrl[name_BKZ_ind] = "http://philharmonic.tomsk.ru" + nameBKZ.select("a").attr("href");
                    place[name_BKZ_ind] = "БКЗ";
                    name_BKZ_ind++;
                }

                int check_ind = 0;
                for(Element dateBKZ : datesBKZ)
                {
                    if (check_ind % 2 == 0)
                    {
                        date[date_BKZ_ind] = dateBKZ.text();
                        date_BKZ_ind++;
                    }
                    check_ind++;
                }

                for(Element imageBKZ : imagesBKZ)
                {
                    imageUrl[image_BKZ_ind] = "http://philharmonic.tomsk.ru" + imageBKZ.attr("src");
                    image_BKZ_ind++;
                }

                for(Element timeBKZ : timesBKZ)
                {
                    time[time_BKZ_ind] = timeBKZ.text();
                    time_BKZ_ind++;
                }

                //считываем и забиваем ДРАМ
                int name_DRAM_ind = name_BKZ_ind, date_DRAM_ind = date_BKZ_ind, time_DRAM_ind = time_BKZ_ind;
                for (int i = 2; i<11; i++)
                {
                    Elements namesDRAM;
                    if (i == 2)
                        namesDRAM = htmlDocumentDRAM.select("#Table_center > tbody > tr > td > table > tbody > tr:nth-child(" + i + ") > td:nth-child(3) > p:nth-child(3) > strong");
                    else
                        namesDRAM = htmlDocumentDRAM.select("#Table_center > tbody > tr > td > table > tbody > tr:nth-child(" + i + ") > td:nth-child(3) > p:nth-child(2) > strong");
                    Elements datesDRAM = htmlDocumentDRAM.select("#Table_center > tbody > tr > td > table > tbody > tr:nth-child(" + i + ") > td:nth-child(1) > p:nth-child(1)");
                    Elements timesDRAM = htmlDocumentDRAM.select("#Table_center > tbody > tr > td > table > tbody > tr:nth-child(" + i + ") > td:nth-child(2) > p");
                    Elements addressesDRAM = htmlDocumentDRAM.select("#Table_center > tbody > tr > td > table > tbody > tr:nth-child(" + i + ") > td:nth-child(4) > p > u > a");

                    for (Element nameDRAM : namesDRAM)
                    {
                        name[name_DRAM_ind] = nameDRAM.text();
                    }

                    for (Element addressDRAM : addressesDRAM)
                    {
                        addressUrl[name_DRAM_ind] = addressDRAM.attr("href");
                    }

                    for (Element dateDRAM : datesDRAM)
                    {
                        date[date_DRAM_ind] = dateDRAM.text();
                    }

                    for (Element timeDRAM : timesDRAM)
                    {
                        time[time_DRAM_ind] = timeDRAM.text();
                    }

                    place[name_DRAM_ind] = "ДРАМЫ";
                    imageUrl[name_DRAM_ind] = "http://www.tomskdrama.ru/bitrix/templates/main/images/pic_03.jpg";

                    name_DRAM_ind++;
                    date_DRAM_ind++;
                    time_DRAM_ind++;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void insertTheater() {
            // Считываем данные из текстовых полей
            ContentValues contentValue = new ContentValues();
            for (int i = 0; i<name.length; i++) {
                contentValue.put(DBHelper.KEY_ID_1, (i+1));
                contentValue.put(DBHelper.KEY_TITLE_1, name[i]);
                contentValue.put(DBHelper.KEY_TIME_1, time[i]);
                contentValue.put(DBHelper.KEY_DATE_1, date[i]);
                contentValue.put(DBHelper.KEY_IMG_1, imageUrl[i]);
                contentValue.put(DBHelper.KEY_PLACE_1, place[i]);
                contentValue.put(DBHelper.KEY_ADDRESS_1, addressUrl[i]);
                sqLiteDatabase.insert(DBHelper.TABLE_NAME_1, null, contentValue);
            }
        }

        public void readTheater() {
            Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_1, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE_1);
                    int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME_1);
                    int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE_1);
                    int placeIndex = cursor.getColumnIndex(DBHelper.KEY_PLACE_1);
                    int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMG_1);
                    int urlIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS_1);

                    data.add(new CardModelTheater(cursor.getString(titleIndex), cursor.getString(timeIndex), cursor.getString(dateIndex),
                            cursor.getString(imageIndex), cursor.getString(urlIndex), cursor.getString(placeIndex)));
                    adapter = new TheaterAdapter(data);
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
            data = new ArrayList<CardModelTheater>();
            insertTheater();
            readTheater();
            mDialog.dismiss();
        }
    }
}
