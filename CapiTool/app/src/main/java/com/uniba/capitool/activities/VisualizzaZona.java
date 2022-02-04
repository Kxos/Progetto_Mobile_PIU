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
import androidx.recyclerview.widget.RecyclerView;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.MainRecyclerAdapter;

import java.io.Serializable;

public class VisualizzaZona extends AppCompatActivity {

    private String nuovaZona = "";
    RecyclerView mainZoneRecycler;
    MainRecyclerAdapter mainRecyclerAdapter;
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

        // mi serve per capire se aggiornare la recycler se ci sono state delle modifiche o se tornare semplicemente indietro

        //GridView gridView= findViewById(R.id.myGrid);
//        gridView.setAdapter(new ImageAdapter(imageList, context));
        Bundle dati = getIntent().getExtras();


        if(dati!=null){
            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");
            //nomeZona= dati.getString("nomeZona");
            allZone = (AllZona) dati.getSerializable("allZone");
            nomeZona= allZone.getNomeZona();


          //  GridView gridView= findViewById(R.id.myGrid);
          //  gridView.setAdapter(new ImageAdapter(allZone.getListaOpereZona(), this));
           // riempiGriglia();


            Log.e("***********", ""+utente.getUid()+" / "+sito.getNome()+"/  "+ allZone.getId());

//            for(int i=0; i<allZone.getListaOpereZona().size(); i++){
//                Log.e("***********", ""+allZone.getListaOpereZona().get(i).getTitolo());
//            }

            if(allZone.getListaOpereZona()==null){
                Log.e("Visulizza Zone Sito", "VUOTOOOOOOO");
                //TODO inserire un messaggio per l'utente
                RelativeLayout emptyState=findViewById(R.id.layoutEmptyStateZona);
                emptyState.setVisibility(View.VISIBLE);

            }else{
                riempiGriglia();
            }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("Hai cliccato l'opera", ""+allZone.getListaOpereZona().get(position).getTitolo());

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
            Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }

//        GridView gridView= findViewById(R.id.myGrid);
//       gridView.setAdapter(new ImageAdapter(allZ, context));

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

//        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                recreate();
//                pullToRefresh.setRefreshing(false);
//            }
//        });
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.e("OnResume",""+1);
////        finish();
////        startActivity(getIntent());
//
//        riempiGriglia();
//    }
}