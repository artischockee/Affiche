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
import android.util.Log;
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

public class Events extends Fragment {
    private static AdapterUniversal adapter;
    private static ArrayList<ListItem> data;
    private static ArrayList<Integer> removedItems;
    public RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private Document htmlDocumentZG;
    private Document htmlDocumentDEPMS;
    private Document htmlDocumentGZ;
    private DBHelper dbhelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("События");
        myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //http://znaigorod.ru/afisha
        //http://www.depms.ru/Events
        //https://gorodzovet.ru/tomsk/
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        removedItems = new ArrayList<>();

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
            TextView textView = vh.itemView.findViewById(R.id.textViewName);
            String title = (String) textView.getText();

            Log.d("title события", title);

            EventPageWebview fragment = new EventPageWebview();
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
            htmlDocumentZG = null;
            htmlDocumentDEPMS = null;
            htmlDocumentGZ = null;

            try {
                //коннектимся к сайтам
                htmlDocumentZG = Jsoup.connect("http://znaigorod.ru/afisha  ").get();
                htmlDocumentDEPMS = Jsoup.connect("http://www.depms.ru/Events").get();
                htmlDocumentGZ = Jsoup.connect("https://gorodzovet.ru/tomsk/").get();

                //считываем ЗНАЙ ГОРОД
                Elements namesZG = htmlDocumentZG.getElementsByAttributeValue("class", "title");
                Elements datesZG = htmlDocumentZG.getElementsByAttributeValue("class", "human_when");
                Elements imagesZG = htmlDocumentZG.getElementsByAttributeValue("class", "image");
                Elements placesZG = htmlDocumentZG.getElementsByAttributeValue("class", "place");

                //считываем DEPMS
                Elements namesDEPMS = htmlDocumentDEPMS.getElementsByAttributeValue("class", "name");
                Elements datesDEPMS = htmlDocumentDEPMS.getElementsByAttributeValue("class", "date");
                Elements placesDEPMS = htmlDocumentDEPMS.getElementsByAttributeValue("class", "dashed");

                //считываем ГОРОД ЗОВЕТ
                Elements namesGZ = htmlDocumentGZ.getElementsByAttributeValue("class", "event-block-name");
                Elements datesGZ = htmlDocumentGZ.getElementsByAttributeValue("class", "innlink link event-block-date-link");
                Elements imagesGZ = htmlDocumentGZ.getElementsByAttributeValue("class", "lazy event-block-img");
                Elements placesGZ = htmlDocumentGZ.getElementsByAttributeValue("class", "event-block-desciption");

                final Integer ALL_NUMBER = namesZG.size() + namesDEPMS.size() + namesGZ.size();
                //final Integer ALL_NUMBER = 100;

                name = new String[ALL_NUMBER];
                time = new String[ALL_NUMBER];
                date = new String[ALL_NUMBER];
                imageUrl = new String[ALL_NUMBER];
                addressUrl = new String[ALL_NUMBER];
                place = new String[ALL_NUMBER];

                //забиваем ZG
                int name_ZG_ind = 0, date_ZG_ind = 0, image_ZG_ind = 0, place_ZG_ind = 0;

                for(Element nameZG : namesZG)
                {
                    name[name_ZG_ind] = nameZG.select("a").text();
                    addressUrl[name_ZG_ind] = "http://znaigorod.ru" + nameZG.select("a").attr("href");
                    name_ZG_ind++;
                }

                for(Element placeZG : placesZG) {
                    place[place_ZG_ind] = placeZG.select("abbr").text();
                    place_ZG_ind++;
                }

                for(Element imageZG : imagesZG)
                {
                    imageUrl[image_ZG_ind] = imageZG.select("a").select("img").attr("src");
                    image_ZG_ind++;
                }

                for(Element dateZG : datesZG)
                {
                    date[date_ZG_ind] = "";
                    time[date_ZG_ind] = datesZG.text();
                    date_ZG_ind++;
                }

                //забиваем GZ
                int name_GZ_ind = name_ZG_ind, date_GZ_ind = date_ZG_ind, image_GZ_ind = image_ZG_ind, place_GZ_ind = place_ZG_ind;

                for(Element nameGZ : namesGZ)
                {
                    name[name_GZ_ind] = nameGZ.select("a").text();
                    addressUrl[name_GZ_ind] = "https://gorodzovet.ru" + nameGZ.select("a").attr("href");
                    name_GZ_ind++;
                }

                for(Element placeGZ : placesGZ) {
                    place[place_GZ_ind] = placeGZ.text();
                    place_GZ_ind++;
                }

                for(Element imageGZ : imagesGZ)
                {
                    imageUrl[image_GZ_ind] = "https:" + imageGZ.attr("data-src");
                    image_GZ_ind++;
                }

                for(Element dateGZ : datesGZ)
                {
                    date[date_GZ_ind] = dateGZ.text();
                    time[date_GZ_ind] = "";
                    date_GZ_ind++;
                }

                //забиваем DEPMS
                int name_DEPMS_ind = name_GZ_ind, date_DEPMS_ind = date_GZ_ind, place_DEPMS_ind = place_GZ_ind;

                for (Element placeDEPMS : placesDEPMS)
                {
                    place[place_DEPMS_ind] = placeDEPMS.text();
                    place_DEPMS_ind++;
                }

                for (Element nameDEPMS : namesDEPMS)
                {
                    name[name_DEPMS_ind] = nameDEPMS.select("a").text();
                    addressUrl[name_DEPMS_ind] = "http://www.depms.ru" + nameDEPMS.select("a").attr("href");
                    imageUrl[name_DEPMS_ind] = "http://www.depms.ru/Content/Images/logo.png";
                    name_DEPMS_ind++;
                }

                for (Element dateDEPMS : datesDEPMS)
                {
                    time[date_DEPMS_ind] = "";
                    date[date_DEPMS_ind] = datesDEPMS.text();
                    date_DEPMS_ind++;
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void insertEvent() {
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
                sqLiteDatabase.insert(DBHelper.TABLE_NAME_3, null, contentValue);
            }
        }

        public void readEvent() {
            Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_3, null, null, null, null, null, null);
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
            insertEvent();
            readEvent();
            mDialog.dismiss();
        }
    }
}
