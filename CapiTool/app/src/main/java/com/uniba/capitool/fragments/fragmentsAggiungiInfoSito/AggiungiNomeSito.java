package com.uniba.capitool.fragments.fragmentsAggiungiInfoSito;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uniba.capitool.R;

public class AggiungiNomeSito extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_aggiungi_nome_sito, container, false);
        Button avanti = v.findViewById(R.id.button);
        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerAggiungiSito, new AggiungiInfoSito());
                fragmentTransaction.commit();
            }
        });

        return v;
    }
}