package com.uniba.capitool.fragmentsNavDrawnBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uniba.capitool.HomePage;
import com.uniba.capitool.Login;
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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disconnettiti, container, false);
    }

}