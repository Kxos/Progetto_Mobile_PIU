package com.uniba.capitool.fragments.fragmentsAggiungiPercorso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.classes.CardOpera;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDatiPercorso extends Fragment {

    private Toolbar toolbar;
    private View viewActivity;
    private static ArrayList<CardOpera> listaOpereChecked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dati_percorso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.viewActivity = view;

        toolbar = ((AggiungiPercorso)getActivity()).getToolbar();

        // Leggo dal file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(sharedPreferences!=null){

            Bundle args = getArguments();;
            listaOpereChecked = (ArrayList<CardOpera>)args.getSerializable("listaOpereSelezionate");

            //Log.e("SEI IN DATI PERCORSO: ", ""+listaOpereChecked);

            //toolbar.setTitle(getString(R.string.site) + " - " + sitoCulturale.getNome());

            Button bottoneIndietro = view.findViewById(R.id.buttonIndietro);
            bottoneIndietro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentSelezionaOpere fragmentSelezionaOpere = new FragmentSelezionaOpere();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("listaOpereSelezionate",  listaOpereChecked);
                    fragmentSelezionaOpere.setArguments(bundle);

                    //Log.e( "LISTA IN RITORNO: ", ""+FragmentDatiPercorso.getListaOpereChecked());

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerRicercaSiti, fragmentSelezionaOpere );
                    fragmentTransaction.commit();
                }
            });



        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

    }

    public static ArrayList<CardOpera> getListaOpereChecked(){
        return listaOpereChecked;
    }
}