package com.uniba.capitool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import per la toolbar
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.uniba.capitool.classes.Visitatore;
import com.uniba.capitool.fragmentsRegistrazione.FragmentRegistraCredenziali;
import com.uniba.capitool.fragmentsRegistrazione.FragmentRegistraDatiPersonali;
import com.uniba.capitool.fragmentsRegistrazione.FragmentRegistraRuolo;

public class Registrati extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrati);

        SharedPreferences datiRegistrazioneUtente = this.getPreferences(Context.MODE_PRIVATE);//getSharedPreferences("datiRegistrazioneUtente", );
        datiRegistrazioneUtente.edit().clear().commit();

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button avanti = findViewById(R.id.avanti);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Registrati");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //fa comparire nella toolbar il pulsante back


        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentRegistraCredenziali() );
        fragmentTransaction.commit();


        //toolbar.setNavigationIcon(R.drawable.ic_android_black_24dp);
        Visitatore visit =new Visitatore();

       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //in questo modo recupero il fragment in uso, così se l'utente spinge back torno al passo precedente
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                if(currentFragment instanceof FragmentRegistraCredenziali){
                    Log.d( "--------------------FRAGMENT IN USE: ", "Registrati1");
                    Intent login = new Intent(Registrati.this, Login.class);
                    startActivity(login);
                }else if(currentFragment instanceof FragmentRegistraRuolo){
                    Log.d( "--------------------FRAGMENT IN USE: ", "Registrati2");
                    FragmentManager fragmentManager= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentRegistraCredenziali() );
                    fragmentTransaction.commit();
                   // toolbar.setNavigationIcon(R.drawable.ic_android_black_24dp);
                }else if(currentFragment instanceof FragmentRegistraDatiPersonali){
                    Log.d( "--------------------FRAGMENT IN USE: ", "Registrati3");
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