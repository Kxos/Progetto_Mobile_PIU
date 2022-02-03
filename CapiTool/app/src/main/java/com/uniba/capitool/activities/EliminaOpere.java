package com.uniba.capitool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.CardOpera;
import com.uniba.capitool.classes.CardOperaAdapter;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;

import java.util.ArrayList;
import java.util.concurrent.CancellationException;

public class EliminaOpere extends AppCompatActivity {

    RecyclerView eliminaOpereRecycler;
    CardOperaAdapter cardOperaAdapter;
    SitoCulturale sito;
    Button btnElimina;
    Utente utente;
    private CardOperaAdapter adapter;
    private ArrayList<CardOpera> listaOpere = new ArrayList<>();
    AllZona zona;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimina_opere);

        btnElimina = findViewById(R.id.eliminaZoneSelezionate);
        eliminaOpereRecycler = findViewById(R.id.recyclerViewEliminaOpere);
        Bundle dati = getIntent().getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.deleteOpera));
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
            utente = (Utente) dati.getSerializable("utente");
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

            cardOperaAdapter = new CardOperaAdapter(listaOpere);
            eliminaOpereRecycler.setAdapter(cardOperaAdapter);


        }else{
            Log.e("Elimina Opere", "Nessun Bundle trovato");
        }

        btnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //eliminaOpereChecked(cardOperaAdapter.getListaOpereChecked());
                Toast.makeText((EliminaOpere.this).getApplicationContext(), getString(R.string.artworkDeletedSuccesful), Toast.LENGTH_SHORT).show();
                EliminaOpere.super.onBackPressed();
            }
        });




    }

    public void eliminaOpereChecked(ArrayList<CardOpera> listaOpereChecked){
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef;

        for(int i=0; i<listaOpereChecked.size(); i++){
            String percorsoDatabase = ("/Siti/"+sito.getId()+"/Zone/"+zona.getId()+"/Opere/"+listaOpereChecked.get(i).getId());
            Log.e("PERCORSO DATABASE ELIMINA OPERE:",""+percorsoDatabase);
            myRef = database.getReference(percorsoDatabase);

            myRef.removeValue();
        }
    }
}