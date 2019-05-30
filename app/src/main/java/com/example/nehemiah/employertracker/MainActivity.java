package com.example.nehemiah.employertracker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
 private  static int splash_Screen_time_out =1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating a handler that fire a thread
        //start login activiy after 4 seconds
        //kill the mainactivity(splash screen)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,LoginScreen.class));
                finish();
            }
        },splash_Screen_time_out);
    }
}
