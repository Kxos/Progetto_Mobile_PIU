package com.uniba.capitool.fragments.fragmentsAggiungiInfoSito;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uniba.capitool.R;
import com.uniba.capitool.activities.AggiungiSito;

public class AggiungiNomeSito extends Fragment {
    private static final int SELECT_IMAGE_CODE = 1;
    ImageView selectedImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_aggiungi_nome_sito, container, false);
        Button avanti = v.findViewById(R.id.button);
        EditText nome =v.findViewById(R.id.nomeSito);
        selectedImage= v.findViewById(R.id.immagineSito);


        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "title"), SELECT_IMAGE_CODE);
            }
        });
        //leggere il file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences!=null){
            String nomeTrovato = sharedPreferences.getString("nome", "");
            nome.setText(nomeTrovato);

        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerAggiungiSito, new AggiungiInfoSito());
                fragmentTransaction.commit();

                //scrivere nel file SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor datiSito = sharedPreferences.edit();
                datiSito.putString("nome", nome.getText().toString());
                datiSito.apply();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            Uri uri=data.getData();
            selectedImage.setImageURI(uri);
        }
    }
}