package com.uniba.capitool.fragments.fragmentsAggiungiPercorso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.classes.CardOpera;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.Percorso;
import com.uniba.capitool.classes.Utente;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDatiPercorso extends Fragment {

    private Toolbar toolbar;
    private View viewActivity;
    Utente utente;
    DatabaseReference myRef;
    Percorso percorsoRicevutoDaPreview;
    ArrayList<Opera> listaOpere = new ArrayList<>();
    private static ArrayList<CardOpera> listaOpereChecked;
    private static ArrayList<Opera> listaOpereRicevuteDaPreview;

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

        utente = ((AggiungiPercorso)getActivity()).getUtente();
        toolbar = ((AggiungiPercorso)getActivity()).getToolbar();

        // Leggo dal file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(sharedPreferences!=null){

            Bundle args = getArguments();;
            listaOpereChecked = (ArrayList<CardOpera>)args.getSerializable("listaOpereSelezionate");
            percorsoRicevutoDaPreview = (Percorso) args.getSerializable("nuovoPercorso");
            listaOpereRicevuteDaPreview = (ArrayList<Opera>) args.getSerializable("listaOpereRicevuteDaPreview");


            Button bottoneIndietro = view.findViewById(R.id.buttonIndietro);
            bottoneIndietro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentSelezionaOpere fragmentSelezionaOpere = new FragmentSelezionaOpere();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("listaOpereSelezionate",  listaOpereChecked);
                    fragmentSelezionaOpere.setArguments(bundle);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerRicercaSiti, fragmentSelezionaOpere );
                    fragmentTransaction.commit();
                }
            });

            EditText nomePercorso = view.findViewById(R.id.textfield_nomePercorso);
            EditText descrizionePercorso = view.findViewById(R.id.textfield_descrizionePercorso);
            Switch pubblicoPercorso = view.findViewById(R.id.switchPubblicoPercorso);

                if(BasicMethod.getUtente().getRuolo().equals("guida")){
                    pubblicoPercorso.setVisibility(View.VISIBLE);
            }else{
                pubblicoPercorso.setVisibility(View.INVISIBLE);
            }

            // Verifico che sto tornando dalla Preview, quindi reimposto i dati
            if(percorsoRicevutoDaPreview != null) {
                nomePercorso.setText(percorsoRicevutoDaPreview.getNome());
                descrizionePercorso.setText(percorsoRicevutoDaPreview.getDescrizione());
                pubblicoPercorso.setChecked(percorsoRicevutoDaPreview.isPubblico());
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
            myRef = database.getReference("/");

            Button bottoneAvanti = view.findViewById(R.id.buttonAvanti);
            bottoneAvanti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(nomePercorsoIsValorized(nomePercorso)){

                        String key = myRef.push().getKey();

                        Percorso percorso = new Percorso();
                        percorso.setId(key);
                        percorso.setNome(nomePercorso.getText().toString());
                        percorso.setDescrizione(descrizionePercorso.getText().toString());
                        percorso.setPubblico(pubblicoPercorso.isChecked());
                        percorso.setIdSitoAssociato(sharedPreferences.getString("idSito", ""));
                        percorso.setNomeSitoAssociato(sharedPreferences.getString("nomeSito", ""));
                        percorso.setCittaSitoAssociato(sharedPreferences.getString("cittaSito", ""));
                        percorso.setIdUtente(utente.getUid());

                        if(listaOpereRicevuteDaPreview != null){

                            FragmentPreviewPercorso fragmentPreviewPercorso = new FragmentPreviewPercorso();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("nuovoPercorso", percorso);
                            bundle.putSerializable("listaOpereNuovoPercorso",  listaOpereRicevuteDaPreview);
                            fragmentPreviewPercorso.setArguments(bundle);

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerRicercaSiti, fragmentPreviewPercorso );
                            fragmentTransaction.commit();

                        }else{

                            for(int i = 0; i < listaOpereChecked.size(); i++){
                                listaOpere.add(i,new Opera());
                                listaOpere.get(i).setId(listaOpereChecked.get(i).getId());
                                listaOpere.get(i).setTitolo(listaOpereChecked.get(i).getTitolo());
                                listaOpere.get(i).setDescrizione(listaOpereChecked.get(i).getDescrizione());
                                listaOpere.get(i).setIdZona(listaOpereChecked.get(i).getIdZona());
                                listaOpere.get(i).setIdFoto(listaOpereChecked.get(i).getIdFoto());
                                //Log.e( "TGFERGFERGER: ", ""+listaOpereChecked.get(i).getIdFoto() );
                            }

                            FragmentPreviewPercorso fragmentPreviewPercorso = new FragmentPreviewPercorso();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("nuovoPercorso",  percorso);
                            bundle.putSerializable("listaOpereNuovoPercorso",  listaOpere);
                            fragmentPreviewPercorso.setArguments(bundle);

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerRicercaSiti, fragmentPreviewPercorso );
                            fragmentTransaction.commit();

                        }


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