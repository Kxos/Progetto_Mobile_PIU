package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

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

public class FragmentConsigliati extends Fragment {

    private ArrayList<CardPercorso> listaPercorsi;

    public FragmentConsigliati() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consigliati, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
                popolaPercorsiInRecyclerView(editable.toString(), view);
            }
        });

    }

    /***
     * Popola la RecyclerView con i Percorsi delle guide PUBBLICI cercati
     */
    public void popolaPercorsiInRecyclerView(String valoreDiRicerca, View view){

        RecyclerView rvCardsSiti = (RecyclerView) view.findViewById(R.id.recyclerViewPercorsi);

        if(!valoreDiRicerca.equals("")){
            listaPercorsi = getPercorsiFromDB(BasicMethod.toLower(valoreDiRicerca));
        } else {
            listaPercorsi.clear();
        }

        // Crea un adapter passando i siti trovati
        CardPercorsoAdapter adapter = new CardPercorsoAdapter(listaPercorsi);

        // Lega l'Adapter alla recyclerview per popolare i Siti
        rvCardsSiti.setAdapter(adapter);

        // SetLayoutManager posiziona i Siti trovati nel Layout
        rvCardsSiti.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
    /** FINE popolaSitiInRecyclerView()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Ottiene tutti i Percorsi delle guide PUBBLICI, cercando con il valoreDiRicerca
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà un Sito od una Città)
     * @return
     */
    private ArrayList<CardPercorso> getPercorsiFromDB(String valoreDiRicerca) {

        ArrayList<CardPercorso> listaPercorsi = new ArrayList<>();

        listaPercorsi = getPercorsiFromDBOrderByNomeSitoAssociato(valoreDiRicerca);

        return listaPercorsi;
    }
    /** FINE getSitoFromDB()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Ottiene tutti i Percorsi delle guide PUBBLICI esistenti, cercando per il Nome del sito associato
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà un Sito)
     */
    public ArrayList<CardPercorso> getPercorsiFromDBOrderByNomeSitoAssociato(String valoreDiRicerca) {

        final ArrayList<CardPercorso>[] listaPercorsi = new ArrayList[]{new ArrayList<>()};
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("Percorsi").orderByChild("nomeSitoAssociato").startAt(valoreDiRicerca).endAt(valoreDiRicerca+'\uf8ff');     //SELECT * FROM Percorsi WHERE nomeSitoAssociato LIKE "xyz"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Percorso percorso = snapshot.getValue(Percorso.class);
                    if(percorso.isPubblico()){

                        CardPercorso cardPercorso = snapshot.getValue(CardPercorso.class);
                        listaPercorsi[0].add(cardPercorso);

                    }

                }

                if(dataSnapshot.getValue() == null){
                    getPercorsiFromDBOrderByCittaSitoAssociato(valoreDiRicerca, listaPercorsi);
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
     *
     * Ottiene tutti i Percorsi delle guide PUBBLICI esistenti, cercando per la Città del sito associato
     *
     * @param valoreDiRicerca
     * @param listaPercorsi
     * @return
     */
    public ArrayList<CardPercorso> getPercorsiFromDBOrderByCittaSitoAssociato(String valoreDiRicerca, ArrayList<CardPercorso>[] listaPercorsi) {

        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("Percorsi").orderByChild("cittaSitoAssociato").startAt(valoreDiRicerca).endAt(valoreDiRicerca+'\uf8ff');     //SELECT * FROM Percorsi WHERE cittaSitoAssociato LIKE "xyz"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Percorso percorso = snapshot.getValue(Percorso.class);
                    if(percorso.isPubblico()){

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

}