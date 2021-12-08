package com.uniba.capitool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Intent intent = getIntent();
        //String value = intent.getStringExtra("User_ID");
        //Log.d("Risultato",value);

        Toolbar toolbar = startToolbarDrawerLayout();
        NavController navController = setNavLateralMenu(toolbar);


    }

    /***
     * Default method for Android Back Button
     */
    @Override
    public void onBackPressed() {
        drawerLayout.closeDrawers();
    }

    /***
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
     * @param toolbar: La toolbar predefinita
     * @return navController: Impostato dell navigationView configurato
     */
    public NavController setNavLateralMenu(Toolbar toolbar) {
        NavigationView navigationView = findViewById(R.id.Home_Nav_Menu);
        navigationView.setItemIconTintList(toolbar.getBackgroundTintList());
        navigationView.setItemTextColor(null);

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

}