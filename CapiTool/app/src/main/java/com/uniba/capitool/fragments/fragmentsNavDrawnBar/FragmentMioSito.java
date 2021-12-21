package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.fragments.fragmentsMioSito.FragmentCreaMioSito;
import com.uniba.capitool.fragments.fragmentsMioSito.FragmentInfoSito;

import java.util.ArrayList;

public class FragmentMioSito extends Fragment {

    SitoCulturale sito = new SitoCulturale();
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mio_sito, container, false);

        controllaSitoAssociato (BasicMethod.getUtente().getUid());
        //Log.e("PRIMA DELL'IF", ""+sito.getNome());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("SONO DOPO ONDATA CHANGE",""+sito);

                if(BasicMethod.getUtente().getRuolo().equals("curatore") && sito.getUidCuratore() != null ){
                    Log.e("*****!!!!*****","SITO ASSOCIATOOO DEFINITIVO");

                    FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerSito, new FragmentInfoSito() );
                    fragmentTransaction.commit();

                }else{
                    Log.e("*****!!!!*****","SITO NON ASSOCIATOOO NON DEFINITIVO");
                    FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerSito, new FragmentCreaMioSito() );
                    fragmentTransaction.commit();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }


    public void controllaSitoAssociato (String uidUtente){
        Log.e("*****!!!!*****","SONO IN CONTROLLASITOASSOCIATO!!!");
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Nome

        recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(uidUtente);     //SELECT * FROM Siti WHERE getUid LIKE "..."
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<SitoCulturale> siti = new ArrayList<>();
                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.e("*****!!!!*****","SONO NEL FOR!!!");
                    siti.add(snapshot.getValue(SitoCulturale.class));

                }
                if(siti.size() != 0){
                    Log.e("****SITI_0*****", siti.get(0).getNome());
                    setSito(siti.get(0));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }

    public void setSito(SitoCulturale sito1){
        Log.e("SITO1****!!!", ""+sito1.getNome());
        this.sito = sito1;

    }

}