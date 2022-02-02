package com.uniba.capitool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.CardOpera;
import com.uniba.capitool.classes.CardOperaAdapter;
import com.uniba.capitool.classes.CardZona;
import com.uniba.capitool.classes.CardZonaAdapter;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;

import java.util.ArrayList;
import java.util.List;

public class EliminaOpere extends AppCompatActivity {

    RecyclerView eliminaOpereRecycler;
    CardOperaAdapter cardOperaAdapter;
    SitoCulturale sito;
    private CardOperaAdapter adapter;
    private ArrayList<CardOpera> listaOpereChecked;
    private ArrayList<CardOpera> listaOpere = new ArrayList<>();
    AllZona zona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimina_opere);

        eliminaOpereRecycler = findViewById(R.id.recyclerViewEliminaOpere);
        Bundle dati = getIntent().getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Elimina Opere");
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
            zona = (AllZona) dati.getSerializable("zona");

            Log.e("LA MADONNA CI VUOLE BENE:: PT1",""+zona.getListaOpereZona().get(0).getTitolo());


            for(int i = 0; i< zona.getListaOpereZona().size(); i++){

                CardOpera cardOpera = new CardOpera(zona.getListaOpereZona().get(i).getId(), zona.getListaOpereZona().get(i).getTitolo(), zona.getListaOpereZona().get(i).getIdFoto());
                cardOpera.setCheckBox(new CheckBox(this));
                cardOpera.setCheckBoxCheckedStatus(false);

                Log.e("LA MADONNA CI VUOLE BENE:: PT2",""+cardOpera.getTitolo());
                listaOpere.add(cardOpera);



            }

            RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
            eliminaOpereRecycler.setLayoutManager(layoutManager);

            cardOperaAdapter =new CardOperaAdapter(listaOpere);
            eliminaOpereRecycler.setAdapter(cardOperaAdapter);

        }else{
            Log.e("Elimina Opere", "Nessun Bundle trovato");
        }




    }
}