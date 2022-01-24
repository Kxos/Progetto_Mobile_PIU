package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.uniba.capitool.R;

/**
 * create an instance of this fragment.
 */
public class FragmentDisconnettiti extends Fragment {

    public FragmentDisconnettiti() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor datiUtente = sharedPreferences.edit();
        datiUtente.clear().commit();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disconnettiti, container, false);
    }

}