package com.uniba.capitool.fragments.fragmentsAggiungiPercorso;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.classes.CardSitoCulturale;
import com.uniba.capitool.classes.CardSitoCulturaleAdapter;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

import java.util.ArrayList;

/**
 *
 */
public class FragmentRicercaSiti extends Fragment implements CardSitoCulturaleAdapter.OnEventClickListener{

    Utente utente;
    private ArrayList<CardSitoCulturale> cardSitiCulturali;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ricerca_siti, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utente = ((AggiungiPercorso)getActivity()).getUtente();
        EditText valoreDiRicerca = view.findViewById(R.id.editCercaSitoCitta);

        // TODO - OTTENERE LE CARD IN TEMPO REALE
        // TODO ----------------------------------------------------------

        valoreDiRicerca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // None
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // None
            }

            @Override
            public void afterTextChanged(Editable editable) {
                popolaSitiInRecyclerView(editable.toString(), view);
            }
        });

        // TODO ----------------------------------------------------------
    }

    /***
     * Al click sulla Card, passa i dati alla Activity: SelezionaOpere
     *
     * @param position: paramentro che indica la Card cliccata
     */
    @Override
    public void onEventClick(int position) {

        // TODO - SALVARE I DATI IN SHARED PREFERENCES
        CardSitoCulturale clickedEvent = cardSitiCulturali.get(position);

        // Scrivo nel file SharedPreferences per salvare i dati
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor datiSitoAggiungiPercorso = sharedPreferences.edit();
        datiSitoAggiungiPercorso.putString("idSito", clickedEvent.getId());
        datiSitoAggiungiPercorso.putString("nomeSito", clickedEvent.getNome());
        datiSitoAggiungiPercorso.putString("cittaSito", clickedEvent.getCitta());
        datiSitoAggiungiPercorso.apply();

        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerRicercaSiti, new FragmentSelezionaOpere() );
        fragmentTransaction.commit();

    }

    /***
     * Popola la RecyclerView con i Siti Culturali cercati
     */
    public void popolaSitiInRecyclerView(String valoreDiRicerca, View view){

        RecyclerView rvCardsSiti = (RecyclerView) view.findViewById(R.id.recyclerViewSiti);

        if(!valoreDiRicerca.equals("")){
            cardSitiCulturali = getSitiFromDB(BasicMethod.toLower(valoreDiRicerca));
        } else {
            cardSitiCulturali.clear();
        }

        // Crea un adapter passando i siti trovati
        CardSitoCulturaleAdapter adapter = new CardSitoCulturaleAdapter(cardSitiCulturali);

        // Lega l'Adapter alla recyclerview per popolare i Siti
        rvCardsSiti.setAdapter(adapter);
        adapter.setOnEventClickListener(this);

        // SetLayoutManager posiziona i Siti trovati nel Layout
        rvCardsSiti.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
    /** FINE popolaSitiInRecyclerView()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Ottiene tutti i Siti, cercando con il valoreDiRicerca
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà un Sito od una Città)
     * @return
     */
    private ArrayList<CardSitoCulturale> getSitiFromDB(String valoreDiRicerca) {

        ArrayList<CardSitoCulturale> cardSitiCulturali = new ArrayList<>();

        cardSitiCulturali = getSitiFromDBOrderByNome(valoreDiRicerca);

        return cardSitiCulturali;
    }
    /** FINE getSitoFromDB()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Ottiene tutti Siti esistenti, cercando per Nome
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà un Sito)
     */
    public ArrayList<CardSitoCulturale> getSitiFromDBOrderByNome(String valoreDiRicerca) {

        final ArrayList<CardSitoCulturale>[] CardSitiCulturali = new ArrayList[]{new ArrayList<>()};
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Nome

        recentPostsQuery = myRef.child("Siti").orderByChild("nome").startAt(valoreDiRicerca).endAt(valoreDiRicerca+'\uf8ff');     //SELECT * FROM Siti WHERE nome LIKE "xyz"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SitoCulturale sitoCulturale = snapshot.getValue(SitoCulturale.class);

                    if(sitoCulturale.getZone().size() != 0){
                        CardSitoCulturale cardSitoCulturale = snapshot.getValue(CardSitoCulturale.class);
                        //Log.e("RISULTATO DB NOME: ",cardSitoCulturale.getNome());
                        CardSitiCulturali[0].add(cardSitoCulturale);
                    }
                }

                // Se non trova nulla nella ricerca per nome ...
                if(CardSitiCulturali[0].isEmpty()){
                    //Log.e("SEI DENTRO ALLA CITTA ","  ");
                    getSitiFromDBOrderByCitta(valoreDiRicerca, CardSitiCulturali);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

        return CardSitiCulturali[0];

    }
    /** FINE getSitiFromDBOrderByNome()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Ottiene tutti Siti esistenti, cercando per Città
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà una Città)
     */
    public ArrayList<CardSitoCulturale> getSitiFromDBOrderByCitta(String valoreDiRicerca, ArrayList<CardSitoCulturale>[] CardSitiCulturali) {

        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Città

        recentPostsQuery = myRef.child("Siti").orderByChild("citta").startAt(valoreDiRicerca).endAt(valoreDiRicerca+'\uf8ff');     //SELECT * FROM Siti WHERE nome LIKE "xyz"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SitoCulturale sitoCulturale = snapshot.getValue(SitoCulturale.class);

                    if(sitoCulturale.getZone().size() != 0){
                        CardSitoCulturale cardSitoCulturale = snapshot.getValue(CardSitoCulturale.class);
                        //Log.e("RISULTATO DB CITTA: ",cardSitoCulturale.getNome());
                        CardSitiCulturali[0].add(cardSitoCulturale);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

        return CardSitiCulturali[0];

    }
    /** FINE getSitiFromDBOrderByCitta()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

}