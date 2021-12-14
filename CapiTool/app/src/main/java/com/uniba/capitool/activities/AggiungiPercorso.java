package com.uniba.capitool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.CardSitoCulturale;
import com.uniba.capitool.classes.CardSitoCulturaleAdapter;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.Visitatore;

import java.util.ArrayList;

public class AggiungiPercorso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_percorso);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Aggiungi Percorso");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        Visitatore utente;

        if(BasicMethod.isCuratore(b.getString("ruolo"))){
            utente = new Visitatore();
        }else{
            utente = new Curatore();
        }

        utente.setUid(b.getString("uid"));
        utente.setNome(b.getString("nome"));
        utente.setCognome(b.getString("cognome"));
        utente.setEmail(b.getString("email"));
        utente.setRuolo(b.getString("ruolo"));

        TextView valoreDiRicerca = findViewById(R.id.editCercaSitoCitta);

        // TODO - OTTENERE LE CARD IN TEMPO REALE
        // TODO - RIMUOVERE LA TASTIERA
        // TODO ----------------------------------------------------------

        valoreDiRicerca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // None
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                popolaSitiInRecyclerView(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Noneg
            }
        });

        // TODO ----------------------------------------------------------
    }

    /***
     * Popola la RecyclerView con i Siti Culturali cercati
     */
    public void popolaSitiInRecyclerView(String valoreDiRicerca){

        RecyclerView rvCardsSiti = (RecyclerView) findViewById(R.id.recyclerViewSiti);

        ArrayList<CardSitoCulturale> cardSitiCulturali = new ArrayList<>();

        // TODO - INSERIMENTO DELLE INFO SU CARD
        // TODO ----------------------------------------------------------

        /**
        // Creazione di una singola card
        CardSitoCulturale cardSitoCulturale = new CardSitoCulturale();
        cardSitoCulturale.setNome("Sito di Prova 1");
        cardSitoCulturale.setDescrizione("Non è importante al momento.");
        cardSitoCulturale.setCitta("Bari");
        cardSitiCulturali.add(cardSitoCulturale);
         */

        if(!valoreDiRicerca.isEmpty()){
            cardSitiCulturali = getSitoFromDB(valoreDiRicerca);
        }

        // TODO ----------------------------------------------------------

        // Crea un adapter passando i siti trovati
        CardSitoCulturaleAdapter adapter = new CardSitoCulturaleAdapter(cardSitiCulturali);

        // Lega l'Adapter alla recyclerview per popolare i Siti
        rvCardsSiti.setAdapter(adapter);
        // SetLayoutManager posiziona i Siti trovati nel Layout
        rvCardsSiti.setLayoutManager(new LinearLayoutManager(this));
    }

    /***
     * Ottiene tutti gli attributi di un Sito
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà un Sito od una Città)
     * @return
     */
    private ArrayList<CardSitoCulturale> getSitoFromDB(String valoreDiRicerca) {

        ArrayList<CardSitoCulturale> cardSitiCulturali = new ArrayList<>();

        cardSitiCulturali = getSitoFromDBOrderByNome(valoreDiRicerca);

        // Non trova nulla nella ricerca per nome
        if(cardSitiCulturali.isEmpty()){
            cardSitiCulturali = getSitoFromDBOrderByCitta(valoreDiRicerca);
        }

        return cardSitiCulturali;
    }


    /***
     * Ottiene tutti gli attributi di un Sito, cercando per Nome
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà un Sito)
     */
    public ArrayList<CardSitoCulturale> getSitoFromDBOrderByNome(String valoreDiRicerca) {

        ArrayList<CardSitoCulturale> CardSitiCulturali = new ArrayList<>();
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
                    CardSitoCulturale cardSitoCulturale = snapshot.getValue(CardSitoCulturale.class);
                    Log.e("RISULTATO DB NOME: ",cardSitoCulturale.getNome());
                    CardSitiCulturali.add(cardSitoCulturale);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

        return CardSitiCulturali;

    }

    /***
     * Ottiene tutti gli attributi di un Sito, cercando per Città
     *
     * @param valoreDiRicerca: parametro su cui effettuare la ricerca (Sarà una Città)
     */
    public ArrayList<CardSitoCulturale> getSitoFromDBOrderByCitta(String valoreDiRicerca) {

        ArrayList<CardSitoCulturale> cardSitiCulturali = new ArrayList<>();
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
                    CardSitoCulturale cardSitoCulturale = snapshot.getValue(CardSitoCulturale.class);
                    Log.e("RISULTATO DB CITTA: ",cardSitoCulturale.getNome());
                    cardSitiCulturali.add(cardSitoCulturale);
                }

                /**
                 //ciclo for per scorrere la lista ottenuta dal db
                 int contatore=0;
                 for(Visitatore visitatore : visitatori){
                 Log.e("OGGETTO RESTITUITO "+(contatore+1)+" della lista visitatori", BasicMethod.getAllVisitatore(visitatori.get(contatore)));
                 contatore++;
                 }
                 */

                //Log.d("Lunghezza lista della query", String.valueOf(visitatori.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

        return cardSitiCulturali;

    }
}