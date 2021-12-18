package com.uniba.capitool.fragments.AggiungiPercorso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

/**
 */
public class FragmentSelezionaOpere extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seleziona_opere, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //leggere il file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(sharedPreferences!=null){

            SitoCulturale sitoCulturale = new SitoCulturale();

            sitoCulturale.setId(sharedPreferences.getString("idSito", ""));
            sitoCulturale.setNome(sharedPreferences.getString("nomeSito", ""));

        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

    }
}