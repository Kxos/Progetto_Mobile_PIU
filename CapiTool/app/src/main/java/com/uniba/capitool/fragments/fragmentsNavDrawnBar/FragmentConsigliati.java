package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.uniba.capitool.R;

/**
 * create an instance of this fragment.
 */
public class FragmentConsigliati extends Fragment {


    public FragmentConsigliati() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consigliati, container, false);
    }
}