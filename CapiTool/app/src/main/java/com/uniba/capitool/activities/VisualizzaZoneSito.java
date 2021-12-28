package com.uniba.capitool.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.classes.Zona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZone;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.MainRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaZoneSito extends AppCompatActivity {

    RecyclerView mainZoneRecycler;
    MainRecyclerAdapter mainRecyclerAdapter;
    SitoCulturale sito;
    Utente utente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_zone_sito);

        Bundle dati = getIntent().getExtras();

        if(dati!=null){
            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");

            Log.e("***********", ""+utente.getUid()+" / "+sito.getNome());

        }else{
           Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Zone "+sito.getNome());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Al click dell'Hamburgher, apre il Menù laterale
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        leggiZoneFromFirebase();
/*
        List<ItemOperaZona> listaOpereZona = new ArrayList<>();
        listaOpereZona.add(new ItemOperaZona("1", "Titolo1"));
        listaOpereZona.add(new ItemOperaZona("2", "Titolo2"));
        listaOpereZona.add(new ItemOperaZona("3", "Titolo3"));
        listaOpereZona.add(new ItemOperaZona("4", "Titolo4"));

        List<AllZone> allZoneList=new ArrayList<>();
        allZoneList.add(new AllZone("Architettura", listaOpereZona));
        allZoneList.add(new AllZone("Informatica", listaOpereZona));
        allZoneList.add(new AllZone("Arte", listaOpereZona));

        setZoneRecycler(allZoneList);
*/

    }

    private void leggiZoneFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/Siti/1");

        // Query recentPostsQuery = myRef.child("Utenti").orderByChild("username").equalTo("andreax5000");

        Query recentPostsQuery = myRef.child("Zone");     //SELECT * FROM Utenti WHERE ruolo="visitatore"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              //  List<ItemOperaZona> listaOpereZona = new ArrayList<>();
                List<AllZone> allZoneList=new ArrayList<>();
                List<Zona> zone=new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i valori trovati dalla query, anche se è 1 solo
                    Zona zona=snapshot.getValue(Zona.class);
                    zone.add(zona);
                }

                for(int i=0; i<zone.size(); i++){
                    int contatore=0;
                    List<ItemOperaZona> listaOpereZona = new ArrayList<>();
                    for(Opera opera: zone.get(i).getOpere()){
                        if(contatore==0){
                            Log.e("SKIP", "SKIP");
                        }else{
                            listaOpereZona.add(new ItemOperaZona(opera.getId(), opera.getTitolo()));
                            Log.e("Opera trovata", ""+opera.getTitolo()+"/"+opera.getId());
                        }
                        contatore++;
                    }
                    allZoneList.add(new AllZone(zone.get(i).getId(), zone.get(i).getNome(), listaOpereZona));


                }

                //ciclo for per scorrere la lista ottnuta dal db
                int contatore=0;
                for(Zona visitatore : zone){
                    Log.d("Oggetto "+(contatore+1)+" della lista visitatori", ""+visitatore);
                    contatore++;
                }

                Log.d("Lunghezza lista della query", String.valueOf(zone.size()));

                setZoneRecycler(allZoneList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }

    private void setZoneRecycler(List<AllZone> allZoneList){
        mainZoneRecycler = findViewById(R.id.main_recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        mainZoneRecycler.setLayoutManager(layoutManager);

        mainRecyclerAdapter=new MainRecyclerAdapter(this, allZoneList);
        mainZoneRecycler.setAdapter(mainRecyclerAdapter);
    }
}