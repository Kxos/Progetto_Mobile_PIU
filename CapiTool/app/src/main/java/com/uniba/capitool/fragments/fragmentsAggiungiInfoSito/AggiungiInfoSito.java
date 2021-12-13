package com.uniba.capitool.fragments.fragmentsAggiungiInfoSito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Visitatore;

import java.util.ArrayList;


public class AggiungiInfoSito extends Fragment {
    SitoCulturale sitoCulturale;
    String nomeSito;
    EditText nomeCitta;
    EditText orarioChiusura;
    EditText orarioApertura;
    EditText costoIngresso;
    EditText indirizzo;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_aggiungi_info_sito, container, false);
        nomeCitta = v.findViewById(R.id.Città);
        orarioChiusura =v.findViewById(R.id.OrarioChiusura);
        orarioApertura =v.findViewById(R.id.OrarioApertura);
        costoIngresso =v.findViewById(R.id.CostoIngresso);
        indirizzo =v.findViewById(R.id.Indirizzo);
        Button btnConferma = v.findViewById(R.id.btnConferma);

        sitoCulturale = new SitoCulturale();

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences!=null){
            String cittaTrovata = sharedPreferences.getString("citta", "");
            String orarioChiusuraTrovato =  sharedPreferences.getString("orarioChiusura", "");
            String orarioAperturaTrovato =  sharedPreferences.getString("orarioApertura", "");
            String costoIngressoTrovato =  sharedPreferences.getString("costoIngresso", "");
            String indirizzoTrovato = sharedPreferences.getString("indirizzo", "");
            nomeSito = sharedPreferences.getString("nome","");

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



        btnConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSitoOnLastPosition();
            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    public int addSitoOnLastPosition(){

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        Query recentPostsQuery = myRef.child("Siti");     //SELECT * FROM Siti

        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("ChildrenCount di "+"/Siti", String.valueOf(dataSnapshot.getChildrenCount()));   //conta tutti i figli del nodo Utenti
                insertQuery(dataSnapshot.getChildrenCount(), database, myRef);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

        return 0;
    }

    public void insertQuery(long counterIndex,  FirebaseDatabase database, DatabaseReference myRef){



        //INSERT di un nuovo oggetto
        SitoCulturale sito= new SitoCulturale((int)counterIndex,nomeSito, indirizzo.getText().toString(), orarioApertura.getText().toString(),
                                                orarioChiusura.getText().toString(), Float.parseFloat(costoIngresso.getText().toString()),
                                                nomeCitta.getText().toString(), mAuth.getCurrentUser().getUid());

        myRef=database.getReference("/Siti/"+counterIndex);
        myRef.setValue(sito);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent homePage = new Intent(getActivity(), HomePage.class);

                // Log.d("*******************************", ""+emailTrovata+""+ nome.getText().toString()+""+cognome.getText().toString()+""+user.getUid());
                getActivity().startActivity(homePage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

