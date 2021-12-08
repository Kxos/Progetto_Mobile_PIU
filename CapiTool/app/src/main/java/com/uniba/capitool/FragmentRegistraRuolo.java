package com.uniba.capitool;

import android.content.Context;
import android.content.SharedPreferences;
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

        TextView backCredenziali = v.findViewById(R.id.backCredenziali);

        //leggere il file SharedPreferences
        SharedPreferences datiRegistrazioneUtente = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(datiRegistrazioneUtente!=null){
            String ruoloTrovato = datiRegistrazioneUtente.getString("ruolo", "");

            if(ruoloTrovato.equals("guida")){
                guida.setChecked(true);
            }else if(ruoloTrovato.equals("curatore")){
                curatore.setChecked(true);
            }else if(ruoloTrovato.equals("visitatore")){
                visitatore.setChecked(true);
            }

        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

     /*   backCredenziali.setOnClickListener(new View.OnClickListener() {
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


       // SharedPreferences datiRegistrazioneUtente = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = datiRegistrazioneUtente.edit();

        guida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("ruolo", "guida");
                editor.apply();
            }
        });

        curatore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("ruolo", "curatore");
                editor.apply();
            }
        });

        visitatore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("ruolo", "visitatore");
                editor.apply();
            }
        });
   /*     radioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ruoloSelezionato=null;
                int radioId=radioGroup.getCheckedRadioButtonId();
                RadioButton ruolo = (RadioButton) v.findViewById(radioId);

                if(guida.isChecked()){
                    ruoloSelezionato="guida";
                    editor.putString("ruolo", ruoloSelezionato);
                    editor.apply();

                }else if(curatore.isChecked()){
                    ruoloSelezionato="curatore";
                    editor.putString("ruolo", ruoloSelezionato);
                    editor.apply();

                }else if(visitatore.isChecked()){
                    ruoloSelezionato="visitatore";
                    editor.putString("ruolo", ruoloSelezionato);
                    editor.apply();

                }
            }
        });*/

        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erroreRuolo=false;

                String ruoloSelezionato=null;

                if(guida.isChecked()){
                    ruoloSelezionato="guida";
                    editor.putString("ruolo", ruoloSelezionato);
                    editor.apply();

                }else if(curatore.isChecked()){
                    ruoloSelezionato="curatore";
                    editor.putString("ruolo", ruoloSelezionato);
                    editor.apply();

                }else if(visitatore.isChecked()){
                    ruoloSelezionato="visitatore";
                    editor.putString("ruolo", ruoloSelezionato);
                    editor.apply();

                }else{
                    erroreRuolo=true;
                    BasicMethod.alertDialog(getActivity(), "Seleziona un ruolo tra Curatore, Visitatore o Guida Turistica", "Ruolo non selezionato", "OK");
                }

               /* Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("password",password);
                bundle.putString("ruolo", ruoloSelezionato);*/

                if(erroreRuolo==false){
                    FragmentRegistraDatiPersonali fragmentRegistraDatiPersonali = new FragmentRegistraDatiPersonali();
                    // fragmentRegistraDatiPersonali.setArguments(bundle);

                    FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragmentRegistraDatiPersonali);
                    fragmentTransaction.commit();
                }

            }
        });
        return v;
    }

}