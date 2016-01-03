package com.clothapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.parse.ParseUser;

public class Homepage extends AppCompatActivity {

    // Prova Android Studio su Debian - Simone

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //  just a sample button it does nothing when clicked
        Button button_final = (Button) findViewById(R.id.final_button);

        button_final.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.final_button:
                        System.out.println("debug: premuto you won");
                        break;
                }
            }
        });

        //button upload a new photo
        FloatingActionButton upload = (FloatingActionButton) findViewById(R.id.upload_button);
        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_upload) {
                // redirecting the user to the upload activity and upload a photo
                Intent i = new Intent(getApplicationContext(), Upload.class);
                startActivity(i);
                finish();
            }
        });

        //  logout button
        Button button_logout = (Button) findViewById(R.id.form_logout_button);
        button_logout.setOnClickListener(new View.OnClickListener() {
            //metto bottone logout in ascolto del click
            @Override
            public void onClick(View view_logout) {
                // TODO Auto-generated method stub
                switch (view_logout.getId()) {
                    case R.id.form_logout_button:
                            //chiudo sessione e metto valore sharedPref a false
                            ParseUser.logOut();
                            SharedPreferences userInformation = getSharedPreferences(getString(R.string.info), MODE_PRIVATE);
                            userInformation.edit().putString("username", "").commit();
                            userInformation.edit().putString("name", "").commit();
                            userInformation.edit().putString("lastname", "").commit();
                            userInformation.edit().putString("email", "").commit();
                            userInformation.edit().putString("date", "").commit();
                            userInformation.edit().putBoolean("isLogged", false).commit();
                            System.out.println("debug: logout eseguito");
                            Intent form_intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(form_intent);
                            finish();
                        break;
                }
            }
        });
        //  bottone profilo, fintanto che non viene implementato il menu
        Button button_profile = (Button) findViewById(R.id.form_profile_button);
        button_profile.setOnClickListener(new View.OnClickListener() {
            //metto bottone profile in ascolto del click
            @Override
            public void onClick(View view_profile) {
                // TODO Auto-generated method stub
                switch (view_profile.getId()) {
                    case R.id.form_profile_button:
                        Intent form_intent = new Intent(getApplicationContext(), Profile.class);
                        startActivity(form_intent);
                        finish();
                        break;
                }
            }
        });
    }

}
