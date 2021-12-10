package com.uniba.capitool.activity.fragmentsRegistrazione;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.uniba.capitool.R;
import com.uniba.capitool.activity.BasicMethod;


public class FragmentRegistraRuolo extends Fragment {

    String email;
    String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
         View v=inflater.inflate(R.layout.fragment_registra_ruolo, container, false);

        Button avanti=v.findViewById(R.id.avanti2);

        RadioGroup radioGroup= v.findViewById(R.id.radioGroup2);
        RadioButton guida = v.findViewById(R.id.guida);
        RadioButton curatore = v.findViewById(R.id.curatore);
        RadioButton visitatore = v.findViewById(R.id.visitatore);

        //leggere il file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences!=null){
            String ruoloTrovato = sharedPreferences.getString("ruolo", "");

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

       // SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor datiRegistrazioneUtente = sharedPreferences.edit();

        guida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datiRegistrazioneUtente.putString("ruolo", "guida");
                datiRegistrazioneUtente.apply();
            }
        });

        curatore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datiRegistrazioneUtente.putString("ruolo", "curatore");
                datiRegistrazioneUtente.apply();
            }
        });

        visitatore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datiRegistrazioneUtente.putString("ruolo", "visitatore");
                datiRegistrazioneUtente.apply();
            }
        });

        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erroreRuolo=false;

                String ruoloSelezionato;

                if(guida.isChecked()){
                    ruoloSelezionato="guida";
                    datiRegistrazioneUtente.putString("ruolo", ruoloSelezionato);
                    datiRegistrazioneUtente.apply();

                }else if(curatore.isChecked()){
                    ruoloSelezionato="curatore";
                    datiRegistrazioneUtente.putString("ruolo", ruoloSelezionato);
                    datiRegistrazioneUtente.apply();

                }else if(visitatore.isChecked()){
                    ruoloSelezionato="visitatore";
                    datiRegistrazioneUtente.putString("ruolo", ruoloSelezionato);
                    datiRegistrazioneUtente.apply();

                }else{
                    erroreRuolo=true;
                    BasicMethod.alertDialog(getActivity(), "Seleziona un ruolo tra Curatore, Visitatore o Guida Turistica", "Ruolo non selezionato", "OK");
                }

                if(erroreRuolo==false){

                    FragmentRegistraDatiPersonali fragmentRegistraDatiPersonali = new FragmentRegistraDatiPersonali();

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