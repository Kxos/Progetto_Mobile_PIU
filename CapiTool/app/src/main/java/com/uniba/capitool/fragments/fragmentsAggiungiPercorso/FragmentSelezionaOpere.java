package com.uniba.capitool.fragments.fragmentsAggiungiPercorso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Zona;

import java.util.ArrayList;

public class FragmentSelezionaOpere extends Fragment {

    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seleziona_opere, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = ((AggiungiPercorso)getActivity()).getToolbar();

        // Leggo dal file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(sharedPreferences!=null){

            SitoCulturale sitoCulturale = new SitoCulturale();

            sitoCulturale.setId(sharedPreferences.getString("idSito", ""));
            sitoCulturale.setNome(sharedPreferences.getString("nomeSito", ""));

            toolbar.setTitle(getString(R.string.site) + " - " + sitoCulturale.getNome());

            recuperaZoneFromDBOrderByZoneId("1");


        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

    }

    /***
     * Ottiene tutte le Zone esistenti di un Sito
     *
     * @param idSito: Id del Sito selezionato nel Fragment Precedente (Tramite Card)
     */
    public void recuperaZoneFromDBOrderByZoneId(String idSito) {

        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Zone

        recentPostsQuery = myRef.child("Siti/"+idSito+"/Zone").orderByChild("id");     //SELECT Zone FROM Siti WHERE id = idSito
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Zona> listaZone = new ArrayList<>();

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Zona zona = snapshot.getValue(Zona.class);
                    //Log.e("RISULTATO DB NOME: ",zona.getNome());
                    listaZone.add(zona);

                }

                mostraOperePerZone(listaZone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }
    /** FINE recuperaZoneFromDBOrderByZoneId()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    public void mostraOperePerZone(ArrayList<Zona> listaZone){
        Log.e("WEJFYGBSAUYFGSDCYUBFSVAYU",""+listaZone.get(0).getOpere()+""+listaZone.get(0).getOpere().size());

        int count=0;
        for (Opera opera : listaZone.get(0).getOpere()) {
            if(count!=0){
                Log.e("WEJFYGBSAUYFGSDCYUBFSVAYU",""+opera.getTitolo());
            }
            count++;

        }
    }


}