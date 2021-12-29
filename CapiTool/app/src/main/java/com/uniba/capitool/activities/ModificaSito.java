package com.uniba.capitool.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentsModificaSito.FragmentModificaInfoSito;
import com.uniba.capitool.fragments.fragmentsModificaSito.FragmentModificaNomeSito;

public class ModificaSito extends AppCompatActivity {
    Utente utente = new Utente();
    SitoCulturale sito = new SitoCulturale();
    Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_sito);
        Bundle b = getIntent().getExtras();

        if(b!=null){
          sito = (SitoCulturale) b.getSerializable("sito");
          utente = (Utente) b.getSerializable("utente");

          Log.e("***********", ""+utente+" / "+sito);
        }else{
           /* BasicMethod.alertDialog(this, "C'è stato un errore nel caricare i tuoi dati, sarai riportato alla login", "Errore caricamento dati", "OK");
            Intent login= new Intent(HomePage.this, Login.class);
            this.startActivity(login);
            */
        }




        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Modifica Sito");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerModificaSito, new FragmentModificaNomeSito() );
        fragmentTransaction.commit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //in questo modo recupero il fragment in uso, così se l'utente spinge back torno al passo precedente
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerModificaSito);
                if(currentFragment instanceof FragmentModificaNomeSito){
                    Log.d( "--------------------FRAGMENT IN USE: ", "Registrati1");
                    Intent homePage = new Intent(ModificaSito.this, HomePage.class);
                    homePage.putExtra("cognome",utente.getCognome());
                    homePage.putExtra("nome",utente.getNome());
                    homePage.putExtra("uid",utente.getUid());
                    homePage.putExtra("email",utente.getEmail());
                    homePage.putExtra("ruolo",utente.getRuolo());
                    finish();
                    startActivity(homePage);



                }else if(currentFragment instanceof FragmentModificaInfoSito){
                    Log.d( "--------------------FRAGMENT IN USE: ", "Registrati2");
                    FragmentManager fragmentManager= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerModificaSito, new FragmentModificaNomeSito() );
                    fragmentTransaction.commit();
                    // toolbar.setNavigationIcon(R.drawable.ic_android_black_24dp);
                }
            }
        });

    }

    public Utente getUtente(){
        return utente;
    }

    public SitoCulturale getSito(){
        return sito;
    }

    public void setSito(SitoCulturale sito) {
        this.sito = sito;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}