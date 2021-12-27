package com.uniba.capitool.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

public class VisualizzaZoneSito extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_zone_sito);

        SitoCulturale sito;
        Utente utente;

        Bundle dati = getIntent().getExtras();

        if(dati!=null){
            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");

            Log.e("***********", ""+utente.getUid()+" / "+sito.getNome());

        }else{
           Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }
    }
}