package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.DelegaSito;
import com.uniba.capitool.activities.EliminaSito;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.Visitatore;


/**
 * create an instance of this fragment.
 */
public class FragmentImpostazioniSito extends Fragment {


    public FragmentImpostazioniSito() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Visitatore utente = getUserInfo();

        View v = inflater.inflate(R.layout.fragment_impostazioni_sito, container, false);
        TextView delega_sito = (TextView) v.findViewById(R.id.text_delega_sito) ;
        TextView elimina_sito = v.findViewById(R.id.text_elimina_sito) ;

        delega_sito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDelegaSito = new Intent(getActivity(), DelegaSito.class) ;
                intentDelegaSito.putExtra("uid", utente.getUid()); //Optional parameters
                intentDelegaSito.putExtra("nome", utente.getNome()); //Optional parameters
                intentDelegaSito.putExtra("cognome", utente.getCognome()); //Optional parameters
                intentDelegaSito.putExtra("email", utente.getEmail()); //Optional parameters
                intentDelegaSito.putExtra("ruolo", utente.getRuolo()); //Optional parameters
                getActivity().startActivity(intentDelegaSito);
            }
        });



        elimina_sito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEliminaSito = new Intent(getActivity(), EliminaSito.class) ;
                intentEliminaSito.putExtra("uid", utente.getUid()); //Optional parameters
                intentEliminaSito.putExtra("nome", utente.getNome()); //Optional parameters
                intentEliminaSito.putExtra("cognome", utente.getCognome()); //Optional parameters
                intentEliminaSito.putExtra("email", utente.getEmail()); //Optional parameters
                intentEliminaSito.putExtra("ruolo", utente.getRuolo()); //Optional parameters
                getActivity().startActivity(intentEliminaSito);
            }
        });



        return v ;

    }


    /***
     * Ottiene i dati dell'utente loggato
     *
     * @return utente: Ritorna l'utente valorizzato delle sue informazioni
     */
    public Visitatore getUserInfo(){

        Visitatore utente;

        if(BasicMethod.isCuratore(getActivity().getIntent().getExtras().getString("ruolo"))){
            utente = new Curatore();
        }else{
            utente = new Visitatore();
        }

        utente.setUid(getActivity().getIntent().getExtras().getString("uid"));
        utente.setNome(getActivity().getIntent().getExtras().getString("nome"));
        utente.setCognome(getActivity().getIntent().getExtras().getString("cognome"));
        utente.setEmail(getActivity().getIntent().getExtras().getString("email"));
        utente.setRuolo(getActivity().getIntent().getExtras().getString("ruolo"));

        return utente;
    }

}