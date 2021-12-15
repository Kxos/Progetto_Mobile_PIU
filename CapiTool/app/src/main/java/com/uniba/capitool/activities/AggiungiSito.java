package com.uniba.capitool.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.Visitatore;
import com.uniba.capitool.fragments.fragmentsAggiungiInfoSito.*;
import com.uniba.capitool.fragments.fragmentsRegistrazione.FragmentRegistraCredenziali;
import com.uniba.capitool.fragments.fragmentsRegistrazione.FragmentRegistraDatiPersonali;
import com.uniba.capitool.fragments.fragmentsRegistrazione.FragmentRegistraRuolo;

public class AggiungiSito extends AppCompatActivity {
    Visitatore utente = new Visitatore();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        if(b!=null){
            utente.setUid(b.getString("uid"));
            utente.setNome(b.getString("nome"));
            utente.setCognome(b.getString("cognome"));
            utente.setEmail(b.getString("email"));
            utente.setRuolo(b.getString("ruolo"));
        }else{
           /* BasicMethod.alertDialog(this, "C'è stato un errore nel caricare i tuoi dati, sarai riportato alla login", "Errore caricamento dati", "OK");
            Intent login= new Intent(HomePage.this, Login.class);
            this.startActivity(login);
            */
        }

        setContentView(R.layout.activity_aggiungi_sito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Aggiungi Sito");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerAggiungiSito, new AggiungiNomeSito() );
        fragmentTransaction.commit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //in questo modo recupero il fragment in uso, così se l'utente spinge back torno al passo precedente
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerAggiungiSito);
                if(currentFragment instanceof AggiungiNomeSito){
                    Log.d( "--------------------FRAGMENT IN USE: ", "Registrati1");
                    Intent homePage = new Intent(AggiungiSito.this, HomePage.class);
                    homePage.putExtra("cognome",utente.getCognome());
                    homePage.putExtra("nome",utente.getNome());
                    homePage.putExtra("uid",utente.getUid());
                    homePage.putExtra("email",utente.getEmail());
                    homePage.putExtra("ruolo",utente.getRuolo());
                    startActivity(homePage);



                }else if(currentFragment instanceof AggiungiInfoSito){
                    Log.d( "--------------------FRAGMENT IN USE: ", "Registrati2");
                    FragmentManager fragmentManager= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerAggiungiSito, new AggiungiNomeSito() );
                    fragmentTransaction.commit();
                    // toolbar.setNavigationIcon(R.drawable.ic_android_black_24dp);
                }
            }
        });

    }

    public Visitatore getUtente(){

        return utente;
    }
}