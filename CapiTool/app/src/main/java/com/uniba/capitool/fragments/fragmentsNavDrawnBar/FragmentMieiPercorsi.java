package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.activities.AggiungiSito;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.classes.Visitatore;

import java.util.zip.Inflater;

/**

 * create an instance of this fragment.
 */
public class FragmentMieiPercorsi extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Utente utente = getUserInfo();

        View v = inflater.inflate(R.layout.fragment_miei_percorsi, container, false);
        FloatingActionButton addPercorso = v.findViewById(R.id.buttonAddPercorso);

        addPercorso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aggiungiPercorso = BasicMethod.putUtenteExtrasInIntent(getActivity(),utente,AggiungiPercorso.class);
                getActivity().startActivity(aggiungiPercorso);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    /***
     * Ottiene i dati dell'utente loggato
     *
     * @return utente: Ritorna l'utente valorizzato delle sue informazioni
     */
    public Utente getUserInfo(){

        Utente utente = new Utente();

        utente.setUid(getActivity().getIntent().getExtras().getString("uid"));
        utente.setNome(getActivity().getIntent().getExtras().getString("nome"));
        utente.setCognome(getActivity().getIntent().getExtras().getString("cognome"));
        utente.setEmail(getActivity().getIntent().getExtras().getString("email"));
        utente.setRuolo(getActivity().getIntent().getExtras().getString("ruolo"));

        return utente;
    }
}