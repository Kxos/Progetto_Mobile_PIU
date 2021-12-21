package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiSito;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.activities.InfoSitoAssociato;
import com.uniba.capitool.classes.CardSitoCulturale;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

import java.util.ArrayList;

/**
 * create an instance of this fragment.
 */
public class FragmentMioSito extends Fragment {
    SitoCulturale sito = new SitoCulturale();
    DatabaseReference myRef;
    public FragmentMioSito() {

    }


   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       Utente utente = ((HomePage)getActivity()).getUtente();
       View v = inflater.inflate(R.layout.fragment_mio_sito, container, false);
       FloatingActionButton addSito = v.findViewById(R.id.buttonAddSito);

       controllaSitoAssociato (utente.getUid());
        //Log.e("PRIMA DELL'IF", ""+sito.getNome());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("SONO DOPO ONDATA CHANGE",""+sito.getNome());
                if(!sito.getNome().equals("")){
                    addSito.setVisibility(View.INVISIBLE);
                    Log.e("*****!!!!*****","SONO QUIIII!!!");
                    Intent infoSitoAssociato = new Intent(getActivity(), InfoSitoAssociato.class);
                    getActivity().startActivity(infoSitoAssociato);


                }else{
                    // Inflate the layout for this fragment
                    addSito.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent aggiungiSito = new Intent(getActivity(), AggiungiSito.class);
                            aggiungiSito.putExtra("cognome",utente.getCognome());
                            aggiungiSito.putExtra("nome",utente.getNome());
                            aggiungiSito.putExtra("uid",utente.getUid());
                            aggiungiSito.putExtra("email",utente.getEmail());
                            aggiungiSito.putExtra("ruolo",utente.getRuolo());
                            getActivity().startActivity(aggiungiSito);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





       return v;
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
               Log.e("****SITI_0*****", siti.get(0).getNome());
               setSito(siti.get(0));

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