package com.uniba.capitool;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uniba.capitool.classes.Visitatore;


public class FragmentRegistraDatiPersonali extends Fragment {

    EditText username;
    EditText nome;
    EditText cognome;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registra_dati_personali, container, false);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            // handle your code here.
            Log.d("onCreateView: ", bundle.get("email").toString());
            //bundle.putString("nome",nome.getText().toString());
           // bundle.putString("cognome",cognome.getText().toString());
           // bundle.putString("dataNascita",username.getText().toString());
        }else{
            //Bundle bundle = new Bundle();
            //bundle.putString("ruolo",ruolo.getText().toString()); // Put anything what you want
        }
        username=v.findViewById(R.id.username2);
        nome=v.findViewById(R.id.nome);
        cognome=v.findViewById(R.id.surname);

        Button conferma= v.findViewById(R.id.conferma);

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }

    public void insertQuery(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://e-culture-tool-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");



      /*  //INSERT di un nuovo oggetto
        Visitatore visitatore= new Visitatore("18/11/99", "visitatore", "De Matteis", "5985", "Vincenzo", "vitoiann@gmail.com", "vito56");
        myRef=database.getReference("/Utenti/9/");
        myRef.setValue(visitatore);*/
    }
}