package com.uniba.capitool.fragments.fragmentsAggiungiInfoSito;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.uniba.capitool.R;


public class AggiungiInfoSito extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_aggiungi_info_sito, container, false);
        EditText nomeCitta = v.findViewById(R.id.Città);
        EditText orarioChiusura =v.findViewById(R.id.OrarioChiusura);
        EditText orarioApertura =v.findViewById(R.id.OrarioApertura);
        EditText costoIngresso =v.findViewById(R.id.CostoIngresso);
        EditText indirizzo =v.findViewById(R.id.Indirizzo);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences!=null){
            String cittaTrovata = sharedPreferences.getString("citta", "");
            String orarioChiusuraTrovato =  sharedPreferences.getString("orarioChiusura", "");
            String orarioAperturaTrovato =  sharedPreferences.getString("orarioApertura", "");
            String costoIngressoTrovato =  sharedPreferences.getString("costoIngresso", "");
            String indirizzoTrovato = sharedPreferences.getString("indirizzo", "");

            nomeCitta.setText(cittaTrovata);
            orarioChiusura.setText(orarioChiusuraTrovato);
            orarioApertura.setText(orarioAperturaTrovato);
            costoIngresso.setText(costoIngressoTrovato);
            indirizzo.setText(indirizzoTrovato);

        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

        SharedPreferences.Editor datiSito = sharedPreferences.edit();

    nomeCitta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("citta", nomeCitta.getText().toString());
                datiSito.apply();
            }
        });

        orarioChiusura.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("orarioChiusura", orarioChiusura.getText().toString());
                datiSito.apply();
            }
        });

        orarioApertura.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("orarioApertura", orarioApertura.getText().toString());
                datiSito.apply();
            }
        });

        costoIngresso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("costoIngresso", costoIngresso.getText().toString());
                datiSito.apply();
            }
        });

        indirizzo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("indirizzo", indirizzo.getText().toString());
                datiSito.apply();
            }
        });



        // Inflate the layout for this fragment
        return v;
    }
}

