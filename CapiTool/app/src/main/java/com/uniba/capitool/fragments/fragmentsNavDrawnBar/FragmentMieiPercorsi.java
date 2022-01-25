package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.classes.CardPercorso;
import com.uniba.capitool.classes.CardPercorsoAdapter;
import com.uniba.capitool.classes.CardSitoCulturale;
import com.uniba.capitool.classes.Utente;

import java.util.ArrayList;

/**

 * create an instance of this fragment.
 */
public class FragmentMieiPercorsi extends Fragment {

    @Override
    public void onResume() {
        super.onResume();

        Utente utente = getUserInfo();
        View view = getView(); // Riottengo la View

        if (view != null) {
            //Ricarico la RecyclerView dopo aver creato un nuovo percorso
            cercaPercorsiFromDB(utente.getUid(), view);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Utente utente = getUserInfo();
        View v = inflater.inflate(R.layout.fragment_miei_percorsi, container, false);

        FloatingActionButton addPercorso = v.findViewById(R.id.buttonAddPercorso);
        addPercorso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aggiungiPercorso = BasicMethod.putUtenteExtrasInIntent(getActivity(),utente,AggiungiPercorso.class);
                getActivity().startActivity(aggiungiPercorso);
            }
        });

        cercaPercorsiFromDB(utente.getUid(), v);

        EditText valoreDiRicercaPercorso = v.findViewById(R.id.editCercaNomePercorso);
        valoreDiRicercaPercorso.addTextChangedListener(new TextWatcher() {
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

                cercaPercorsoFromDBDalNome(editable.toString(), v);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    /***
     * Cerca un singolo Percorso tramite il suo nome, per popolarlo singolarmente nella RecyclerView
     *
     * @param nomePercorso
     * @param view
     */
    public void cercaPercorsoFromDBDalNome(String nomePercorso, View view){

        final ArrayList<CardPercorso> listaPercorsi = new ArrayList<>();
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Nome

        recentPostsQuery = myRef.child("Percorsi").orderByChild("nome").startAt(nomePercorso).endAt(nomePercorso+'\uf8ff');     //SELECT * FROM Percorsi WHERE nome LIKE "nomePercorso"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    CardPercorso cardPercorso = snapshot.getValue(CardPercorso.class);
                    listaPercorsi.add(cardPercorso);

                }

                    popolaPercorsiInRecyclerView(listaPercorsi,view);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }

    /***
     * Se ci sono dei Percorsi creati dall'utente allora imposta la recycle View con le cards, altrimenti lascia l'immagine Predefinita
     *
     * @param idUtente
     * @param view
     */
    private void cercaPercorsiFromDB(String idUtente, View view) {

        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("Percorsi/").orderByChild("idUtente").equalTo(idUtente);
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
                    ConstraintLayout layoutImmagineNuovoPercorso = view.findViewById(R.id.layoutImmagineNuovoPercorso);
                    layoutImmagineNuovoPercorso.setVisibility(View.INVISIBLE);

                    RelativeLayout layoutRecyclerViewPercorsi = view.findViewById(R.id.layoutRecyclerViewPercorsi);
                    layoutRecyclerViewPercorsi.setVisibility(View.VISIBLE);

                    popolaPercorsiInRecyclerView(listaPercorsi,view);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }

    /***
     * Popola la RecyclerView con i Percorsi dell'Utente
     *
     * @param listaPercorsi: Lita di tutti i Percorsi di un Utente
     */
    public CardPercorsoAdapter popolaPercorsiInRecyclerView(ArrayList<CardPercorso> listaPercorsi, View view){

        RecyclerView rvCardsPercorsi = (RecyclerView) view.findViewById(R.id.recyclerViewPercorsi);

        if(!listaPercorsi.isEmpty()){

            // Crea un adapter passando i Percorsi trovati
            CardPercorsoAdapter adapter = new CardPercorsoAdapter(listaPercorsi);

            // Lega l'Adapter alla recyclerview per popolare i Percorsi
            rvCardsPercorsi.setAdapter(adapter);

            // SetLayoutManager posiziona i Percorsi trovati nel Layout
            rvCardsPercorsi.setLayoutManager(new LinearLayoutManager(this.getContext()));

            return adapter;
        }

        return null;
    }
    /** FINE popolaPercorsiInRecyclerView()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Ottiene i dati dell'utente loggato (abbiamo impostato come primo fragment MioSito per il Curatore).
     *                                      Il problema riscontato è che se il fragment in questione è il primo ad essere istanziato della HomePage, BasicMethod.getUtente() da valore nullo.
     *                                      Non viene valorizzato in tempo)
     *
     * @return utente: Ritorna l'utente valorizzato delle sue informazioni
     */
    public Utente getUserInfo(){

        Utente utente = new Utente();

        utente.setUid(getActivity().getIntent().getExtras().getString("uid"));
        utente.setNome(getActivity().getIntent().getExtras().getString("nome"));
        utente.setCognome(getActivity().getIntent().getExtras().getString("cognome"));
        utente.setEmail(getActivity().getIntent().getExtras().getString("email"));
        utente.setRuolo(getActivity().getIntent().getExtras().getString("ruolo"));

        return utente;
    }
}