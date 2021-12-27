package com.uniba.capitool.fragments.fragmentsMioSito;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.SitoCulturale;

public class FragmentModificaNomeSito extends Fragment {
    SitoCulturale sito;
    ImageView fotoSito;
    EditText nome;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modifica_nome_sito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        sito = (SitoCulturale) bundle.getSerializable("sito");

        fotoSito = view.findViewById(R.id.modificaImmagineSito);
        nome = view.findViewById(R.id.modificaNomeSito);

        letturaImmagineSito(fotoSito, getActivity());
        nome.setText(sito.getNome());


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