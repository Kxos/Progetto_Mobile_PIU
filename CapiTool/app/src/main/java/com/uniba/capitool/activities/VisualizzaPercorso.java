package com.uniba.capitool.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.CardPercorso;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.MainRecyclerAdapterVisualizzaPercorso;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaPercorso extends AppCompatActivity {
    CardPercorso percorso;
    ImageView fotoSito;
    TextView nomeSito;
    TextView descrizionePercorso;
    List<AllZona> zoneSito;
    RecyclerView mainZoneRecycler;
    MainRecyclerAdapterVisualizzaPercorso mainRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_percorso);

        fotoSito=findViewById(R.id.fotoSito);
        nomeSito=findViewById(R.id.nomeMuseo);
        descrizionePercorso=findViewById(R.id.descrizionePercorso);



        Bundle dati = getIntent().getExtras();

        if(dati!=null){
            percorso = (CardPercorso) dati.getSerializable("percorso");

            nomeSito.setText(BasicMethod.setUpperPhrase(percorso.getNomeSitoAssociato()));
            descrizionePercorso.setText(percorso.getDescrizione());
            setImmagineSitoFromDB(percorso.getIdSitoAssociato(), this, fotoSito);
        }else{
            Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }

        Toolbar toolbar = findViewById(R.id.toolbarVisualizzaPercorso);
        toolbar.setTitle(BasicMethod.setUpperPhrase(percorso.getNome()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisualizzaPercorso.super.onBackPressed();
            }
        });


        leggiOpere(percorso.getId());
    }

    public void leggiOpere(String idPercorso){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference myRef = database.getReference("/Percorsi/"+idPercorso);

        Query recentPostsQuery = myRef.child("OpereScelte").orderByChild("idZona");
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<ItemOperaZona> listaOpere = new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i risultati della query e li formatta esattamente come se fossero oggetti di classe Zona
                    try{
                        ItemOperaZona opera=snapshot.getValue(ItemOperaZona.class);

                        /***
                         * Con questa query "annidata" vado a leggere di ogni opera il suo idFoto perché nel percorso non l'ho salvato
                         * E' come fare un join
                         */
                        FirebaseDatabase.getInstance().getReference()
                                .child("Siti").child(percorso.getIdSitoAssociato()).child("Zone").child(opera.getIdZona()).child(opera.getId()).limitToFirst(1)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            Opera opera=snapshot.getValue(Opera.class);
                                            opera.setIdFoto(opera.getIdFoto());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                        listaOpere.add(opera);
                    }catch (Exception e){
                        Log.e("Errore","C'è stato un'errore nel leggere un opera");

                    }
                }

                /***
                 * Le opere non sono salvate per zona, quindi in qualche modo le devo ordinare.
                 * In questo for mi recupero ogni id di ogni zona, così saprò quante zone ci sono
                 * e quali sono
                 * Salvo tutto nell'array indiciZone
                 */
                String idZona=listaOpere.get(0).getIdZona();
                List<String> indiciZone=new ArrayList<>();
                indiciZone.add(listaOpere.get(0).getIdZona());

                for(int i=0; i<listaOpere.size(); i++){
                    if(!idZona.equals(listaOpere.get(i).getIdZona())){
                        idZona=listaOpere.get(i).getIdZona();
                        indiciZone.add(listaOpere.get(i).getIdZona());
                    }
                }

                /***
                 * Per creare la doppia recycle view, devo salvare le opere e le zone in una lista
                 * List<AllZona> allZoneList
                 * Nel for creo una lista List<ItemOperaZona> listaOpereZona per ogni zona che ho trovato con il for precedente
                 * Scorro la lista listaOpere che contiene tutte le opere del percorso in modo sparso e le salvo in listaOpereZona
                 * che conterrà le opere solo di quella zona
                 * Alla fine del ciclo salvo la lista creata (listaOpereZona) in allZoneList
                 * Quindi ora in allZoneList in posizione i-esima ci saranno tutte le opere relative alla zona i                 *
                 */
                List<AllZona> allZoneList=new ArrayList<>();

                for(int i=0; i<indiciZone.size(); i++){
                    List<ItemOperaZona> listaOpereZona=new ArrayList<>();
                    for(int j=0; j<listaOpere.size(); j++){
                        if(listaOpere.get(j).getIdZona().equals(indiciZone.get(i))){
                            listaOpereZona.add(listaOpere.get(j));
                        }
                    }
                    allZoneList.add(new AllZona(indiciZone.get(i), indiciZone.get(i), listaOpereZona));
                }

                /***
                 * Il problema successivo è che non dispongo del nome di ogni singola zona
                 * Li vado a recuperare da DB con questo metodo
                 */
                setNomiZone(allZoneList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /***
     * Pero ogni oggetto della lista allZoneList, vado a leggere da DB il suo "nomeZona"
     * @param allZoneList
     */
    private void setNomiZone(List<AllZona> allZoneList){

        for(int i=0; i<allZoneList.size(); i++){

            int finalI = i;
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("/");

            Query recentPostsQuery = myRef.child("Siti").child(percorso.getIdSitoAssociato()).child("Zone").child(allZoneList.get(i).getId()).child("nome");
            recentPostsQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                            allZoneList.get(finalI).setNomeZona((String) datasnapshot.getValue());

                            setZoneRecycler(allZoneList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    /***
     * Setto gli ogetti da inserire nella doppia recyclerview
     * @param allZoneList
     */
    private void setZoneRecycler(List<AllZona> allZoneList) {

        mainZoneRecycler = findViewById(R.id.recyclerPercorso);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        mainZoneRecycler.setLayoutManager(layoutManager);

        mainRecyclerAdapter=new MainRecyclerAdapterVisualizzaPercorso(this, allZoneList);
        mainZoneRecycler.setAdapter(mainRecyclerAdapter);

        zoneSito=allZoneList;
    }

    public void setImmagineSitoFromDB(String idSito, Context context, ImageView immagineSito){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/fotoSiti/"+idSito);

        /**
         * Scarica il "DownloadURL" che ci serve per leggere l'immagine dal DB e metterla in una ImageView
         * */
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(context).load(downloadUrl).into(immagineSito);
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Glide.with(context).load(R.drawable.image_not_found).into(immagineSito);
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

        getMenuInflater().inflate(R.menu.condividi_button, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.condividiPercorso:
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");

                DatabaseReference myRef = database.getReference("/Percorsi");

                Query recentPostsQuery = myRef.child(percorso.getId()).orderByChild("idZona");
                recentPostsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, ""+snapshot.getValue());
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                break;


        }

        return super.onOptionsItemSelected(item);
    }

}