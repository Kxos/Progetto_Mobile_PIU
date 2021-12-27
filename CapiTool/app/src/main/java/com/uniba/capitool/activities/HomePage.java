package com.uniba.capitool.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;

import com.uniba.capitool.R;
import com.uniba.capitool.classes.Utente;

public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Utente utente = getUserInfo();
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        BasicMethod basicMethod = new BasicMethod();

        Toolbar toolbar = startToolbarDrawerLayout();
        NavController navController = basicMethod.startNavLateralMenu(toolbar, this);



    }

    /***
     * Default method for Android Back Button
     */
    @Override
    public void onBackPressed() { drawerLayout.closeDrawers(); }

    /***
     * Inizializza la Toolbar ed il Drawer laterale vuoto (Bianco)
     *
     * @return toolbar: la toolbar configurata
     */
    public Toolbar startToolbarDrawerLayout() {

        drawerLayout = findViewById(R.id.drawerLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("CapiTool");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);

        // Al click dell'Hamburgher, apre il Menù laterale
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        return toolbar;
    }

    /***
     * Ottiene i dati dell'utente loggato
     *
     * @return utente: Ritorna l'utente valorizzato delle sue informazioni
     */
    public Utente getUserInfo(){

        Utente utente = new Utente();

        utente.setUid(getIntent().getExtras().getString("uid"));
        utente.setNome(getIntent().getExtras().getString("nome"));
        utente.setCognome(getIntent().getExtras().getString("cognome"));
        utente.setEmail(getIntent().getExtras().getString("email"));
        utente.setRuolo(getIntent().getExtras().getString("ruolo"));

        return utente;
    }

}