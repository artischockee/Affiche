package com.example.alexe.affiche;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class EventPageWebview extends Fragment {
    private WebView webView;
    private String url;
    private DBHelper dbhelper;
    private SQLiteDatabase sqLiteDatabase;
    private String story;
    private String title;

    public String searchChildren(String title) {
        Context context = getActivity();
        dbhelper = new DBHelper(context);
        sqLiteDatabase = dbhelper.getWritableDatabase();

        Log.w("EPW", "function started");
        Log.w("EPW", title);
        String url = "";

        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_3, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE_1);
                int urlIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS_1);

                if (title.equals(cursor.getString(titleIndex)))
                {
                    url = cursor.getString(urlIndex);
                    Log.d("found", "url" + url);
                }
            } while (cursor.moveToNext());
        }
        dbhelper.close();
        sqLiteDatabase.close();
        return url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");

        Log.w("EPW", "fragment opened");

        getActivity().findViewById(R.id.imageButton).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
        getActivity().findViewById(R.id.imageButton2).setVisibility(View.GONE);
        getActivity().findViewById(R.id.imageButton3).setVisibility(View.GONE);
        getActivity().findViewById(R.id.imageButton4).setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        url = searchChildren(title);

        View view =  inflater.inflate(R.layout.fragment_event_page_webview, container, false);
        webView = (WebView) view.findViewById(R.id.webView);


        webView.loadUrl(url);

//        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
//        jsoupAsyncTask.execute();
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

        return view;
    }

//    class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            Document htmlDocument = null;
//
//            try {
//                htmlDocument = Jsoup.connect(url).get();
//                story = htmlDocument.getElementsByAttributeValue("class", "story").html();
//                title = htmlDocument.getElementsByAttributeValue("class", "film-title").html();
//                title = "<h2>" + title + "</h2>";
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            String html = "<html><body>" + title + story + "</body></html>";
//            webView.loadData(html, "text/html", "en_US");
//        }
//    }
}
