package com.uniba.capitool.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uniba.capitool.classes.Curatore;

public class InfoSitoAssociato extends AppCompatActivity {
    Curatore utente = new Curatore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        if(b!=null){
            utente.setUid(b.getString("uid"));
            utente.setNome(b.getString("nome"));
            utente.setCognome(b.getString("cognome"));
            utente.setEmail(b.getString("email"));
            utente.setRuolo(b.getString("ruolo"));
        }else{
           /* BasicMethod.alertDialog(this, "C'è stato un errore nel caricare i tuoi dati, sarai riportato alla login", "Errore caricamento dati", "OK");
            Intent login= new Intent(HomePage.this, Login.class);
            this.startActivity(login);
            */
        }

//        setContentView(R.layout.activity_info_sito_associato);
    }
}