package com.uniba.capitool.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    String nomeZona;
    List<String> nomiZone;
    List<AllZona> zoneSito;

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
//Log.e("PATH","/Percorsi/"+idPercorso+"/OpereScelte/");

        Query recentPostsQuery = myRef.child("OpereScelte").orderByChild("idZona");
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // Log.e("dataSnapshot",""+dataSnapshot.getChildren());
                List<ItemOperaZona> listaOpere = new ArrayList<>();


               // listaOpere= (List<ItemOperaZona>) dataSnapshot.getValue(ItemOperaZona.class);
//try{
    for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i risultati della query e li formatta esattamente come se fossero oggetti di classe Zona
        try{
            ItemOperaZona opera=snapshot.getValue(ItemOperaZona.class);
           // Log.e("Opera trovata",""+opera.getId());

            listaOpere.add(opera);
        }catch (Exception e){
            Log.e("Metodo ALTERNATIVO (Gestione stringa snapshot)","qui");

//                    String idZona = leggiIdZona(snapshot.getValue().toString());
//                    recuperaFromDB(idSito);

        }
    }
//}catch(Exception e){
//    Log.e("Metodo ALTERNATIVO (Gestione stringa snapshot)","qui2");
//}

                for(int i=0; i<listaOpere.size(); i++){
                   Log.e("Opera di listaOpere",""+listaOpere.get(i).getIdZona()+" "+listaOpere.get(i).getTitolo());
                }

                String idZona=listaOpere.get(0).getIdZona();
                List<String> indiciZone=new ArrayList<>();
                indiciZone.add(listaOpere.get(0).getIdZona());

                for(int i=0; i<listaOpere.size(); i++){
                    if(!idZona.equals(listaOpere.get(i).getIdZona())){
                        idZona=listaOpere.get(i).getIdZona();
                        indiciZone.add(listaOpere.get(i).getIdZona());
                    }
                }

//                for(int i=0; i<indiciZone.size(); i++){
//                    Log.e("IndiciZone","Zona: "+indiciZone.get(i));
//                }

               // leggiNomiZone(indiciZone);

                List<AllZona> allZoneList=new ArrayList<>();
                //List<ItemOperaZona> listaOpereZona=new ArrayList<>();

                /***
                 * iniziale
                 */

                Log.e("Size ", ""+listaOpere.size());

                idZona=listaOpere.get(0).getIdZona();

                for(int i=0; i<indiciZone.size(); i++){
                    Log.e("IndiciZone","Zona: "+indiciZone.get(i));
                    List<ItemOperaZona> listaOpereZona=new ArrayList<>();
                    for(int j=0; j<listaOpere.size(); j++){

                        //Log.e("Opera salvata","Zona: "+listaOpere.get(i).getIdZona()+"idOpera: "+listaOpere.get(i).getId()+" "+listaOpere.get(i).getTitolo());

                        if(listaOpere.get(j).getIdZona().equals(indiciZone.get(i))){

                            listaOpereZona.add(listaOpere.get(j));
                            Log.e("if Opera aggiunta a ", ""+idZona+"="+listaOpere.get(j));

                        }else{


                            //leggiNomeZona(percorso.getIdSitoAssociato(), idZona);
                            //  myRef.addListenerForSingleValueEvent(new                                );
                            // Log.e("Lista opere", ""+listaOpereZona.size());

//                            idZona=listaOpere.get(j).getIdZona();
//                            listaOpereZona.clear();
//
//                            listaOpereZona.add(listaOpere.get(j));
//                            Log.e("else Opera aggiunta a ", ""+idZona+"="+listaOpere.get(j).getTitolo());


                        }
                    }

                    allZoneList.add(new AllZona(indiciZone.get(i), indiciZone.get(i), listaOpereZona));
                }



                for(int i=0; i<allZoneList.size(); i++){
                    Log.e("allZoneList",""+allZoneList.get(i).getNomeZona());
                    for(int j=0; j<allZoneList.get(i).getListaOpereZona().size(); j++){
                        Log.e("opera",""+allZoneList.get(i).getListaOpereZona().get(j).getTitolo());

                    }
                }

                setZoneRecycler(allZoneList);
                /***
                 * Passo gli oggetti salvati nella lista zone, nella lista allZoneList
                 * Il primo for scorre le zone
                 * Il secondo for scorre tutte le opere della relativa zona
                 * Salto il salvataggio della prima opera perchè è null
                 */
               /* for(int i=0; i<listaOpere.size(); i++){
                    int contatore=0;
                    Log.e("", ""+listaOpere.get(i).getIdZona());

                    List<ItemOperaZona> listaOpereZona = new ArrayList<>();
                    try{
                        for(ItemOperaZona opera: listaOpere){
                            //Log.e("zone.get(i).getOpere()", ""+opera.getId());

                            //Log.e("SKIP", "Skip dell'opera null");

                            try{
                                listaOpereZona.add(new ItemOperaZona(opera.getId(), opera.getTitolo(), opera.getDescrizione(), listaOpere.get(i).getId(), opera.getIdFoto()));
                                Log.e("Opera trovata", ""+opera.getTitolo()+"/"+opera.getId());
                            }catch(Exception e){
                                Log.e("Opera trovata", "errore");
                            }


                            contatore++;
                        }
                        allZoneList.add(new AllZona(listaOpere.get(i).getId(), listaOpere.get(i).getNome(), listaOpereZona));
                    }catch (Exception e){
                        Log.e("","Erroreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

                        allZoneList.add(new AllZona(listaOpere.get(i).getId(), listaOpere.get(i).getNome(), null));
                    }


                }*/


            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    RecyclerView mainZoneRecycler;
    MainRecyclerAdapterVisualizzaPercorso mainRecyclerAdapter;

    private void setZoneRecycler(List<AllZona> allZoneList){
        mainZoneRecycler = findViewById(R.id.recyclerPercorso);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        mainZoneRecycler.setLayoutManager(layoutManager);

        mainRecyclerAdapter=new MainRecyclerAdapterVisualizzaPercorso(this, allZoneList);
        mainZoneRecycler.setAdapter(mainRecyclerAdapter);

        zoneSito=allZoneList;

    }
    private void leggiNomiZone(List<String> indiciZone) {
        //leggiNomeZona(indiciZone);
    }

    private void leggiNomeZona(String idSitoAssociato, String idZona) {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/Siti/"+idSitoAssociato+"/Zone/"+idZona);

        Log.e("datasnapshot", ""+idSitoAssociato+"-----------"+idZona);
        Query recentPostsQuery = myRef.limitToFirst(1);
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("inner datasnapshot", ""+dataSnapshot.getValue());
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    nomeZona= (String) snapshot.getValue();
                    Log.e("NomeZONA nel db", ""+snapshot.getValue());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
}