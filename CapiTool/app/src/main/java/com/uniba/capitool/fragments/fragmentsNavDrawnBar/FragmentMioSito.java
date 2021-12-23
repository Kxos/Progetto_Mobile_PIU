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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_mio_sito, container, false);
        controllaSitoAssociato (BasicMethod.getUtente().getUid());

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

        recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(uidUtente);     //SELECT * FROM Siti WHERE getUid LIKE "..."
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<SitoCulturale> siti = new ArrayList<>();
                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    siti.add(snapshot.getValue(SitoCulturale.class));

                }
                if(siti.size() != 0){
                    setSito(siti.get(0));
                }else{
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

    public void setSito(SitoCulturale sito1){

        this.sito = sito1;

        if(BasicMethod.getUtente().getRuolo().equals("curatore") && sito.getUidCuratore() != null ){
          //  Log.e("*****!!!!*****","SITO ASSOCIATOOO DEFINITIVO"+BasicMethod.getUtente().getUid()+""+sito.getUidCuratore());

//            FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragmentContainerSito, new FragmentInfoSito() );
//            fragmentTransaction.commit();

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