package com.uniba.capitool.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.uniba.capitool.R;

public class StartLogo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_logo);

        //display the logo during 5 seconds,
        new CountDownTimer(2500,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                //set the new Content of your activity
                Intent login = new Intent(StartLogo.this, Login.class);
                Bundle transazione= ActivityOptions.makeSceneTransitionAnimation(StartLogo.this).toBundle();
                startActivity(login, transazione);
            }
        }.start();
    }
}