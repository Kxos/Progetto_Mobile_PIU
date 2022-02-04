package com.uniba.capitool.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;

public class VisualizzaModificaOpera extends AppCompatActivity{

    Uri image;
    ImageView immagine;
    EditText testo;
    EditText titoloOpera;
    String idSito;
    ItemOperaZona opera;
    Utente utente;
    SitoCulturale sito;
    String idZona;
    String nomeZona;
    AllZona allZone;

    private static final int SELECT_IMAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_modifica_opera);

        immagine = findViewById(R.id.imageViewVisualizzaModificaOpera);
        testo = findViewById(R.id.descrizioneOpera);
        titoloOpera = findViewById(R.id.titoloNomeModificaOpera);
        Button btnModifica = findViewById(R.id.btnModificaOpera);



        Bundle dati = getIntent().getExtras();

        if(dati!=null){
            opera = (ItemOperaZona) dati.getSerializable("opera");
            Glide.with(this).load(opera.getFoto()).into(immagine);

            titoloOpera.setText(opera.getTitolo());
            testo.setText(opera.getDescrizione());
            idSito = dati.getString("idSito");

            idZona = dati.getString("idZona");
            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");

            nomeZona=  dati.getString("nomeZona");
            allZone= (AllZona) dati.getSerializable("allZona");

            setImmagineOperaFromDB(opera.getIdFoto(), this, immagine);

        }else{
            Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }

        Toolbar toolbar = findViewById(R.id.toolbarModificaOpera);
        toolbar.setTitle("Modifica Opera " + opera.getTitolo());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisualizzaModificaOpera.super.onBackPressed();
            }
        });

        immagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "title"), SELECT_IMAGE_CODE);
            }
        });

        btnModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean erroreDatiPersonali=false;

                if(titoloOpera.getText().toString().equals("")){
                    titoloOpera.setError("Inserisci un titolo dell'opera che sia valido e non vuoto");
                    erroreDatiPersonali=true;
                }



                if(testo.getText().toString().equals("")){
                    testo.setError("Inserisci una descrizione");
                    erroreDatiPersonali=true;
                }





                if(erroreDatiPersonali==false){
                    saveChanges(opera, idSito);
                }

            }
        });
    }

    public void setImmagineOperaFromDB(String idOpera, Context context, ImageView imageViewOpera){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/fotoOpere/"+idOpera);

        /**
         * Scarica il "DownloadURL" che ci serve per leggere l'immagine dal DB e metterla in una ImageView
         * */
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(context).load(downloadUrl).into(imageViewOpera);
                image = downloadUrl;
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Glide.with(context).load(R.drawable.default_add_image).into(imageViewOpera);
                    }
                });

    }

    private void saveChanges(ItemOperaZona opera, String idSito) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef= database.getReference("/Siti/"+ idSito + "/Zone/" + opera.getIdZona() + "/Opere/" + opera.getId());

        Opera operaModificata =new Opera(opera.getId(), titoloOpera.getText().toString(), testo.getText().toString(), idZona, opera.getIdFoto());

//        myRef = database.getReference("/Siti/"+ idSito + "/Zone/" + opera.getIdZona() + "/Opere/" + opera.getId() + "/nome");
        myRef.setValue(operaModificata);

//        myRef = database.getReference("/Siti/"+ idSito + "/Zone/" + opera.getIdZona() + "/Opere/" + opera.getId() + "/descrizione");
//        myRef.setValue(testo.getText().toString());



        /***
         * Aggiormento pper la lista che viene letta "offline"
         */
        for (int i=0; i<allZone.getListaOpereZona().size(); i++){
            if(allZone.getListaOpereZona().get(i).getId().equals(opera.getId())){
                allZone.getListaOpereZona().get(i).setTitolo(titoloOpera.getText().toString());
                allZone.getListaOpereZona().get(i).setDescrizione(testo.getText().toString());
            }
        }

        StorageReference fileReference= FirebaseStorage.getInstance().getReference().child("fotoOpere").child(opera.getIdFoto());


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    fileReference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            Toast.makeText(VisualizzaModificaOpera.this, "Opera modificata con successo", Toast.LENGTH_LONG).show();

                            Intent backVisualizzaZona= new Intent(VisualizzaModificaOpera.this, VisualizzaZona.class);
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
                    });
                }catch (Exception e){
                    Toast.makeText(VisualizzaModificaOpera.this, "Opera modificata con successo", Toast.LENGTH_LONG).show();

                    Intent backVisualizzaZona= new Intent(VisualizzaModificaOpera.this, VisualizzaZona.class);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data!=null){
            Log.e("***********************************************************","");
            image=data.getData();
            if(image!=null){
                immagine.setImageURI(image);

            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top);

    }

}
