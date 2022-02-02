package com.uniba.capitool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.CardZona;
import com.uniba.capitool.classes.CardZonaAdapter;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;

import java.util.ArrayList;
import java.util.List;


public class EliminaZone extends AppCompatActivity {

    RecyclerView eliminaZoneRecycler;
    CardZonaAdapter cardZonaAdapter;
    SitoCulturale sito;
    private CardZonaAdapter adapter;
    private ArrayList<CardZona> listaZoneChecked;
    private ArrayList<CardZona> listaZone;
    List<AllZona> zoneSito;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimina_zone);

        eliminaZoneRecycler = findViewById(R.id.recyclerViewEliminaOpere);
        Bundle dati = getIntent().getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Elimina Zone");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(dati!=null){
            sito = (SitoCulturale) dati.getSerializable("sito");
            zoneSito = (List<AllZona>) dati.getSerializable("zone");
            listaZone = new ArrayList<>();
            for(int i=0; i<zoneSito.size(); i++){
                CardZona cardZona = new CardZona(zoneSito.get(i).getId(), zoneSito.get(i).getNomeZona(), sito.getId());
                listaZone.add(cardZona);
            }

            RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
            eliminaZoneRecycler.setLayoutManager(layoutManager);

            cardZonaAdapter=new CardZonaAdapter(listaZone, this);
            eliminaZoneRecycler.setAdapter(cardZonaAdapter);

        }else{
            Log.e("Elimina Zone", "Nessun Bundle trovato");
        }




    }
}