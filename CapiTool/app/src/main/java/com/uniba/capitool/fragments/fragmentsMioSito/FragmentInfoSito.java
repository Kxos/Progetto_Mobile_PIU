package com.uniba.capitool.fragments.fragmentsMioSito;

import android.app.Activity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.classes.SitoCulturale;

public class FragmentInfoSito extends Fragment {
    SitoCulturale sito;
    TextView nomeSito;
    TextView orarioSito;
    TextView indirizzo;
    TextView costoIngresso;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_sito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView fotoSito= view.findViewById(R.id.immagineSito2);
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
}