package com.uniba.capitool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.Visitatore;
import com.uniba.capitool.fragments.fragmentsMioSito.*;

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
           Log.e("Errore", "Nessun bundle trovato");
        }

        setContentView(R.layout.activity_aggiungi_sito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.addSiteToolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerAggiungiSito, new FragmentAggiungiNomeSito() );
        fragmentTransaction.commit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //in questo modo recupero il fragment in uso, così se l'utente spinge back torno al passo precedente
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerAggiungiSito);
                if(currentFragment instanceof FragmentAggiungiNomeSito){

                    Intent homePage = new Intent(AggiungiSito.this, HomePage.class);
                    homePage.putExtra("cognome",utente.getCognome());
                    homePage.putExtra("nome",utente.getNome());
                    homePage.putExtra("uid",utente.getUid());
                    homePage.putExtra("email",utente.getEmail());
                    homePage.putExtra("ruolo",utente.getRuolo());
                    startActivity(homePage);

                }else if(currentFragment instanceof FragmentAggiungiInfoSito){
                    Log.d( "--------------------FRAGMENT IN USE: ", "Registrati2");
                    FragmentManager fragmentManager= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerAggiungiSito, new FragmentAggiungiNomeSito() );
                    fragmentTransaction.commit();

                }
            }
        });

    }

    public Visitatore getUtente(){

        return utente;
    }
}