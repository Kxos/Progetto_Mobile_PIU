package com.uniba.capitool.fragmentsNavDrawnBar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uniba.capitool.R;

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
       Button addSito = v.findViewById(R.id.buttonAddSito);

       addSito.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

       return v;

   }


}