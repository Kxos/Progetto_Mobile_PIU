package com.uniba.capitool;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentRegistraCredenziali extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View v= inflater.inflate(R.layout.fragment_registra_credenziali, container, false);

       Button avanti= v.findViewById(R.id.avanti);
       EditText email=v.findViewById(R.id.email);
       EditText password=v.findViewById(R.id.password);

       avanti.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Bundle bundle = new Bundle();
               bundle.putString("email",email.getText().toString()); // Put anything what you want
               bundle.putString("password",password.getText().toString());

               FragmentRegistraRuolo fragmentRegistraRuolo = new FragmentRegistraRuolo();
               fragmentRegistraRuolo.setArguments(bundle);

               FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
               FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.fragmentContainerView, fragmentRegistraRuolo);
               fragmentTransaction.commit();


           }
       });


        return v;

    }



}