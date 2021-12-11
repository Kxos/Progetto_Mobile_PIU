package com.uniba.capitool.fragments.fragmentsAggiungiInfoSito;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.uniba.capitool.R;

public class AggiungiNomeSito extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_aggiungi_nome_sito, container, false);
        Button avanti = v.findViewById(R.id.button);
        EditText nome =v.findViewById(R.id.nomeSito);
        //leggere il file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences!=null){
            String nomeTrovato = sharedPreferences.getString("nome", "");
            nome.setText(nomeTrovato);

        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerAggiungiSito, new AggiungiInfoSito());
                fragmentTransaction.commit();

                //scrivere nel file SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor datiSito = sharedPreferences.edit();
                datiSito.putString("nome", nome.getText().toString());
                datiSito.apply();
            }
        });

        return v;
    }
}