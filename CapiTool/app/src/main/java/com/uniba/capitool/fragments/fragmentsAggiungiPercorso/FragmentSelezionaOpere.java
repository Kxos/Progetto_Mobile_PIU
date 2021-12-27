package com.uniba.capitool.fragments.fragmentsAggiungiPercorso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.classes.CardOpera;
import com.uniba.capitool.classes.CardOperaAdapter;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Zona;

import java.util.ArrayList;

public class FragmentSelezionaOpere extends Fragment {

    private Toolbar toolbar;
    private View viewActivity;
    private static int numeroZoneSIZE = 100;
    private int countZone = 0;
    private CardOperaAdapter adapter;
    private ArrayList<CardOpera> listaOpereChecked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seleziona_opere, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.viewActivity = view;

        toolbar = ((AggiungiPercorso)getActivity()).getToolbar();

        // Leggo dal file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(sharedPreferences!=null){

            SitoCulturale sitoCulturale = new SitoCulturale();

            sitoCulturale.setId(sharedPreferences.getString("idSito", ""));
            sitoCulturale.setNome(sharedPreferences.getString("nomeSito", ""));

            toolbar.setTitle(getString(R.string.site) + " - " + sitoCulturale.getNome());

            recuperaZoneFromDBOrderByZoneId(sitoCulturale.getId());

        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

    }

    /***
     * Ottiene tutte le Zone esistenti di un Sito
     *
     * @param idSito: Id del Sito selezionato nel Fragment Precedente (Tramite Card)
     */
    public void recuperaZoneFromDBOrderByZoneId(String idSito) {

        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Zone

        recentPostsQuery = myRef.child("Siti/"+idSito+"/Zone").orderByChild("id");     //SELECT Zone FROM Siti WHERE id = idSito
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Zona> listaZone = new ArrayList<>();

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Zona zona = snapshot.getValue(Zona.class);
                    //Log.e("RISULTATO DB NOME: ",zona.getNome());
                    listaZone.add(zona);
                }

                mostraOperePerZone(listaZone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }
    /** FINE recuperaZoneFromDBOrderByZoneId()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     *
     * @param listaZone: Lista delle Zone valorizzate in recuperaZoneFromDBOrderByZoneId()
     */
    public void mostraOperePerZone(ArrayList<Zona> listaZone){

        int numeroOpere =  listaZone.get(0).getOpere().size() -1; // -1 Perchè inizia da 0
        ArrayList<CardOpera>[] listaOpere = new ArrayList[numeroZoneSIZE];

        // Inizializzo listaOpere
        for (int i = 0; i < numeroZoneSIZE; i++) {
            listaOpere[i] = new ArrayList<CardOpera>();
        }

        Log.e("Numero di Opere trovate: ",""+numeroOpere);
        Log.e("Numero di Zone trovate: ",""+listaZone.size());

        int count=0;
        for(int countZone = 0; countZone!=listaZone.size();countZone++){

            for (Opera opera : listaZone.get(countZone).getOpere()) {
                if(count!=0){
                    CardOpera cardOpera = new CardOpera();
                    cardOpera.setId(opera.getId());
                    cardOpera.setTitolo(opera.getTitolo());
                    cardOpera.setFoto(opera.getFoto());

                    View cardOperaView = getLayoutInflater().inflate(R.layout.card_opera, null);
                    cardOpera.setCheckBox(cardOperaView.findViewById(R.id.checkOperaSelezionata));
                    listaOpere[countZone].add(cardOpera);
                }
                count++;
            }
            count=0;
        }

        TextView nomeZona = viewActivity.findViewById(R.id.lableNomeZona);
        nomeZona.setText(listaZone.get(countZone).getNome());

        Button buttonPrecedenteZona = viewActivity.findViewById(R.id.buttonPrecedenteZona);
        Button buttonSuccessivaZona = viewActivity.findViewById(R.id.buttonSuccessivaZona);
        buttonPrecedenteZona.setVisibility(View.INVISIBLE);

        adapter = popolaOpereInRecyclerView(listaOpere[0]);

        // TODO: Pulsanti Avanti e Indietro (Modificano il contatore, ripulendo volta per volta la RecycleView)

        // Pulsante Avanti
        buttonSuccessivaZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Log.e("CONTATORE: ",""+countZone);

                if(countZone +1 < listaZone.size()){
                    
                    // TODO: INCORRETTA GESTIONE DI addAll()
                    if(adapter != null){
                        if(listaOpereChecked != null){
                            listaOpereChecked.addAll(adapter.getListaOpereChecked());
                        }else{
                            listaOpereChecked = adapter.getListaOpereChecked();
                        }
                    }

                    Log.e("Esistono opere checked: ", ""+listaOpereChecked);
                    //listaOpere[countZone] = spuntaCheckboxDelleOpereChecked(listaOpere[countZone], listaOpereChecked, adapter);

                    countZone = countZone +1;
                    adapter = popolaOpereInRecyclerView(listaOpere[countZone]);
                    buttonPrecedenteZona.setVisibility(View.VISIBLE);
                    nomeZona.setText(listaZone.get(countZone).getNome());
                }

            }
        });

        // Pulsante Indietro
        buttonPrecedenteZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("CONTATORE: ",""+countZone);

                if(countZone -1 >= 0){

                    // TODO: INCORRETTA GESTIONE DI addAll()
                    if(adapter != null){
                        if(listaOpereChecked != null){
                            listaOpereChecked.addAll(adapter.getListaOpereChecked());
                        }else{
                            listaOpereChecked = adapter.getListaOpereChecked();
                        }
                    }

                    Log.e("Esistono opere checked: ", ""+listaOpereChecked);
                    //listaOpere[countZone] = spuntaCheckboxDelleOpereChecked(listaOpere[countZone], listaOpereChecked, adapter);

                    countZone = countZone -1;
                    adapter = popolaOpereInRecyclerView(listaOpere[countZone]);
                    nomeZona.setText(listaZone.get(countZone).getNome());

                    if(countZone == 0){ buttonPrecedenteZona.setVisibility(View.INVISIBLE); }
                }

            }
        });

    }

    private ArrayList<CardOpera> spuntaCheckboxDelleOpereChecked(ArrayList<CardOpera> listaOpere, ArrayList<CardOpera> listaOpereChecked, CardOperaAdapter adapter) {

      if(listaOpereChecked.get(0).getCheckBox().isChecked()){
          listaOpereChecked.get(0).getCheckBox().setChecked(true);
      }

      for(int i = 0; i < listaOpere.size(); i++){

          for(int j = 0; j < listaOpereChecked.size(); j++){

              if(listaOpere.contains(listaOpereChecked.get(j))){
                  Log.e("UELAAAAAAAAAAAAAAA>: ","");
                  listaOpere.get(listaOpere.indexOf(listaOpereChecked.get(j))).getCheckBox().setChecked(true);
              }

          }

      }

        return listaOpere;
    }

    /***
     * Popola la RecyclerView con le Opere di una Zona
     *
     * @param listaOpere: Lita di tutte le opere di una Zona
     */
    public CardOperaAdapter popolaOpereInRecyclerView(ArrayList<CardOpera> listaOpere){

        RecyclerView rvCardsOpere = (RecyclerView) viewActivity.findViewById(R.id.recyclerViewOpere);

        if(!listaOpere.isEmpty()){

            // Crea un adapter passando i siti trovati
            CardOperaAdapter adapter = new CardOperaAdapter(listaOpere);

            // Lega l'Adapter alla recyclerview per popolare i Siti
            rvCardsOpere.setAdapter(adapter);
            //adapter.setOnEventClickListener(this);

            // SetLayoutManager posiziona i Siti trovati nel Layout
            rvCardsOpere.setLayoutManager(new LinearLayoutManager(this.getContext()));

            return adapter;
        }

        return null;
    }
    /** FINE popolaOpereInRecyclerView()
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

}