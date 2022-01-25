package com.uniba.capitool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.MainRecyclerAdapter;

public class VisualizzaZona extends AppCompatActivity {

    private String nuovaZona = "";
    RecyclerView mainZoneRecycler;
    MainRecyclerAdapter mainRecyclerAdapter;
    SitoCulturale sito;
    Utente utente;
    String nomeZona;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_zona);


        // mi serve per capire se aggiornare la recycler se ci sono state delle modifiche o se tornare semplicemente indietro

        //GridView gridView= findViewById(R.id.myGrid);
//        gridView.setAdapter(new ImageAdapter(imageList, context));
        Bundle dati = getIntent().getExtras();


        if(dati!=null){
            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");
            //nomeZona= dati.getString("nomeZona");
           AllZona allZone = (AllZona) dati.getSerializable("allZone");
            nomeZona= allZone.getNomeZona();
            GridView gridView= findViewById(R.id.myGrid);
            gridView.setAdapter(new ImageAdapter(allZone.getListaOpereZona(), this));



            Log.e("***********", ""+utente.getUid()+" / "+sito.getNome()+"/  "+ allZone.getId());

//            for(int i=0; i<allZone.getListaOpereZona().size(); i++){
//                Log.e("***********", ""+allZone.getListaOpereZona().get(i).getTitolo());
//            }

            if(allZone.getListaOpereZona()==null){
                Log.e("Visulizza Zone Sito", "VUOTOOOOOOO");
                //TODO inserire un messaggio per l'utente
            }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("Hai cliccato l'opera", ""+allZone.getListaOpereZona().get(position).getTitolo());
                    Bundle opera = new Bundle();
                    opera.putSerializable("opera",allZone.getListaOpereZona().get(position));
                    opera.putString("idSito",sito.getId());
                    Intent visualizzaModificaOpera = new Intent(VisualizzaZona.this,VisualizzaModificaOpera.class);
                    visualizzaModificaOpera.putExtras(opera);
                    startActivity(visualizzaModificaOpera);
                }
            });
        }else{
            Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }

//        GridView gridView= findViewById(R.id.myGrid);
//       gridView.setAdapter(new ImageAdapter(allZ, context));

        Toolbar toolbar = findViewById(R.id.toolbarVisualizzaZona);
        toolbar.setTitle("Zona "+ nomeZona);
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

                break;

            case R.id.eliminaOpere:


                break;

            case R.id.rinominaZona:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}