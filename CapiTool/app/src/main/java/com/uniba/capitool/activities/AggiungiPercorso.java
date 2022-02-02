package com.uniba.capitool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentsAggiungiPercorso.FragmentDatiPercorso;
import com.uniba.capitool.fragments.fragmentsAggiungiPercorso.FragmentPreviewPercorso;
import com.uniba.capitool.fragments.fragmentsAggiungiPercorso.FragmentRicercaSiti;
import com.uniba.capitool.fragments.fragmentsAggiungiPercorso.FragmentSelezionaOpere;

public class AggiungiPercorso extends AppCompatActivity {

    Utente utente;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_percorso);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.addSite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();

        utente = new Utente();

        utente.setUid(b.getString("uid"));
        utente.setNome(b.getString("nome"));
        utente.setCognome(b.getString("cognome"));
        utente.setEmail(b.getString("email"));
        utente.setRuolo(b.getString("ruolo"));

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerRicercaSiti, new FragmentRicercaSiti() );
        fragmentTransaction.commit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //in questo modo recupero il fragment in uso, così se l'utente spinge back torno al passo precedente
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.containerRicercaSiti);
                if(currentFragment instanceof FragmentRicercaSiti){
                    Log.d( "--------------------FRAGMENT IN USE: ", "FragmentRicercaSiti");
                    Intent HomePage = BasicMethod.putUtenteExtrasInIntent(AggiungiPercorso.this,utente,HomePage.class);
                    startActivity(HomePage);
                } else if (currentFragment instanceof FragmentSelezionaOpere){
                    Log.d( "--------------------FRAGMENT IN USE: ", "FragmentSelezionaOpere");
                    toolbar.setTitle(R.string.addSite);
                    FragmentManager fragmentManager= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerRicercaSiti, new FragmentRicercaSiti() );
                    fragmentTransaction.commit();
                } else if (currentFragment instanceof FragmentDatiPercorso){
                    Log.d( "--------------------FRAGMENT IN USE: ", "FragmentSelezionaOpere");

                    FragmentSelezionaOpere fragmentSelezionaOpere = new FragmentSelezionaOpere();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("listaOpereSelezionate",  FragmentDatiPercorso.getListaOpereChecked());
                    fragmentSelezionaOpere.setArguments(bundle);

                    //Log.e( "LISTA IN RITORNO: ", ""+FragmentDatiPercorso.getListaOpereChecked());

                    FragmentManager fragmentManager= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerRicercaSiti, fragmentSelezionaOpere );
                    fragmentTransaction.commit();

                }else if (currentFragment instanceof FragmentPreviewPercorso){
                    Log.d( "--------------------FRAGMENT IN USE: ", "FragmentSelezionaOpere");

                    FragmentDatiPercorso fragmentDatiPercorso = new FragmentDatiPercorso();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("nuovoPercorso",  FragmentPreviewPercorso.getPercorso());
                    bundle.putSerializable("listaOpereRicevuteDaPreview",  FragmentPreviewPercorso.getListaOpere());
                    fragmentDatiPercorso.setArguments(bundle);

                    FragmentManager fragmentManager= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerRicercaSiti, fragmentDatiPercorso );
                    fragmentTransaction.commit();

                }
            }
        });

    }
    /** FINE onCreate()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    public Utente getUtente(){
        return utente;
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

}