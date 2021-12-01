package com.example.musei_musei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_login;
        button_login = findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Scrive un messaggio sulla console
                Log.d("Risultato","Hai cliccato il pulsante");

                // Passaggio da una Activity ad un altra
                String value = "Sei entrato";
                Intent myIntent = new Intent(MainActivity.this, SignIn.class);
                myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);

            }
        });

    }
}