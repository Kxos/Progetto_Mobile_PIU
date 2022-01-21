package com.uniba.capitool.fragments.fragmentsAggiungiPercorso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.classes.CardOpera;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDatiPercorso extends Fragment {

    private Toolbar toolbar;
    private View viewActivity;
    private static ArrayList<CardOpera> listaOpereChecked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dati_percorso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.viewActivity = view;

        toolbar = ((AggiungiPercorso)getActivity()).getToolbar();

        // Leggo dal file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(sharedPreferences!=null){

            Bundle args = getArguments();;
            listaOpereChecked = (ArrayList<CardOpera>)args.getSerializable("listaOpereSelezionate");

            //Log.e("SEI IN DATI PERCORSO: ", ""+listaOpereChecked);

            //toolbar.setTitle(getString(R.string.site) + " - " + sitoCulturale.getNome());

            Button bottoneIndietro = view.findViewById(R.id.buttonIndietro);
            bottoneIndietro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentSelezionaOpere fragmentSelezionaOpere = new FragmentSelezionaOpere();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("listaOpereSelezionate",  listaOpereChecked);
                    fragmentSelezionaOpere.setArguments(bundle);

                    //Log.e( "LISTA IN RITORNO: ", ""+FragmentDatiPercorso.getListaOpereChecked());

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerRicercaSiti, fragmentSelezionaOpere );
                    fragmentTransaction.commit();
                }
            });

            EditText nomePercorso = view.findViewById(R.id.textfield_nomePercorso);
            EditText descrizionePercorso = view.findViewById(R.id.textfield_descrizionePercorso);
            Switch pubblicoPercorso = view.findViewById(R.id.switchPubblicoPercorso);

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("/");

            Button bottoneAvanti = view.findViewById(R.id.buttonAvanti);
            bottoneAvanti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(nomePercorsoIsValorized(nomePercorso)){

                        // TODO - SALVARE IL PERCORSO SU FIREBASE

                        Log.e("AAAAAAAA","hjhjgh");
                        Log.e("dgfdghfdghdfhdfh",""+pubblicoPercorso.isChecked());
                        /**

                        String key = myRef.push().getKey();
                        myRef = database.getReference("Percorsi").child(key);
                        myRef.setValue(percorso);
                         */
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.toastDatiPercorso), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

    }

    /***
     * Controlla che il nome del Percorso sia valorizzato
     *
     * @param nomePercorso
     * @return boolean: Valore di verità
     */
    public boolean nomePercorsoIsValorized(EditText nomePercorso){
        if(!nomePercorso.getText().toString().matches("")){
            return true;
        }
        return false;
    }

    public static ArrayList<CardOpera> getListaOpereChecked(){
        return listaOpereChecked;
    }
}