package com.uniba.capitool.fragments.fragmentsAggiungiInfoSito;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.uniba.capitool.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AggiungiInfoSito#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AggiungiInfoSito extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aggiungi_info_sito, container, false);
    }
}