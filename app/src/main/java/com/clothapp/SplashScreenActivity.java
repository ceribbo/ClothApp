package com.clothapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.clothapp.resources.ApplicationSupport;
import com.clothapp.resources.Image;
import com.clothapp.resources.ImageGridViewAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.clothapp.resources.ExceptionCheck.check;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // nascondo la status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        /**
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */

        //qui scarico le foto
        final View vi = new View(this.getApplicationContext());
        final List<Image> photo = new ArrayList<>();

        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Photo");
        query.setLimit(10);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> fotos, ParseException e) {
                if (e == null) {
                    Log.d("Query", "Retrieved " + fotos.size() + " photos");

                    int i;
                    for (i = 0; i < 10; i++) {
                        ParseObject obj = fotos.get(i);
                        ParseFile file = obj.getParseFile("photo");
                        try {
                            //inserisco le foto in una lista che poi setto come variabile globale nella ApplicationSupport
                            photo.add(new Image(file.getFile(), obj.getObjectId()));
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    }
                    Intent intent = new Intent(getBaseContext(), HomepageActivity.class);
                    //prendo la variabile globale photos e ci metto dentro le immagini
                    ApplicationSupport photos = ((ApplicationSupport) getApplicationContext());
                    photos.setPhotos(photo);
                    startActivity(intent);
                    finish();
                } else {
                    //errore nel reperire gli oggetti Photo dal database
                    check(e.getCode(), vi, e.getMessage());
                }
            }
        });
        Log.d("SplashScreenActivity", "Finito il prefetch dei dati.");
    }

}

