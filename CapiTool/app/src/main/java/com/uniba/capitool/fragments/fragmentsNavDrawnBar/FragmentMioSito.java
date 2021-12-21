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

    public FragmentMioSito() {

    }


   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       Utente utente = BasicMethod.getUtente();
       View v = inflater.inflate(R.layout.fragment_mio_sito, container, false);
       FloatingActionButton addSito = v.findViewById(R.id.buttonAddSito);

       addSito.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent aggiungiSito = new Intent(getActivity(), AggiungiSito.class);
               aggiungiSito.putExtra("cognome", utente.getCognome());
               aggiungiSito.putExtra("nome", utente.getNome());
               aggiungiSito.putExtra("uid", utente.getUid());
               aggiungiSito.putExtra("email", utente.getEmail());
               aggiungiSito.putExtra("ruolo", utente.getRuolo());
               getActivity().startActivity(aggiungiSito);

           }
       });



       return v;

   }


}