package com.uniba.capitool;

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

import android.os.Bundle;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = startToolbarDrawerLayout();
        NavController navController = startNavLateralMenu(toolbar);

    }

    /***
     * Default method for Android Back Button
     */
    @Override
    public void onBackPressed() {
        drawerLayout.closeDrawers();
    }

    /***
     * Inizializza la Toolbar ed il Drawer laterale vuoto
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
     * Inizializza il Drawer laterale con gli item di navigation_menu
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

        // TODO - QUERY PER RICEVERE IL RUOLO DELL'UTENTE
        // TODO - ... RUOLO RICEVUTO
        // TODO - SELEZIONE MENU A SECONDA DEL RUOLO
        navigationView.inflateMenu(R.menu.navigation_curatore_menu);
    }

}