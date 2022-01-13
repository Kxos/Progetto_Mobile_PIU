package com.uniba.capitool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;

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

public class VisualizzaZoneSito extends AppCompatActivity{

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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        leggiZoneFromFirebase();

    }

    private void leggiZoneFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
       //TODO mettere l'id del sito nel path e togliere 1
        DatabaseReference myRef = database.getReference("/Siti/1");

        Query recentPostsQuery = myRef.child("Zone");     //SELECT * FROM Utenti WHERE ruolo="visitatore"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<AllZone> allZoneList=new ArrayList<>();
                List<Zona> zone=new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i risultati della query e li formatta esattamente come se fossero oggetti di classe Zona
                    Zona zona=snapshot.getValue(Zona.class);
                    zone.add(zona);
                }

                /***
                 * Passo gli oggetti salvati nella lista zone, nella lista allZoneList
                 * Il primo for scorre le zone
                 * Il secondo for scorre tutte le opere della relativa zona
                 * Salto il salvataggio della prima opera perchè è null
                 */
                for(int i=0; i<zone.size(); i++){
                    int contatore=0;
                    List<ItemOperaZona> listaOpereZona = new ArrayList<>();
                    for(Opera opera: zone.get(i).getOpere()){
                        if(contatore==0){
                            Log.e("SKIP", "Skip dell'opera null");
                        }else{
                            listaOpereZona.add(new ItemOperaZona(opera.getId(), opera.getTitolo(), opera.getDescrizione(), zone.get(i).getId()));
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

    /***
     * Effettua la creazione della recycler view innsetata e andrà a inserire tutti gli oggetti presenti nella lista allZoneList
     * @param allZoneList
     */
    private void setZoneRecycler(List<AllZone> allZoneList){
        mainZoneRecycler = findViewById(R.id.main_recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        mainZoneRecycler.setLayoutManager(layoutManager);

        mainRecyclerAdapter=new MainRecyclerAdapter(this, allZoneList);
        mainZoneRecycler.setAdapter(mainRecyclerAdapter);

    }

    /***
     * Istanzia nella toolbar il kebab menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.kebab_menu_visualizza_zone, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.aggiungiZona:
                Log.e("ITEMSELECTED", "AGGIUNGI ZONA");
                break;

            case R.id.eliminaZone:
                Intent eliminaZone = new Intent(this, EliminaZone.class);
                startActivity(eliminaZone);
                break;

            case R.id.ordinaZone:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}