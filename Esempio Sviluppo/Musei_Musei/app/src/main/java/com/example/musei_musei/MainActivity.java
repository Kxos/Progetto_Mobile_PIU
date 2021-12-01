package com.example.musei_musei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_login;
        button_login = findViewById(R.id.button_login);

        EditText email;
        email = findViewById(R.id.textfield_email);

        EditText password;
        password = findViewById(R.id.textfield_password);

        ImageView email_error_icon, password_error_icon;
        email_error_icon = findViewById(R.id.email_error_icon);
        password_error_icon = findViewById(R.id.password_error_icon);

        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Scrive un messaggio sulla console
                Log.d("Risultato","Hai cliccato il pulsante");

                Log.d("Email", email.getText().toString());
                Log.d("Password", password.getText().toString());

                CharSequence email_value = email.getText().toString();
                CharSequence password_value = password.getText().toString();

                // Controllo Credenzali
                if (isEmailValid(email_value) && isPasswordValid(password_value)){

                    /**
                     // Passaggio da una Activity ad un altra
                     String value = "Sei entrato";
                     Intent myIntent = new Intent(MainActivity.this, SignIn.class);
                     myIntent.putExtra("key", value); //Optional parameters
                     MainActivity.this.startActivity(myIntent);
                     */

                }else{
                    if (!isEmailValid(email_value)){
                        email_error_icon.setVisibility(View.VISIBLE);
                    }

                    if (!isPasswordValid(password_value)){
                        password_error_icon.setVisibility(View.VISIBLE);
                    }

                }

            }
        });

    }

    /***
     *
     * @param email - Email da verificare
     * @return Boolean
     */
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /***
     *
     * @param password - Password da validare
     * @return boolean
     */
    private boolean isPasswordValid(CharSequence password) {
        return true;
    }

}