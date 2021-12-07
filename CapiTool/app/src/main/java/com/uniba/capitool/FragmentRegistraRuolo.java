package com.uniba.capitool;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class FragmentRegistraRuolo extends Fragment {

    String email;
    String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = this.getArguments();

        if(bundle != null){
            // handle your code here.
            email=bundle.get("email").toString();
            password=bundle.get("password").toString();
        }




        // Inflate the layout for this fragment
         View v=inflater.inflate(R.layout.fragment_registra_ruolo, container, false);

      //  Toolbar toolbar = v.findViewById(R.id.toolbar2);
        //toolbar.setTitle("Registrati");

     /*   toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("password",password);
              //  bundle.putString("ruolo", ruoloSelezionato);
                FragmentRegistraCredenziali fragmentRegistraCredenziali = new FragmentRegistraCredenziali();
                fragmentRegistraCredenziali.setArguments(bundle);

                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragmentRegistraCredenziali);
                fragmentTransaction.commit();
            }
        });*/

        Button avanti=v.findViewById(R.id.avanti2);

        RadioGroup radioGroup= v.findViewById(R.id.radioGroup2);
        RadioButton guida = v.findViewById(R.id.guida);
        RadioButton curatore = v.findViewById(R.id.curatore);
        RadioButton visitatore = v.findViewById(R.id.visitatore);
        visitatore.setChecked(true);
        TextView backCredenziali = v.findViewById(R.id.backCredenziali);

        backCredenziali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("password",password);
                //  bundle.putString("ruolo", ruoloSelezionato);
                FragmentRegistraCredenziali fragmentRegistraCredenziali = new FragmentRegistraCredenziali();
                fragmentRegistraCredenziali.setArguments(bundle);

                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragmentRegistraCredenziali);
                fragmentTransaction.commit();
            }
        });

        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String ruoloSelezionato;
                int radioId=radioGroup.getCheckedRadioButtonId();
                RadioButton ruolo = (RadioButton) v.findViewById(radioId);


                if(guida.isChecked()){
                    ruoloSelezionato="visitatore";

                }else if(curatore.isChecked()){
                    ruoloSelezionato="curatore";

                }else{
                    ruoloSelezionato="visitatore";

                }


                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("password",password);
                bundle.putString("ruolo", ruoloSelezionato);
                FragmentRegistraDatiPersonali fragmentRegistraDatiPersonali = new FragmentRegistraDatiPersonali();
                fragmentRegistraDatiPersonali.setArguments(bundle);

                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragmentRegistraDatiPersonali);
                fragmentTransaction.commit();
            }
        });
        return v;
    }

}