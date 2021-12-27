package com.uniba.capitool.fragments.fragmentsProfilo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.uniba.capitool.R;

public class FragmentSicurezza extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sicurezza, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText vecchiaPsw = view.findViewById(R.id.textfield_VecchiaPassword);
        TextInputEditText nuovaPsw = view.findViewById(R.id.textfield_NuovaPassword);
        TextInputEditText confermaPsw = view.findViewById(R.id.textfield_ConfermaPassword);
        Button conferma = view.findViewById(R.id.confermaModificaPassword);

        conferma.setEnabled(false);

        
    }
}