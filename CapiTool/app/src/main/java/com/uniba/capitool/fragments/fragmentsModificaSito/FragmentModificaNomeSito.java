package com.uniba.capitool.fragments.fragmentsModificaSito;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.ModificaSito;
import com.uniba.capitool.classes.SitoCulturale;


public class FragmentModificaNomeSito extends Fragment {
    SitoCulturale sito;
    ImageView fotoSito;
    Uri imageUri;
    EditText nome;
    private static final int SELECT_IMAGE_CODE = 1;

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
        sito = ((ModificaSito)getActivity()).getSito();
        fotoSito = view.findViewById(R.id.modificaImmagineSito);
        nome = view.findViewById(R.id.modificaNomeSito);
        Button btnAvanti = view.findViewById(R.id.buttonAvantiModifica);


        nome.setText(sito.getNome());

        if( ((ModificaSito)getActivity()).getImageUri() != null){
            Log.e("****Fotooo", " "+((ModificaSito)getActivity()).getImageUri());

            Glide.with(getActivity())
                    .load(((ModificaSito)getActivity()).getImageUri())
                    .into(fotoSito);
        }else{
            letturaImmagineSito(fotoSito, getActivity());
        }


        fotoSito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "title"), SELECT_IMAGE_CODE);
            }
        });



        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean erroreDatiPersonali = false;

                if(nome.getText().toString().equals("") || !BasicMethod.checkIfNameIsAcceptable(nome.getText().toString()) ){
                    nome.setError(getString(R.string.errorSiteName));
                    erroreDatiPersonali=true;
                }


                if(erroreDatiPersonali==false){
                    sito.setNome(nome.getText().toString());
                    ((ModificaSito)getActivity()).setSito(sito);
                    FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerModificaSito, new FragmentModificaInfoSito());
                    fragmentTransaction.commit();



                }
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
                ((ModificaSito)getActivity()).setImageUri(downloadUrl);
                //do something with downloadurl
                Glide.with(activity)
                        .load(downloadUrl)
                        .into(imageView);

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data!=null){
            imageUri=data.getData();
            if(imageUri!=null){
                ((ModificaSito)getActivity()).setImageUri(imageUri);
                fotoSito.setImageURI(imageUri);

            }
        }
    }
}