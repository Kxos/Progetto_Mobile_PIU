package com.uniba.capitool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Visitatore;

import android.os.Bundle;
import android.util.Log;

public class SelezionaOpere extends AppCompatActivity {

    private SitoCulturale sitoCulturale;
    private Visitatore utente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleziona_opere);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Seleziona Opere");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();

        sitoCulturale = new SitoCulturale();

        if(BasicMethod.isCuratore(b.getString("ruoloUtente"))){
            utente = new Visitatore();
        }else{
            utente = new Curatore();
        }

        utente.setUid(b.getString("uidUtente"));
        utente.setRuolo(b.getString("ruoloUtente"));

        sitoCulturale.setId(b.getString("idSito"));
        sitoCulturale.setNome(b.getString("nomeSito"));
        sitoCulturale.setOrarioApertura(b.getString("orarioAperturaSito"));
        sitoCulturale.setOrarioChiusura(b.getString("orarioChiusuraSito"));
        sitoCulturale.setIndirizzo(b.getString("indirizzoSito"));
        sitoCulturale.setCostoBiglietto(b.getFloat("costoBigliettoSito"));
        sitoCulturale.setCitta(b.getString("cittaSito"));

        Log.e("DATI PASSATI: ",utente.getUid() + " " + sitoCulturale.getId() + " " + sitoCulturale.getNome());

    }
}