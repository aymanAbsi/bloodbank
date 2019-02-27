package com.example.app.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences spp= PreferenceManager.getDefaultSharedPreferences(this);
    boolean ch = spp.getBoolean("booll",false);

    if(ch) {


      Intent i = new Intent(splash.this, MainScreen.class);
        startActivity(i);
        finish();


    }
else {

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this, login.class);
                startActivity(i);
                finish();
        
            }
        }, 3000);


        new CDialog(this).createAlert("B L O O D - B A N K",
                CDConstants.ERROR,   // Type of dialog
                CDConstants.LARGE)    //  size of dialog
                .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                .setDuration(3000)   // in milliseconds
                .setTextSize(CDConstants.EXTRA_LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                .show();

    }
    }


}
