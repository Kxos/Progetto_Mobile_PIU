package com.uniba.capitool.fragments.fragmentsMioSito;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiSito;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.classes.Utente;

/**
 * create an instance of this fragment.
 */
public class FragmentCreaMioSito extends Fragment {

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       Utente utente = BasicMethod.getUtente();
       View v = inflater.inflate(R.layout.fragment_crea_mio_sito, container, false);
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