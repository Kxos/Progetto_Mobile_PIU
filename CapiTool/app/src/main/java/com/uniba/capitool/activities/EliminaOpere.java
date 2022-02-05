package com.uniba.capitool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.CardOpera;
import com.uniba.capitool.classes.CardOperaAdapter;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EliminaOpere extends AppCompatActivity {

    RecyclerView eliminaOpereRecycler;
    CardOperaAdapter cardOperaAdapter;
    SitoCulturale sito;
    Button btnElimina;
    Utente utente;
    private ArrayList<CardOpera> listaOpere = new ArrayList<>();
    AllZona allZone;
    String idZona;
    String nomeZona;
    boolean spostareOpera;


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

            idZona = dati.getString("idZona");
            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");
            nomeZona=  dati.getString("nomeZona");
            allZone= (AllZona) dati.getSerializable("allZona");


            for(int i = 0; i< allZone.getListaOpereZona().size(); i++){

                CardOpera cardOpera = new CardOpera(allZone.getListaOpereZona().get(i).getId(), allZone.getListaOpereZona().get(i).getTitolo(), allZone.getListaOpereZona().get(i).getIdFoto());
                cardOpera.setCheckBox(new CheckBox(this));
                cardOpera.setCheckBoxCheckedStatus(false);

                listaOpere.add(cardOpera);

            }

            RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
            eliminaOpereRecycler.setLayoutManager(layoutManager);

            cardOperaAdapter = new CardOperaAdapter(listaOpere);
            eliminaOpereRecycler.setAdapter(cardOperaAdapter);


        }else{

        }

        btnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminaOpereChecked(cardOperaAdapter.getListaOpereChecked());
                Toast.makeText((EliminaOpere.this).getApplicationContext(), getString(R.string.artworkDeletedSuccesful), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void eliminaOpereChecked(ArrayList<CardOpera> listaOpereChecked){

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef=database.getReference("/");
        Map<String, Object> map = new HashMap<>();

        spostareOpera=false;

        int sizeListaOpere=allZone.getListaOpereZona().size();

        for(int i=0; i<listaOpereChecked.size(); i++){

            map.put("/Siti/"+sito.getId()+"/Zone/"+ allZone.getId()+"/Opere/"+listaOpereChecked.get(i).getId(), null);

            /***
             * Aggiornamento della lista offline
             */
            for(int j=0; j<allZone.getListaOpereZona().size(); j++){

                if(allZone.getListaOpereZona().get(j).getId().equals(listaOpereChecked.get(i).getId())){
                    allZone.getListaOpereZona().remove(j);
                }
            }

             if(allZone.getListaOpereZona().size()!=1 && listaOpereChecked.get(i).getId().equals("0")){
               spostareOpera=true;
            }
        }

        /***
         * Se l'utente vuole eliminare tutte le opere non devo spostare in posizione 0 nessuna
         */
        if(sizeListaOpere==listaOpereChecked.size()){
            spostareOpera=false;
        }

        myRef.updateChildren(map);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(spostareOpera==true){
                    DatabaseReference myRef2=database.getReference("/");

                    Query recentPostsQuery = myRef2.child("Siti").child(sito.getId()).child("Zone").child(allZone.getId()).child("Opere").orderByChild("id").limitToFirst(1);     //SELECT * FROM Utenti WHERE ruolo="visitatore"
                    recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Opera operaDaSposatare= new Opera();

                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    operaDaSposatare=snapshot.getValue(Opera.class);
                                }


                            String vecchioID=operaDaSposatare.getId();
                            operaDaSposatare.setId("0");

                            DatabaseReference myRef3=database.getReference("Siti/"+ sito.getId() + "/Zone/" + allZone.getId() + "/Opere/0");
                            myRef3.setValue(operaDaSposatare);

                            myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    DatabaseReference myRef4=database.getReference("Siti/"+ sito.getId() + "/Zone/" + allZone.getId() + "/Opere/"+vecchioID);
                                    myRef4.removeValue();

                                    myRef4.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Intent backVisualizzaZona= new Intent(EliminaOpere.this, VisualizzaZona.class);
                                            Bundle dati = new Bundle();
                                            dati.putSerializable("sito", sito);
                                            dati.putSerializable("utente", utente);
                                            dati.putString("nomeZona", nomeZona);
                                            dati.putString("idZona", allZone.getId());
                                            dati.putSerializable("allZone", allZone);
                                            backVisualizzaZona.putExtras(dati);
                                            finish();
                                            startActivity(backVisualizzaZona);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Intent backVisualizzaZona= new Intent(EliminaOpere.this, VisualizzaZona.class);
                    Bundle dati = new Bundle();
                    dati.putSerializable("sito", sito);
                    dati.putSerializable("utente", utente);
                    dati.putString("nomeZona", nomeZona);
                    dati.putString("idZona", allZone.getId());
                    dati.putSerializable("allZone", allZone);
                    backVisualizzaZona.putExtras(dati);
                    finish();
                    startActivity(backVisualizzaZona);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}