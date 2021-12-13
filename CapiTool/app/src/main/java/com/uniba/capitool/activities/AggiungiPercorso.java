package com.uniba.capitool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.CardSitoCulturale;
import com.uniba.capitool.classes.CardSitoCulturaleAdapter;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.Visitatore;

import java.util.ArrayList;

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

        // Lookup the recyclerview in activity layout
        RecyclerView rvCardsSiti = (RecyclerView) findViewById(R.id.recyclerViewSiti);

        // Initialize contacts
        ArrayList<CardSitoCulturale> cardSitiCulturali = new ArrayList<>();

        // TODO - GET SITI DA DATABASE
        // TODO ----------------------------------------------------------
        CardSitoCulturale cardSitoCulturale = new CardSitoCulturale();
        cardSitoCulturale.setNome("Sito di Prova 1");
        cardSitoCulturale.setDescrizione("Non è importante al momento");
        cardSitoCulturale.setCitta("Bari");
        cardSitiCulturali.add(cardSitoCulturale);
        // TODO ----------------------------------------------------------

        // Crea un adapter passando i siti trovati
        CardSitoCulturaleAdapter adapter = new CardSitoCulturaleAdapter(cardSitiCulturali);

        // Attach the adapter to the recyclerview to populate items
        rvCardsSiti.setAdapter(adapter);
        // Set layout manager to position the items
        rvCardsSiti.setLayoutManager(new LinearLayoutManager(this));

    }
}