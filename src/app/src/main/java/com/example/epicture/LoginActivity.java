package com.example.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Locale;


public class LoginActivity extends AppCompatActivity {

    Intent connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String url = getString(R.string.urlLogin) + "&response_type=token";
        Uri uri = Uri.parse(url);
        connect = new Intent(Intent.ACTION_VIEW, uri);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(connect);
            }
        });
        ImageButton buttonfr = findViewById(R.id.français_language);
        buttonfr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String languageToLoad = "fr";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                finish();
                startActivity(getIntent());
            }
        });
        ImageButton buttoneng = findViewById(R.id.espagnol_language);
        buttoneng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String languageToLoad = "es";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                finish();
                startActivity(getIntent());
            }
        });
        ImageButton buttonesp = findViewById(R.id.anglais_language);
        buttonesp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String languageToLoad = "en";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                finish();
                startActivity(getIntent());
            }
        });
    }
}
