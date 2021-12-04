package com.uniba.capitool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.uniba.capitool.classes.Visitatore;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button_login;
        button_login = findViewById(R.id.button_login);

        EditText email;
        email = findViewById(R.id.textfield_email);

        EditText password;
        password = findViewById(R.id.textfield_password);

        ImageView email_error_icon, password_error_icon;
        email_error_icon = findViewById(R.id.email_error_icon);
        password_error_icon = findViewById(R.id.password_error_icon);

        TextView registrati;
        registrati = findViewById(R.id.textView_register);
        registrati.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        registrati.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Visitatore visit= new Visitatore();
                visit.setCognome("Nardo");
                Intent intentRegistrati = new Intent(Login.this, Registrati.class);
                intentRegistrati.putExtra("cognome",visit.getCognome());
                intentRegistrati.putExtra("nome",visit.getNome());
                Login.this.startActivity(intentRegistrati);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Scrive un messaggio sulla console
                Log.d("Risultato","Hai cliccato il pulsante");

                Log.d("Email", email.getText().toString());
                Log.d("Password", password.getText().toString());

                CharSequence email_value = email.getText().toString();
                CharSequence password_value = password.getText().toString();

                if (isEmailValid(email_value) && isPasswordValid(password_value)){
                    // Login validato

                    /**
                     // Passaggio da una Activity ad un altra
                     String value = "Sei entrato";
                     Intent myIntent = new Intent(MainActivity.this, SignIn.class);
                     myIntent.putExtra("key", value); //Optional parameters
                     MainActivity.this.startActivity(myIntent);
                     */

                }else{
                    // Errore nelle credenziali

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