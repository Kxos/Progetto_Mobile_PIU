package com.uniba.capitool.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.OrdinaZoneAdapter;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;

import java.util.Collections;
import java.util.List;

public class OrdinaZone extends AppCompatActivity {

    private String nuovaZona = "";
    RecyclerView ordinaZoneRecycler;
    OrdinaZoneAdapter ordinaZoneRecyclerAdapter;
    SitoCulturale sito;
    Utente utente;
    List<AllZona> allZone;
    Toolbar toolbar;
    Boolean modifiche_effettuate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordina_zone);

        modifiche_effettuate=false;

        Bundle dati = getIntent().getExtras();

        if(dati!=null){
            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");
            allZone = (List<AllZona>) dati.getSerializable("zone");

        }

        toolbar = findViewById(R.id.toolbarOrdinaZone);
        toolbar.setTitle(getString(R.string.orderZoneToolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(modifiche_effettuate=true){
                    for(int i=0; i<allZone.size(); i++){
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference("/Siti/"+sito.getId()+"/Zone/"+allZone.get(i).getId()+"/posizione");
                        myRef.setValue(""+i);
                    }

                    Toast.makeText((OrdinaZone.this), getString(R.string.correctOrderZone), Toast.LENGTH_SHORT).show();
                    OrdinaZone.super.onBackPressed();

                }else{

                    OrdinaZone.super.onBackPressed();
                }

            }
        });

        ordinaZoneRecycler = findViewById(R.id.recyclerOrdinamentoZone);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        ordinaZoneRecycler.setLayoutManager(layoutManager);

        ordinaZoneRecyclerAdapter =new OrdinaZoneAdapter(this, allZone);
        ordinaZoneRecycler.setAdapter(ordinaZoneRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(ordinaZoneRecycler);




    }

    ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            Collections.swap(allZone, fromPosition, toPosition);

           // Log.e(" ", ""+fromPosition+" to "+toPosition);

           // Log.e(" Oggetto allZone", ""+allZone.get(0).getNomeZona());

            toolbar.setNavigationIcon(R.drawable.ic_baseline_check_24);

            modifiche_effettuate=true;

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
           // Log.e("Pos 0", ""+allZone.get(0).getNomeZona());
        }
    };
}