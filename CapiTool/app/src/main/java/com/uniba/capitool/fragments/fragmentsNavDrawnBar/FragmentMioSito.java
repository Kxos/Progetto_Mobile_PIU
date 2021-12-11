package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiSito;

/**
 * create an instance of this fragment.
 */
public class FragmentMioSito extends Fragment {

    public FragmentMioSito() {
        // Required empty public constructor
    }


   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View v = inflater.inflate(R.layout.fragment_mio_sito, container, false);
       FloatingActionButton addSito = v.findViewById(R.id.buttonAddSito);

       addSito.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent aggiungiSito = new Intent(getActivity(), AggiungiSito.class);
               getActivity().startActivity(aggiungiSito);
           }
       });

       return v;

   }


}