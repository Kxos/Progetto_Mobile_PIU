package com.uniba.capitool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;

import java.io.Serializable;

public class VisualizzaZona extends AppCompatActivity {

    SitoCulturale sito;
    Utente utente;
    String nomeZona;
    AllZona allZone;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_zona);
        gridView= findViewById(R.id.myGrid);

        Bundle dati = getIntent().getExtras();

        if(dati!=null){

            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");
            allZone = (AllZona) dati.getSerializable("allZone");
            nomeZona= allZone.getNomeZona();

            if(allZone.getListaOpereZona()==null || allZone.getListaOpereZona().size()==0){

                RelativeLayout emptyState=findViewById(R.id.layoutEmptyStateZona);
                emptyState.setVisibility(View.VISIBLE);

            }else{
                riempiGriglia();
            }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent visualizzaModificaOpera = new Intent(VisualizzaZona.this, VisualizzaModificaOpera.class);
                    Bundle dati = new Bundle();
                    dati.putSerializable("zona", (Serializable) allZone);
                    dati.putSerializable("opera",allZone.getListaOpereZona().get(position));
                    dati.putSerializable("opera",allZone.getListaOpereZona().get(position));
                    dati.putString("idSito",sito.getId());
                    dati.putSerializable("sito",sito);
                    dati.putSerializable("utente", utente);
                    dati.putString("nomeZona", nomeZona);
                    dati.putSerializable("allZona", allZone);
                    visualizzaModificaOpera.putExtras(dati);
                    finish();
                    startActivity(visualizzaModificaOpera);
                }
            });
        }else{

        }

        Toolbar toolbar = findViewById(R.id.toolbarVisualizzaZona);
        toolbar.setTitle(BasicMethod.setUpperPhrase(nomeZona));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    VisualizzaZona.super.onBackPressed();
            }
        });

    }

    /***
     * Istanzia nella toolbar il kebab menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.kebab_menu_visualizza_zona, menu);
              return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.aggiungiOpera:

                Intent aggiungiOpera= new Intent(VisualizzaZona.this, AggiungiOpera.class);
                Bundle dati = new Bundle();
                dati.putSerializable("sito", sito);
                dati.putSerializable("utente", utente);
                dati.putString("nomeZona", nomeZona);
                dati.putString("idZona", allZone.getId());
                dati.putSerializable("allZona", allZone);
                aggiungiOpera.putExtras(dati);
                finish();
                startActivity(aggiungiOpera);

                break;

            case R.id.eliminaOpere:

                Intent eliminaOpere = new Intent(this, EliminaOpere.class);
                Bundle datiZona = new Bundle();
                datiZona.putSerializable("sito",sito);
                datiZona.putSerializable("utente", utente);
                datiZona.putString("nomeZona", nomeZona);
                datiZona.putSerializable("allZona", allZone);
                eliminaOpere.putExtras(datiZona);
                finish();
                startActivity(eliminaOpere);

                break;

            case R.id.rinominaZona:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void riempiGriglia(){

        gridView.setAdapter(new ImageAdapter(allZone.getListaOpereZona(), this));
    }

}