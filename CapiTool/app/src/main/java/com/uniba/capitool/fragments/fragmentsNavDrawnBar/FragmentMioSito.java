package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.fragments.fragmentsMioSito.FragmentCreaMioSito;
import com.uniba.capitool.fragments.fragmentsMioSito.FragmentInfoSito;

import java.util.Map;

public class FragmentMioSito extends Fragment {

    SitoCulturale sito = new SitoCulturale();
    DatabaseReference myRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_mio_sito, container, false);

        //BasicMethod.getUtente() da valore nullo perchè forse non è ancora valorizzato, dato che MioSito è il primo fragment
        // che viene creato all'avvio di HomePage. La soluzione è leggere con il Bundle
        controllaSitoAssociato (getActivity().getIntent().getExtras().getString("uid"));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void controllaSitoAssociato (String uidUtente){

        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Nome

        recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(uidUtente).limitToFirst(1);     //SELECT * FROM Siti WHERE getUid LIKE "..."
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /***
                 * con questo try catch provo a vedere se l'istruzione "dataSnapshot.getValue().toString()" mi da un valore diverso da null
                 * Se dovesse essere null sifgnifica che non è stato trovato nessun risultato dalla query fatta
                 * quindi raccolgo l'eccezione e faccio visualizzare all'utente il fragment CreaMioSito
                 * Altrimenti leggo il sito associato all'utente
                 */
                try{
                    if(dataSnapshot.getValue().toString() != null){
                        Log.e("Try dataSnapshot","La query NON ha restituito valore null. Quindi c'è un sito associato all'utente");

                        // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                            /***
                             * Uso un try catch perché si potrebbe verificare un errore nella lettura del SitoCulturale
                             * Se si verifica gestisco l'errore leggendo il sito in un altro modo
                             * Se non si verifica leggo il sito Culturale nel metodo classico
                             */
                            try{
                                Log.e("Metodo STANDARD","Nome sito culturale trovato: "+snapshot.getValue(SitoCulturale.class).getNome());

                                SitoCulturale sitoCulturale= snapshot.getValue(SitoCulturale.class);
                                setSito(sitoCulturale);

                            }catch (Exception e){
                                Log.e("Metodo ALTERNATIVO (Gestione stringa snapshot)","");

                                String idSito = leggiIdSito(snapshot.getValue().toString());
                                recuperaSitoFromDB(idSito);

                            }
                        }
                    }

                } catch (Exception e){
                    Log.e("Exception e dataSnapshot","La query ha restituito valore null. Quindi non c'è nessun sito associato all'utente");
                    setCreaSito();
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
     * Questo metodo gestisce la stringa snapshot e trova l'id del sito
     * Il for parte da "posizione+3" perché i 2 caratteri successivi al LastIndexOf("id") sono: d=
     * Esempio ("id=1" quindi "d=" i successivi 2 char)
     * @param snapshot (stringa ricevuta da firebase dalla quale trovare, tramite gestione delle stringhe, l'id del sito)
     * @return idSito
     */
    private String leggiIdSito(String snapshot) {

        int posizione=snapshot.lastIndexOf("id");

        String idSito="";

        for(int i=posizione+3; i<snapshot.length(); i++){
            if(snapshot.charAt(i)!=','){
                idSito=idSito+snapshot.charAt(i);
            }else{
                i=snapshot.length();
            }
        }

        Log.e("idSito trovato", idSito);

        return idSito;
    }

    /***
     * Con questo metodo leggo direttamente dal DB i dati del sito evitando così la query con snapshot che ha dato problemi ed è stata catturata dal catch
     * task.getResult().getValue() è una mappa annidata
     * Grazie al task.getResult().getValue() posso leggere dato per dato del sito culturale e salvarlo in un oggetto di classe SitoCulturale
     * @param idSito
     */
    private void recuperaSitoFromDB(String idSito) {

        SitoCulturale sitoCulturale=new SitoCulturale();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/Siti/"+idSito);

        //metodo di letture dal db diretto
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("Dati estratti dal nodo:", String.valueOf(task.getResult().getValue()));

                    Map<String, Map<String, Object>> risultato ;

                    risultato= (Map<String, Map<String, Object>>) task.getResult().getValue();  //funziona solo se al nodo ci sono valori alfanumerici altrimenti ci vuole una LISTA e non MAPPA

                    sitoCulturale.setNome(String.valueOf(risultato.get("nome")));
                    sitoCulturale.setIndirizzo(String.valueOf(risultato.get("indirizzo")));
                    sitoCulturale.setOrarioChiusura(String.valueOf(risultato.get("orarioChiusura")));
                    sitoCulturale.setOrarioApertura(String.valueOf(risultato.get("orarioApertura")));
                    sitoCulturale.setCitta(String.valueOf(risultato.get("citta")));
                    sitoCulturale.setCostoBiglietto(String.valueOf(risultato.get("costoBiglietto")));
                    sitoCulturale.setId(String.valueOf(risultato.get("id")));
                    sitoCulturale.setUidCuratore(String.valueOf(risultato.get("uidCuratore")));

                    Log.d("Sito Salvato", ""+sitoCulturale.getNome());

                    setSito(sitoCulturale);

                }
            }
        });

    }

    public void setSito(SitoCulturale sitoTrovato){

        this.sito = sitoTrovato;

        if(BasicMethod.getUtente().getRuolo().equals("curatore") && sito.getUidCuratore() != null ){

            FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            FragmentInfoSito fragmentInfoSito = new FragmentInfoSito();

            Bundle bundle = new Bundle();
            SitoCulturale sitoCulturale = sito;
            bundle.putSerializable("sito", sitoCulturale);
            fragmentInfoSito.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragmentContainerSito, fragmentInfoSito);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }else{

        }

    }

    /***
     * Se non è stato trovato nessun sito associato all'utente, visualizzo il FragmentCreaMioSito
     */
    private void setCreaSito() {
        Log.e("*****!!!!*****","SITO NON ASSOCIATOOO NON DEFINITIVO"+BasicMethod.getUtente().getUid()+""+sito.getUidCuratore() );
        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerSito, new FragmentCreaMioSito() );
        fragmentTransaction.commit();
    }

    public SitoCulturale getSito(){
        return sito;
    }

}