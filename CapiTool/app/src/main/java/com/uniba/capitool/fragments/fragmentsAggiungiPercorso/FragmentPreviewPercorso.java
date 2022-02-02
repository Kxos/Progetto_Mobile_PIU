package com.uniba.capitool.fragments.fragmentsAggiungiPercorso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.CardOpera;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.Percorso;

import java.util.ArrayList;

public class FragmentPreviewPercorso extends Fragment {

    static Percorso percorso = new Percorso();
    static ArrayList<Opera> listaOpere = new ArrayList<>();


    public FragmentPreviewPercorso() {
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
        return inflater.inflate(R.layout.fragment_preview_percorso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Leggo dal file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(sharedPreferences!=null) {

            Bundle args = getArguments();

            percorso = (Percorso) args.getSerializable("nuovoPercorso");
            listaOpere = (ArrayList<Opera>) args.getSerializable("listaOpereNuovoPercorso");

            Button bottoneIndietro = view.findViewById(R.id.buttonIndietroPreview);
            bottoneIndietro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentDatiPercorso fragmentDatiPercorso = new FragmentDatiPercorso();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("nuovoPercorso",  percorso);
                    bundle.putSerializable("listaOpereRicevuteDaPreview",  listaOpere);
                    fragmentDatiPercorso.setArguments(bundle);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerRicercaSiti, fragmentDatiPercorso );
                    fragmentTransaction.commit();
                }
            });

            Button bottoneSalva = view.findViewById(R.id.buttonSalva);
            bottoneSalva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("/");

                    myRef = database.getReference("/Percorsi/"+percorso.getId());
                    myRef.setValue(percorso);

                    myRef = database.getReference("/Percorsi/"+percorso.getId()+"/OpereScelte");
                    myRef.setValue(listaOpere);

                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.toastPercorsoSalvato), Toast.LENGTH_SHORT).show();

                    // Ritorna ad "I Miei Percorsi"
                    getActivity().onBackPressed();
                }
            });

        }

    }

    public static Percorso getPercorso() {
        return percorso;
    }

    public static ArrayList<Opera> getListaOpere() {
        return listaOpere;
    }
}