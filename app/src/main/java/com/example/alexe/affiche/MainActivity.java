package com.example.alexe.affiche;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Toolbar tb;
    private ProgressDialog mDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportActionBar().setTitle(R.string.title_home);
                    findViewById(R.id.editSearch).setVisibility(View.GONE);
                    findViewById(R.id.imageButton).setVisibility(View.GONE);
                    findViewById(R.id.imageButton2).setVisibility(View.GONE);
                    findViewById(R.id.imageButton3).setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    getSupportActionBar().setTitle(R.string.title_dashboard);
                    findViewById(R.id.imageButton).setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frgmCont, new FragmentCategory())
                            .commit();
                    findViewById(R.id.editSearch).setVisibility(View.GONE);
                    findViewById(R.id.imageButton2).setVisibility(View.GONE);
                    findViewById(R.id.imageButton3).setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notification:
                    getSupportActionBar().setTitle("");
                    findViewById(R.id.imageButton).setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frgmCont, new Search())
                            .commit();
                    findViewById(R.id.editSearch).setVisibility(View.VISIBLE);
                    findViewById(R.id.imageButton2).setVisibility(View.GONE);
                    findViewById(R.id.imageButton3).setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    public void onClick(View v) {
        if (v.getId() == R.id.imageButton4)
        {
            mDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
            mDialog.setMessage("Идет загрузка");
            mDialog.show();

            WebView webView = findViewById(R.id.webView);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            MyWebViewClient myWebViewClient = new MyWebViewClient();
            myWebViewClient.shouldOverrideUrlLoading(webView, "http://goodwincinema.ru/schedule/");
            webView.setWebViewClient(new MyWebViewClient());
            findViewById(R.id.toolbar).setVisibility(View.GONE);
            findViewById(R.id.imageButton4).setVisibility(View.GONE);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);

            getFragmentManager().beginTransaction().addToBackStack(null);
        }
        else if (v.getId() == R.id.imageButton || v.getId() == R.id.fab)
        {
            onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frgmCont, new FragmentCategory())
                .commit();

        findViewById(R.id.imageButton2).setVisibility(View.GONE);

        tb = findViewById(R.id.toolbar);

        setSupportActionBar(tb);
        getSupportActionBar().setTitle(R.string.title_dashboard);
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fragManager = getSupportFragmentManager();
        int count = fragManager.getBackStackEntryCount();
        Fragment currentFrag =  getSupportFragmentManager().findFragmentById(R.id.frgmCont);
        String fragName = currentFrag.getClass().getSimpleName();
        if (count != 0) {
            if ((fragName.equals("CinemaPageWebview")
                    || fragName.equals("TheaterPageWebview")
                    || fragName.equals("EventPageWebview")
                    || fragName.equals("ChildrenPageWebview"))){
                findViewById(R.id.imageButton4).setVisibility(View.GONE);
                findViewById(R.id.imageButton).setVisibility(View.GONE);
                findViewById(R.id.imageButton2).setVisibility(View.VISIBLE);
                findViewById(R.id.imageButton3).setVisibility(View.VISIBLE);
                findViewById(R.id.navigation).setVisibility(View.VISIBLE);
            }

            fragManager.popBackStack();
            return;
        }

        super.onBackPressed();
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            mDialog.dismiss();
        }
    }
}