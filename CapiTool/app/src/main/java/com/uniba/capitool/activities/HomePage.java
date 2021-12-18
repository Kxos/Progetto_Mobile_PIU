package com.uniba.capitool.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.Visitatore;

public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;

    Visitatore utente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        Toolbar toolbar = startToolbarDrawerLayout();
        NavController navController = startNavLateralMenu(toolbar);


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
     *
     * Inizializza il Drawer laterale con gli item di navigation_RUOLO_menu
     *
     * @param toolbar: La toolbar predefinita
     * @return navController: Impostato dell navigationView configurato
     */
    public NavController startNavLateralMenu(Toolbar toolbar) {
        NavigationView navigationView = findViewById(R.id.Home_Nav_Menu);
        navigationView.setItemIconTintList(toolbar.getBackgroundTintList());
        navigationView.setItemTextColor(null);

        setNavLateralMenuOnUserRole(navigationView);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        // In base al nome del Fragment, cambia il Titolo sulla Toolbar
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                toolbar.setTitle(destination.getLabel());

                switch (destination.getId()){

                    // Torna al Login
                    case R.id.disconnetti:
                        Intent myIntent = new Intent(HomePage.this, Login.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                        break;
                }
            }
        });

        return navController;
    }

    /***
     * Imposta il Menù laterale in base al Ruolo dell'utente
     *
     * @param navigationView: Viene passata la navigationView legata alla Toolbar
     */
    public void setNavLateralMenuOnUserRole(NavigationView navigationView){
        navigationView.getMenu().clear();

        Bundle b = getIntent().getExtras();

        if(BasicMethod.isCuratore(b.getString("ruolo"))){
            utente = new Curatore();
            navigationView.inflateMenu(R.menu.navigation_curatore_menu);
        }else{
            utente = new Visitatore();
            navigationView.inflateMenu(R.menu.navigation_visitatore_menu);
        }

        if(b!=null){
            utente.setUid(b.getString("uid"));
            utente.setNome(b.getString("nome"));
            utente.setCognome(b.getString("cognome"));
            utente.setEmail(b.getString("email"));
            utente.setRuolo(b.getString("ruolo"));
        }else{
            BasicMethod.alertDialog(this, "C'è stato un errore nel caricare i tuoi dati, sarai riportato alla login", "Errore caricamento dati", "OK");
            Intent login= new Intent(HomePage.this, Login.class);
            this.startActivity(login);

        }



        View headerView = navigationView.getHeaderView(0);

        TextView headerNome    = headerView.findViewById(R.id.headerNome);
        TextView headerCognome = headerView.findViewById(R.id.headerCognome);
        TextView headerEmail   = headerView.findViewById(R.id.headerEmail);

        headerNome.setText(utente.getNome());
        headerCognome.setText(utente.getCognome());
        headerEmail.setText(utente.getEmail());

    }

    public Visitatore getUtente(){
        return utente;
    }

}