package com.uniba.capitool.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    SitoCulturale sito = new SitoCulturale();
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        BasicMethod basicMethod = new BasicMethod();

        Toolbar toolbar = startToolbarDrawerLayout();
        NavController navController = basicMethod.startNavLateralMenu(toolbar, this);

        controllaSitoAssociato (BasicMethod.getUtente().getUid());
        //Log.e("PRIMA DELL'IF", ""+sito.getNome());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("SONO DOPO ONDATA CHANGE",""+sito);

                if(sito.getUidCuratore() != null ){
                        Log.e("*****!!!!*****","SITO ASSOCIATOOO DEFINITIVO");



                    }else{

                        Log.e("*****!!!!*****","SITO NON ASSOCIATOOO NON DEFINITIVO");


                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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



    public void controllaSitoAssociato (String uidUtente){
        Log.e("*****!!!!*****","SONO IN CONTROLLASITOASSOCIATO!!!");
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Nome

        recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(uidUtente);     //SELECT * FROM Siti WHERE getUid LIKE "..."
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<SitoCulturale> siti = new ArrayList<>();
                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.e("*****!!!!*****","SONO NEL FOR!!!");
                    siti.add(snapshot.getValue(SitoCulturale.class));

                }
               if(siti.size() != 0){
                   Log.e("****SITI_0*****", siti.get(0).getNome());
                   setSito(siti.get(0));
               }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }

    public void setSito(SitoCulturale sito1){
        Log.e("SITO1****!!!", ""+sito1.getNome());
        this.sito = sito1;

    }
}