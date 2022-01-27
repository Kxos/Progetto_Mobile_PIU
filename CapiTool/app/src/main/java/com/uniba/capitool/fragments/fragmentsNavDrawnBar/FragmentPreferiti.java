package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.classes.CardPercorso;
import com.uniba.capitool.classes.CardPercorsoAdapter;
import com.uniba.capitool.classes.Percorso;

import java.util.ArrayList;

/**
 * create an instance of this fragment.
 */
public class FragmentPreferiti extends Fragment {

    private ArrayList<CardPercorso> listaPercorsi;

    public FragmentPreferiti() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preferiti, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cercaPercorsiPreferitiFromDB(BasicMethod.getUtente().getUid(), view, this.getContext());

        EditText valoreDiRicerca = view.findViewById(R.id.editCercaNomePercorso);
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

                // TODO - VISUALIZZARE I SOLI PERCORSI PREFERITI DALL'UTENTE CORRENTE
                popolaPercorsiInRecyclerView(editable.toString(), view);
            }
        });

    }

    /***
     * Popola la RecyclerView con i Percorsi cercati, preferiti dell'utente
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà un Sito od una Città)
     * @param view
     */
    public void popolaPercorsiInRecyclerView(String valoreDiRicerca, View view){

        RecyclerView rvCardsSiti = (RecyclerView) view.findViewById(R.id.recyclerViewPercorsi);

        if(!valoreDiRicerca.equals("")){
            listaPercorsi = getPercorsiFromDB(BasicMethod.toLower(valoreDiRicerca));
        } else {
            listaPercorsi.clear();
        }

        // Crea un adapter passando i Percorsi trovati
        CardPercorsoAdapter adapter = new CardPercorsoAdapter(listaPercorsi, "Preferiti", view, this.getContext());

        // Lega l'Adapter alla recyclerview per popolare i Percorsi
        rvCardsSiti.setAdapter(adapter);

        // SetLayoutManager posiziona i Percorsi trovati nel Layout
        rvCardsSiti.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
    /** FINE
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */


    /***
     * Ottiene tutti i Percorsi preferiti dell'utente, cercando con il valoreDiRicerca
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà un Sito od una Città)
     * @return listaPercorsi
     */
    private ArrayList<CardPercorso> getPercorsiFromDB(String valoreDiRicerca) {

        ArrayList<CardPercorso> listaPercorsi = new ArrayList<>();

        listaPercorsi = getPercorsiPreferitiFromDBOrderByNomeSitoAssociato(valoreDiRicerca);

        return listaPercorsi;
    }
    /** FINE
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Ottiene tutti i Percorsi scelti tra i preferiti, cercando per il Nome del sito associato
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà un Sito)
     * @return listaPercorsi
     */
    public ArrayList<CardPercorso> getPercorsiPreferitiFromDBOrderByNomeSitoAssociato(String valoreDiRicerca) {

        final ArrayList<CardPercorso>[] listaPercorsi = new ArrayList[]{new ArrayList<>()};
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("PercorsiPreferiti").orderByChild("nomeSitoAssociato").startAt(valoreDiRicerca).endAt(valoreDiRicerca+'\uf8ff');     //SELECT * FROM PercorsiPreferiti WHERE nomeSitoAssociato LIKE "xyz"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Percorso percorso = snapshot.getValue(Percorso.class);
                    if(percorso.isPubblico() && percorso.getIdUtenteSelezionatoPercorsoTraPreferiti().equals(BasicMethod.getUtente().getUid())){

                        CardPercorso cardPercorso = snapshot.getValue(CardPercorso.class);
                        listaPercorsi[0].add(cardPercorso);

                    }

                }
                // Ha trovato dei Percorsi esistenti
                if(dataSnapshot.getValue() == null){
                    getPercorsiPreferitiFromDBOrderByCittaSitoAssociato(valoreDiRicerca, listaPercorsi);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

        return listaPercorsi[0];

    }
    /** FINE
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Ottiene tutti i Percorsi scelti tra i preferiti, cercando per la città del sito associato
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà una città)
     * @return listaPercorsi
     */
    public ArrayList<CardPercorso> getPercorsiPreferitiFromDBOrderByCittaSitoAssociato(String valoreDiRicerca, ArrayList<CardPercorso>[] listaPercorsi) {

        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("PercorsiPreferiti").orderByChild("cittaSitoAssociato").startAt(valoreDiRicerca).endAt(valoreDiRicerca+'\uf8ff');     //SELECT * FROM PercorsiPreferiti WHERE cittaSitoAssociato LIKE "xyz"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Percorso percorso = snapshot.getValue(Percorso.class);
                    if(percorso.isPubblico() && percorso.getIdUtenteSelezionatoPercorsoTraPreferiti().equals(BasicMethod.getUtente().getUid())){

                        CardPercorso cardPercorso = snapshot.getValue(CardPercorso.class);
                        listaPercorsi[0].add(cardPercorso);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

        return listaPercorsi[0];

    }
    /** FINE
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Se ci sono dei Percorsi Preferiti dall'utente allora imposta la recycle View con le cards
     *
     * @param idUtente
     * @param view
     * @param context
     */
    private void cercaPercorsiPreferitiFromDB(String idUtente, View view, Context context) {

        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("PercorsiPreferiti/").orderByChild("idUtenteSelezionatoPercorsoTraPreferiti").equalTo(idUtente);
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<CardPercorso> listaPercorsi = new ArrayList<>();

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    CardPercorso cardPercorso = snapshot.getValue(CardPercorso.class);
                    listaPercorsi.add(cardPercorso);

                }

                // Ha trovato dei Percorsi esistenti per l'utente corrente
                if(!listaPercorsi.isEmpty()){

                    //Aggiorna la RecyclerView
                    RecyclerView rvCardsSiti = (RecyclerView) view.findViewById(R.id.recyclerViewPercorsi);
                    CardPercorsoAdapter adapter = new CardPercorsoAdapter(listaPercorsi, "Preferiti", view, context);
                    rvCardsSiti.setAdapter(adapter);
                    rvCardsSiti.setLayoutManager(new LinearLayoutManager(context));
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