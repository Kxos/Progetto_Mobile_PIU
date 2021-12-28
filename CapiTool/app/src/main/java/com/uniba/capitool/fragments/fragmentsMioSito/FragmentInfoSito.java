package com.uniba.capitool.fragments.fragmentsMioSito;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.activities.ModificaSito;
import com.uniba.capitool.activities.VisualizzaZoneSito;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

public class FragmentInfoSito extends Fragment {
    SitoCulturale sito;
    TextView nomeSito;
    TextView orarioSito;
    TextView indirizzo;
    TextView costoIngresso;
    ImageView fotoSito;
    Utente utente = new Utente();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_info_sito, container, false);
        FloatingActionButton btnEdit = v.findViewById(R.id.buttonEditSito);
        utente = BasicMethod.getUtente();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle dati = new Bundle();
                dati.putSerializable("sito", sito);
                dati.putSerializable("utente", utente);
                Intent modificaSito = new Intent(getActivity(), ModificaSito.class);
                modificaSito.putExtras(dati);
                getActivity().startActivity(modificaSito);


            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fotoSito= view.findViewById(R.id.immagineSito2);
        TextView visualizzaZone = view.findViewById(R.id.textViewZoneOpere);
        nomeSito = view.findViewById(R.id.titoloNomeSito);
        indirizzo = view.findViewById(R.id.Via);
        orarioSito = view.findViewById(R.id.Orario);
        costoIngresso = view.findViewById(R.id.Costo);


        //recupero il bundle (ovvero l'oggetto SitoCculturale associato all'utente) passato dal fragment FragmentMioSito
        Bundle bundle = getArguments();
        sito = (SitoCulturale) bundle.getSerializable("sito");

        nomeSito.setText(BasicMethod.setUpperPhrase(sito.getNome()));
        letturaImmagineSito(fotoSito, getActivity());
        indirizzo.setText(sito.getIndirizzo());
        orarioSito.setText(sito.getOrarioApertura() + " - " + sito.getOrarioChiusura());
        costoIngresso.setText(sito.getCostoBiglietto() + "€");

        visualizzaZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visualizzaZone = new Intent(getActivity(), VisualizzaZoneSito.class);
                Bundle dati = new Bundle();
                dati.putSerializable("utente", BasicMethod.getUtente());
                dati.putSerializable("sito", sito);
                visualizzaZone.putExtras(dati);
                getActivity().startActivity(visualizzaZone);

            }
        });


    }
    public void letturaImmagineSito(ImageView imageView, Activity activity){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/fotoSiti/" + sito.getId());

        /**
         * Scarica il "DownloadURL" che ci serve per leggere l'immagine dal DB e metterla in una ImageView
         * */
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                //do something with downloadurl
                Glide.with(activity)
                        .load(downloadUrl)
                        .into(imageView);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        letturaImmagineSito(fotoSito,getActivity());
    }
}