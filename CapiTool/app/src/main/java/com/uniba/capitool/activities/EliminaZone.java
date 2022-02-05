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
    Button btnElimina;
    private ArrayList<CardZona> listaZone;
    List<AllZona> zoneSito;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimina_zone);

        btnElimina = findViewById(R.id.eliminaZoneSelezionate);
        eliminaZoneRecycler = findViewById(R.id.recyclerViewEliminaOpere);

        Bundle dati = getIntent().getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.deleteZoneToolbar));
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
                View cardZonaView = getLayoutInflater().inflate(R.layout.card_zona, null);
                cardZona.setCheckBox(cardZonaView.findViewById(R.id.checkZonaSelezionata));
                cardZona.setCheckBoxCheckedStatus(false);
                listaZone.add(cardZona);
            }

            RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
            eliminaZoneRecycler.setLayoutManager(layoutManager);

            cardZonaAdapter=new CardZonaAdapter(listaZone, this);
            eliminaZoneRecycler.setAdapter(cardZonaAdapter);

        }else{

        }


        btnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminaZoneChecked(cardZonaAdapter.getListaZoneChecked());
                Toast.makeText((EliminaZone.this).getApplicationContext(), getString(R.string.zoneDeletedSuccesful), Toast.LENGTH_SHORT).show();
                EliminaZone.super.onBackPressed();
            }
        });



    }
    public void eliminaZoneChecked(ArrayList<CardZona> listaZoneChecked){
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef;

        for(int i=0; i<listaZoneChecked.size(); i++){
            String percorsoDatabase = ("/Siti/"+sito.getId()+"/Zone/"+listaZoneChecked.get(i).getId());

            myRef = database.getReference(percorsoDatabase);

            myRef.removeValue();
        }
    }
}