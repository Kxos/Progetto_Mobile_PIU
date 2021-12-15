package com.uniba.capitool.fragments.fragmentsProfilo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.classes.Visitatore;

import java.util.ArrayList;
import java.util.List;

public class FragmentDatiPersonali extends Fragment {

    TextInputEditText dataNascita;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dati_personali, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText email= view.findViewById(R.id.text_email);
        TextInputEditText nome= view.findViewById(R.id.text_nome);
        TextInputEditText cognome= view.findViewById(R.id.text_cognome);
        dataNascita= view.findViewById(R.id.text_dataNascita);
        TextInputEditText patentino= view.findViewById(R.id.text_numeroPatentino);

        Button conferma= view.findViewById(R.id.confermaModifiche);

        Visitatore utente=((HomePage)getActivity()).getUtente();    //recupero l'utente che ha fatto il login dalla activity HomePage
        email.setText(utente.getEmail());
        nome.setText(utente.getNome());
        cognome.setText(utente.getCognome());



        if(!utente.getRuolo().equals("guida")){
            patentino.setHint("");
            patentino.setEnabled(false);
            patentino.setBackground(null);
            patentino.setFocusable(false);
        }

        setDataNascitaUtente(utente.getEmail());





    }


    public void setDataNascitaUtente(String email){

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("/");

            Query recentPostsQuery = myRef.child("Utenti").orderByChild("email").equalTo(email);     //SELECT * FROM Utenti WHERE ruolo="visitatore"
            recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    List<Visitatore> visitatori=new ArrayList<>();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i valori trovati dalla query, anche se è 1 solo
                        Visitatore visitatore=snapshot.getValue(Visitatore.class);  //così facendo si associa un l'oggetto della query nell'oggetto della classe
                        visitatori.add(visitatore);                                 //se ci sarà più di un risultato nella query creiamo una lista di oggetti per gestirli da codice
                    }

                    //setto nella EditText il valore della data trovata
                    setDataNascita(visitatori.get(0).getDataNascita());
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Getting Post failed, log a message
                    Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
                }
            });
    }

    private void setDataNascita(String dataN) {
        dataNascita.setText(dataN);
    }

}