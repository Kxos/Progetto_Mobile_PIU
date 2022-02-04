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
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;

public class VisualizzaOpera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_opera);

        ImageView immagine = findViewById(R.id.imageViewVisualizzaOpera);
        TextView testo = findViewById(R.id.textView12);
        TextView titoloOpera = findViewById(R.id.titoloNomeOpera);
        CardView qrCode = findViewById(R.id.qrCodeButton);
        Bundle dati = getIntent().getExtras();
        ImageView esci = findViewById(R.id.esci);
        esci.setClickable(true);


        if(dati!=null){
            ItemOperaZona opera = (ItemOperaZona) dati.getSerializable("opera");
            Glide.with(this).load(opera.getFoto()).into(immagine);
           // immagine.setImageDrawable(opera.getFoto());
            titoloOpera.setText(opera.getTitolo());
            testo.setText(opera.getDescrizione());

            setImmagineOperaFromDB(opera.getIdFoto(), this, immagine);
        }else{
            Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }

        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 
                Log.e("VisualizzaOpera","cliccato QRCODE");
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
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Glide.with(context).load(R.drawable.images).into(imageViewOpera);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top);

    }



}