package com.uniba.capitool.fragments.fragmentsProfilo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uniba.capitool.R;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.classes.Visitatore;

public class FragmentDatiPersonali extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dati_personali, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView text= view.findViewById(R.id.textDatiP);

        Visitatore utente=((HomePage)getActivity()).getUtente();    //recupero l'utente che ha fatto il login dalla activity HomePage
        text.setText(utente.getUid());

    }
}