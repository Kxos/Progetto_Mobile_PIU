package com.uniba.capitool.activity.fragmentsNavDrawnBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.uniba.capitool.R;

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
        return inflater.inflate(R.layout.fragment_impostazioni_sito, container, false);
    }
}