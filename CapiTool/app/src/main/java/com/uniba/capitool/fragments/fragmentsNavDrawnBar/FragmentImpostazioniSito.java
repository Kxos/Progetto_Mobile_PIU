package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.DelegaSito;
import com.uniba.capitool.activities.EliminaSito;
import com.uniba.capitool.classes.CardSitoCulturale;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Visitatore;
import com.uniba.capitool.fragments.fragmentsMioSito.FragmentInfoSito;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * create an instance of this fragment.
 */
public class FragmentImpostazioniSito extends Fragment {

    CardSitoCulturale sito ;
    DatabaseReference myRef ;



    public FragmentImpostazioniSito() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_impostazioni_sito, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Visitatore utente = getUserInfo();
        TextView delega_sito = (TextView) view.findViewById(R.id.text_delega_sito) ;
        TextView elimina_sito = view.findViewById(R.id.text_elimina_sito) ;

        checkSitoAssociatoAlCuratore(utente.getUid());

        delega_sito.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // checkSitoAssociatoAlCuratore(utente.getUid()); inserito prima dell' onClick
                Log.e("controlloSito", "Ho controllato se è associato il sito");

                if(sito == null){
                    Toast.makeText(getActivity().getApplicationContext(), R.string.toastDelegateSito, Toast.LENGTH_SHORT).show();
                    Log.e("toat_creare", "dovrebbe essere visuaizzato il toast") ;
                }else{

                    Log.e("sitoAssociato", "il curatore ha un sito associato");

                    Intent intentDelegaSito = new Intent(getActivity(), DelegaSito.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("utente", (Serializable) utente);
                    intentDelegaSito.putExtras(bundle) ;

                    /*intentDelegaSito.putExtra("uid", utente.getUid()); //Optional parameters
                    intentDelegaSito.putExtra("nome", utente.getNome()); //Optional parameters
                    intentDelegaSito.putExtra("cognome", utente.getCognome()); //Optional parameters
                    intentDelegaSito.putExtra("email", utente.getEmail()); //Optional parameters
                    intentDelegaSito.putExtra("ruolo", utente.getRuolo()); //Optional parameters*/

                    getActivity().startActivity(intentDelegaSito);
                }


            }
        });


        elimina_sito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("controlloSito", "Ho controllato se è associato il sito");

                if(sito == null){
                    Toast.makeText(getActivity().getApplicationContext(), R.string.toastDeleteSito, Toast.LENGTH_SHORT).show();
                    Log.e("toat_creare", "dovrebbe essere visuaizzato il toast") ;
                }else {

                    Intent intentEliminaSito = new Intent(getActivity(), EliminaSito.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("utente", (Serializable) utente);
                    intentEliminaSito.putExtras(bundle) ;


                        /*intentEliminaSito.putExtra("uid", utente.getUid()); //Optional parameters
                        intentEliminaSito.putExtra("nome", utente.getNome()); //Optional parameters
                        intentEliminaSito.putExtra("cognome", utente.getCognome()); //Optional parameters
                        intentEliminaSito.putExtra("email", utente.getEmail()); //Optional parameters
                        intentEliminaSito.putExtra("ruolo", utente.getRuolo()); //Optional parameters*/

                    getActivity().startActivity(intentEliminaSito);
                }
            }
        });


    }


    /***
     * Ottiene i dati dell'utente loggato
     *
     * @return utente: Ritorna l'utente valorizzato delle sue informazioni
     */
    public Visitatore getUserInfo(){

        Visitatore utente;

        if(BasicMethod.isCuratore(getActivity().getIntent().getExtras().getString("ruolo"))){
            utente = new Curatore();
        }else{
            utente = new Visitatore();
        }

        utente.setUid(getActivity().getIntent().getExtras().getString("uid"));
        utente.setNome(getActivity().getIntent().getExtras().getString("nome"));
        utente.setCognome(getActivity().getIntent().getExtras().getString("cognome"));
        utente.setEmail(getActivity().getIntent().getExtras().getString("email"));
        utente.setRuolo(getActivity().getIntent().getExtras().getString("ruolo"));

        return utente;
    }



    public void checkSitoAssociatoAlCuratore (String uidUtente){
        Log.e("*****!!!!*****","controllo se c'è un sito associato al curatore!");
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Nome

        recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(uidUtente).limitToFirst(1);     //SELECT * FROM Siti WHERE getUid LIKE "..."
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.e("*****!!!!*****","SONO NEL FOR!!!");

                    sito = snapshot.getValue(SitoCulturale.class);
                    Log.e("sonoInImpostazioniSito", "sito catturato: " + sito.getNome());

                    sito = snapshot.getValue(CardSitoCulturale.class) ;
                    Log.e("sono in ImpostazioniSito", "sito catturato: " + sito.getNome()) ;


                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });
    }








    }
