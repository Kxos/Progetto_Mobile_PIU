package com.uniba.capitool.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.Visitatore;
import com.uniba.capitool.fragments.fragmentsRegistrazione.FragmentRegistraCredenziali;
import com.uniba.capitool.fragments.fragmentsRegistrazione.FragmentRegistraDatiPersonali;
import com.uniba.capitool.fragments.fragmentsRegistrazione.FragmentRegistraRuolo;

//import per la toolbar

public class Registrati extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrati);

        SharedPreferences datiRegistrazioneUtente = this.getPreferences(Context.MODE_PRIVATE);
        datiRegistrazioneUtente.edit().clear().commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.signUpToolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //fa comparire nella toolbar il pulsante back

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentRegistraCredenziali() );
        fragmentTransaction.commit();

        Visitatore visit =new Visitatore();

       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //in questo modo recupero il fragment in uso, così se l'utente spinge back torno al passo precedente
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                if(currentFragment instanceof FragmentRegistraCredenziali){

                    Intent login = new Intent(Registrati.this, Login.class);
                    startActivity(login);
                }else if(currentFragment instanceof FragmentRegistraRuolo){

                    FragmentManager fragmentManager= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentRegistraCredenziali() );
                    fragmentTransaction.commit();
                   // toolbar.setNavigationIcon(R.drawable.ic_android_black_24dp);
                }else if(currentFragment instanceof FragmentRegistraDatiPersonali){

                    FragmentManager fragmentManager= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentRegistraRuolo() );
                    fragmentTransaction.commit();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //fare la stessa cosa della toolbar
    }
}