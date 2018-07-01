package com.example.alexe.affiche;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceControl extends AppCompatActivity implements View.OnClickListener {
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        speakButton = findViewById(R.id.button);
//        speakButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startSpeak();
    }

    public void startSpeak() {
        Intent intent =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getAvailableLocales());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Произнесите команду");

        try {
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(VoiceControl.this, "Ваше устройство не поддерживает голосовое управление", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != VOICE_RECOGNITION_REQUEST_CODE || resultCode != RESULT_OK || data == null)
            return;

        ArrayList<String> commandList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

//        speakButton.setText(commandList.toString());

        if (commandList.contains("goodwin") || commandList.contains("гудвин")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://goodwincinema.ru/affiche/"));
            startActivity(browserIntent);
        }

        if (commandList.contains("tusur") || commandList.contains("тусур")
                || commandList.contains("сайт tusur")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tusur.ru"));
            startActivity(browserIntent);
        }

        if (commandList.contains("выйти") || commandList.contains("выход"))
            finish();

        if (commandList.contains("карты")) {
            Intent maps = new Intent(Intent.ACTION_VIEW);
            maps.setPackage("com.google.android.apps.maps");
            startActivity(maps);
        }
    }
}
