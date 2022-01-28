package com.uniba.capitool.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

import java.util.ArrayList;
import java.util.List;

public class AggiungiOpera extends AppCompatActivity {

    private static final int SELECT_IMAGE_CODE = 1;
    private Uri image;
    ImageView fotoOpera;
    TextInputEditText descrizioneOpera, titoloOpera;
    Utente utente;
    SitoCulturale sito;
    String idZona;
    String nomeZona;
    ProgressBar progressBar;
    Button btaggiungiOpera;
    int nextIdOpera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_opera);

        fotoOpera = findViewById(R.id.addOpera_foto);
        descrizioneOpera = findViewById(R.id.textfield_descrizioneOpera);
        titoloOpera = findViewById(R.id.text_titolo_opera);
        btaggiungiOpera = findViewById(R.id.bt_Aggiungi_Opera);


        Bundle dati = getIntent().getExtras();

        if(dati!=null){

            idZona = dati.getString("idZona");
            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");
            idZona = dati.getString("idZona");
            nomeZona=  dati.getString("nomeZona");

        }else{
            Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }

        Toolbar toolbar = findViewById(R.id.toolbarAddOpera);
        toolbar.setTitle("Aggiungi opera in"+ nomeZona);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AggiungiOpera.super.onBackPressed();
            }
        });

        fotoOpera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "title"), SELECT_IMAGE_CODE);
            }
        });

        btaggiungiOpera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean erroreDatiCompilati=false;

                if(titoloOpera.getText().toString().equals("")){
                    titoloOpera.setError("Inserisci un titolo dell'opera che sia valido e non vuoto");
                    erroreDatiCompilati=true;
                }



                if(descrizioneOpera.getText().toString().equals("")){
                    descrizioneOpera.setError("Inserisci una descrizione");
                    erroreDatiCompilati=true;
                }





                if(erroreDatiCompilati==false){
                    saveChanges();
                   // onBackPressed();
                }

            }
        });
    }

//    public void setImmagineOperaFromDB(String idOpera, Context context, ImageView imageViewOpera){
//
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference dateRef = storageRef.child("/fotoOpere/"+idOpera);
//
//        /**
//         * Scarica il "DownloadURL" che ci serve per leggere l'immagine dal DB e metterla in una ImageView
//         * */
//        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
//        {
//            @Override
//            public void onSuccess(Uri downloadUrl)
//            {
//                Glide.with(context).load(downloadUrl).into(imageViewOpera);
//                image = downloadUrl;
//            }
//
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Glide.with(context).load(R.drawable.images).into(imageViewOpera);
//                    }
//                });
//
//    }

    private void saveChanges() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef= database.getReference("/");

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        String key = myRef.push().getKey();

        leggiOpere();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference myRef;
                Log.e("nextId",""+nextIdOpera);

                myRef =database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/titolo");
        myRef.setValue(titoloOpera.getText().toString());

        myRef =database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/id");
        myRef.setValue(""+nextIdOpera);

        myRef = database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/descrizione");
        myRef.setValue(descrizioneOpera.getText().toString());

                myRef = database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/idFoto");
                myRef.setValue(key);

                StorageReference fileReference= FirebaseStorage.getInstance().getReference().child("fotoOpere").child(key);

//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.setMessage("Caricamento");
//        pd.show();



                fileReference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        //   pd.dismiss();
//                        Intent aggiungiOpera= new Intent(AggiungiOpera.this, VisualizzaZona.class);
//                        Bundle dati = new Bundle();
//                        dati.putSerializable("sito", sito);
//                        dati.putSerializable("utente", utente);
//                        dati.putString("nomeZona", nomeZona);
//                        //dati.putString("idZona", allZone.getId());
//                        aggiungiOpera.putExtras(dati);
//                        startActivity(aggiungiOpera);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       // String key = myRef.push().getKey();

//



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data!=null){
            Log.e("***********************************************************","");
            image=data.getData();
            if(image!=null){
                fotoOpera.setImageURI(image);

            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top);

    }

   private void leggiOpere(){
       // int nextIdOpera=0;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        //TODO mettere l'id del sito nel path e togliere 1
        DatabaseReference myRef = database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/");

        Query recentPostsQuery = myRef.orderByChild("id");    //SELECT * FROM Utenti WHERE ruolo="visitatore"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Opera> opere=new ArrayList<>();

//                try{}catch (Exception e){}
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i risultati della query e li formatta esattamente come se fossero oggetti di classe Zona
                    try{
                        Opera opera = snapshot.getValue(Opera.class);
                        opere.add(opera);
                    }catch (Exception e){
                        Log.e("Metodo ALTERNATIVO (Gestione stringa snapshot)","");

//                    String idZona = leggiIdZona(snapshot.getValue().toString());
//                    recuperaFromDB(idSito);

                    }
                }



                /***
                 * Passo gli oggetti salvati nella lista zone, nella lista allZoneList
                 * Il primo for scorre le zone
                 * Il secondo for scorre tutte le opere della relativa zona
                 * Salto il salvataggio della prima opera perchè è null
                 */
                for(int i=0; i<opere.size(); i++){
                    int contatore=0;
                    Log.e("", ""+opere.get(i).getId());

//                    List<ItemOperaZona> listaOpereZona = new ArrayList<>();
//                    try{
//                        for(Opera opera: zone.get(i).getOpere()){
//                            if(contatore==0){
//                                Log.e("SKIP", "Skip dell'opera null");
//                            }else{
//                                listaOpereZona.add(new ItemOperaZona(opera.getId(), opera.getTitolo(), opera.getDescrizione(), zone.get(i).getId()));
//                                Log.e("Opera trovata", ""+opera.getTitolo()+"/"+opera.getId());
//                            }
//                            contatore++;
//                        }
//                        allZoneList.add(new AllZona(zone.get(i).getId(), zone.get(i).getNome(), listaOpereZona));
//                    }catch (Exception e){
//                        Log.e("","Erroreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//
//                        allZoneList.add(new AllZona(zone.get(i).getId(), zone.get(i).getNome(), null));
//                    }


                }

//                //ciclo for per scorrere la lista ottnuta dal db
//                int contatore=0;
//                for(Zona visitatore : zone){
//                    Log.d("Oggetto "+(contatore+1)+" della lista visitatori", ""+visitatore);
//                    contatore++;
//                }
//
//                Log.d("Lunghezza lista della query", String.valueOf(zone.size()));
//
//                setZoneRecycler(allZoneList);
Log.e("size opere",""+opere.size());
               if(opere.size()==0){
                   salvaIndice(0);
               }else{
                   salvaIndice(Integer.parseInt(opere.get(opere.size()-1).getId()));
               }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }

    private void salvaIndice(int idOperaPrecedente) {
        Log.e("nextIdSalvaIndice",""+idOperaPrecedente);
        nextIdOpera=idOperaPrecedente+1;
    }
}