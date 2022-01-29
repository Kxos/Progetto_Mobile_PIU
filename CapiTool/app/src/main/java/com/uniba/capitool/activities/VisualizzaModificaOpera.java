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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniba.capitool.R;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;

public class VisualizzaModificaOpera extends AppCompatActivity {

    Uri image;
    ImageView immagine;
    EditText testo;
    EditText titoloOpera;
    ImageView close;
    String idSito;
    ItemOperaZona opera;
    private static final int SELECT_IMAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_modifica_opera);
        Bundle dati = getIntent().getExtras();
        immagine = findViewById(R.id.imageViewVisualizzaModificaOpera);
        testo = findViewById(R.id.descrizioneOpera);
        titoloOpera = findViewById(R.id.titoloNomeModificaOpera);
        Button btnModifica = findViewById(R.id.btnModificaOpera);
        close = findViewById(R.id.esci);
        close.setClickable(true);


        if(dati!=null){
            opera = (ItemOperaZona) dati.getSerializable("opera");
            Glide.with(this).load(opera.getFoto()).into(immagine);

            titoloOpera.setText(opera.getTitolo());
            testo.setText(opera.getDescrizione());
            idSito = dati.getString("idSito");

            setImmagineOperaFromDB(opera.getId(), this, immagine);

        }else{
            Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                    onBackPressed();
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
                        Glide.with(context).load(R.drawable.images).into(imageViewOpera);
                    }
                });

    }

    private void saveChanges(ItemOperaZona opera, String idSito) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef;

        myRef = database.getReference("/Siti/"+ idSito + "/Zone/" + opera.getIdZona() + "/Opere/" + opera.getId() + "/nome");
        myRef.setValue(titoloOpera.getText().toString());

        myRef = database.getReference("/Siti/"+ idSito + "/Zone/" + opera.getIdZona() + "/Opere/" + opera.getId() + "/descrizione");
        myRef.setValue(testo.getText().toString());

        StorageReference fileReference= FirebaseStorage.getInstance().getReference().child("fotoOpere").child(opera.getId());

//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.setMessage("Caricamento");
//        pd.show();



        fileReference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

//                pd.dismiss();
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
