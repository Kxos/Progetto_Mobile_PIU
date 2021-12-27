package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.classes.Utente;

/**

 * create an instance of this fragment.
 */
public class FragmentMieiPercorsi extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // Utente utente =getUserInfo();

        Utente utente = BasicMethod.getUtente();

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
     * Ottiene i dati dell'utente loggato (abbiamo impostato come primo fragment MioSito per il Curatore. Al momento questo metodo per leggere il Bundle non serve. Vedremo con il visitatore.
     *                                      Il problema riscontato è che se il fragment in questione è il primo ad essere istanziato della HomePage, BasicMethod.getUtente() da valore nullo.
     *                                      Forse non viene valorizzato in tempo)
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