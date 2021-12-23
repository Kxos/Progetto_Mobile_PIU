package com.uniba.capitool.fragments.fragmentsMioSito;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniba.capitool.R;

public class FragmentAggiungiNomeSito extends Fragment {
    private static final int SELECT_IMAGE_CODE = 1;
    ImageView fotoSito;
    Uri imageUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_aggiungi_nome_sito, container, false);
        Button avanti = v.findViewById(R.id.button);
        EditText nome =v.findViewById(R.id.nomeSito);
        fotoSito = v.findViewById(R.id.immagineSito);


        fotoSito.setOnClickListener(new View.OnClickListener() {
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

            imageUri = Uri.parse(sharedPreferences.getString("url", ""));
            fotoSito.setImageURI(imageUri);

        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean erroreDatiPersonali = false;

                if(nome.getText().toString().equals("")){
                    nome.setError("Inserisci il nome del tuo sito");
                    erroreDatiPersonali=true;
                }

                if(erroreDatiPersonali==false){
                    FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerAggiungiSito, new FragmentAggiungiInfoSito());
                    fragmentTransaction.commit();

                    //scrivere nel file SharedPreferences
                    SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor datiSito = sharedPreferences.edit();
                    datiSito.putString("nome", nome.getText().toString());
                    datiSito.putString("url", imageUri.toString());
                    datiSito.apply();

                }


            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data!=null){
            Log.e("***********************************************************","");
            imageUri=data.getData();
            if(imageUri!=null){
                fotoSito.setImageURI(imageUri);

            }
        }
    }
}