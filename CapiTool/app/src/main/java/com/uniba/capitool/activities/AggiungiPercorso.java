package com.uniba.capitool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.Visitatore;

public class AggiungiPercorso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_percorso);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Aggiungi Percorso");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        Visitatore utente;

        if(BasicMethod.isCuratore(b.getString("ruolo"))){
            utente = new Visitatore();
        }else{
            utente = new Curatore();
        }

        utente.setUid(b.getString("uid"));
        utente.setNome(b.getString("nome"));
        utente.setCognome(b.getString("cognome"));
        utente.setEmail(b.getString("email"));
        utente.setRuolo(b.getString("ruolo"));

    }
}