package com.uniba.capitool.fragments.fragmentsModificaSito;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.activities.ModificaSito;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentModificaInfoSito extends Fragment {
    SitoCulturale sitoCulturale;
    String nomeSito;
    String imageUriString;
    EditText nomeCitta;
    TextView orarioChiusura;
    TextView  orarioApertura;
    EditText costoIngresso;
    EditText indirizzo;
    int hour, minute;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modifica_info_sito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sitoCulturale = ((ModificaSito)getActivity()).getSito();

        nomeCitta = view.findViewById(R.id.modificaCittà);
        orarioChiusura = view.findViewById(R.id.modificaTimer2);
        orarioApertura = view.findViewById(R.id.modificaTimer);
        costoIngresso = view.findViewById(R.id.modificaCostoIngresso);
        indirizzo = view.findViewById(R.id.modificaIndirizzo);
        Button btnModifica = view.findViewById(R.id.btnModifica);

        nomeCitta.setText(sitoCulturale.getCitta());
        orarioChiusura.setText(sitoCulturale.getOrarioChiusura());
        orarioApertura.setText(sitoCulturale.getOrarioApertura());
        costoIngresso.setText(sitoCulturale.getCostoBiglietto());
        indirizzo.setText(sitoCulturale.getIndirizzo());


        nomeCitta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sitoCulturale.setCitta(nomeCitta.getText().toString());
                ((ModificaSito)getActivity()).setSito(sitoCulturale);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        orarioChiusura.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sitoCulturale.setOrarioChiusura(orarioChiusura.getText().toString());
                ((ModificaSito)getActivity()).setSito(sitoCulturale);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        orarioApertura.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sitoCulturale.setOrarioApertura(orarioApertura.getText().toString());
                ((ModificaSito)getActivity()).setSito(sitoCulturale);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        costoIngresso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sitoCulturale.setCostoBiglietto(costoIngresso.getText().toString());
                ((ModificaSito)getActivity()).setSito(sitoCulturale);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        indirizzo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sitoCulturale.setIndirizzo(indirizzo.getText().toString());
                ((ModificaSito)getActivity()).setSito(sitoCulturale);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        btnModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean erroreDatiPersonali=false;

                if(nomeCitta.getText().toString().equals("") || !BasicMethod.checkIfNameIsAcceptable(nomeCitta.getText().toString())){
                    nomeCitta.setError(getString(R.string.errorCitytName));
                    erroreDatiPersonali=true;
                }



                if(orarioChiusura.getText().toString().equals("")){
                    orarioChiusura.setError(getString(R.string.errorClosingTime));
                    erroreDatiPersonali=true;
                }

                if(orarioApertura.getText().toString().equals("")){
                    orarioApertura.setError(getString(R.string.errorOpeningTime));
                    erroreDatiPersonali=true;
                }

                if(costoIngresso.getText().toString().equals("")){
                    costoIngresso.setError(getString(R.string.errorEntranceFee));
                    erroreDatiPersonali=true;
                }

                if(!BasicMethod.stringIsInteger(costoIngresso.getText().toString())){
                    costoIngresso.setError(getString(R.string.errorEntranceFee));
                    erroreDatiPersonali=true;
                }

                if(indirizzo.getText().toString().equals("") || !BasicMethod.checkIfAddressIsAcceptable(indirizzo.getText().toString())) {
                    indirizzo.setError(getString(R.string.errorAddress));
                    erroreDatiPersonali=true;
                }


                if(erroreDatiPersonali==false){
                    saveChanges();
                }

            }
        });

        orarioApertura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int oraApertura, int minutiApertura) {

                        //Inizializzo ora e minuti
                        hour = oraApertura;
                        minute = minutiApertura;

                        //Salvo ora e minuti
                        String time = hour + ":" + minute;

                        //Inizializza l'orario con un format di 24 ore
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );

                        try {
                            Date date = f24Hours.parse(time);

                            //Cambia la text view con l'orario selezionato
                            orarioApertura.setText(f24Hours.format(date));
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                    }
                }, 24,0,true);

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour,minute);
                timePickerDialog.show();
            }
        });

        orarioChiusura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int oraChiusura, int minutiChiusura) {
                        //Iniziallizza ora e minuti
                        hour = oraChiusura;
                        minute = minutiChiusura;

                        //Salvo ora e minuti
                        String time = hour + ":" + minute;

                        //Inizializza l'orario con un format di 24 ore
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(time);

                            //Cambia la text view con l'orario selezionato
                            orarioChiusura.setText(f24Hours.format(date));
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                    }
                }, 24,0,true);

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour,minute);
                timePickerDialog.show();
            }
        });

    }

    private void saveChanges() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef;

        myRef = database.getReference("/Siti/"+ sitoCulturale.getId() + "/nome");
        myRef.setValue(BasicMethod.toLower(sitoCulturale.getNome()));

        myRef = database.getReference("/Siti/"+ sitoCulturale.getId() + "/citta");
        myRef.setValue(BasicMethod.toLower(sitoCulturale.getCitta()));

        myRef = database.getReference("/Siti/"+ sitoCulturale.getId() + "/costoBiglietto");
        myRef.setValue(sitoCulturale.getCostoBiglietto());

        myRef = database.getReference("/Siti/"+ sitoCulturale.getId() + "/indirizzo");
        myRef.setValue(sitoCulturale.getIndirizzo());

        myRef = database.getReference("/Siti/"+ sitoCulturale.getId() + "/orarioChiusura");
        myRef.setValue(sitoCulturale.getOrarioChiusura());

        myRef = database.getReference("/Siti/"+ sitoCulturale.getId() + "/orarioApertura");
        myRef.setValue(sitoCulturale.getOrarioApertura());

        StorageReference fileReference= FirebaseStorage.getInstance().getReference().child("fotoSiti").child(sitoCulturale.getId());

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.loading));
        pd.show();



        fileReference.putFile(((ModificaSito)getActivity()).getImageUri()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                pd.dismiss();
            }
        });
        /*Log.e("******* ID *******", myRef.push().getKey() );*/
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //   Handler handler= new Handler();
                Utente utente = ((ModificaSito)getActivity()).getUtente();

                Log.e("**************", ""+utente.getCognome());
                Intent homePage = new Intent(getActivity(), HomePage.class);
                homePage.putExtra("cognome",utente.getCognome());
                homePage.putExtra("nome",utente.getNome());
                homePage.putExtra("uid",utente.getUid());
                homePage.putExtra("email",utente.getEmail());
                homePage.putExtra("ruolo",utente.getRuolo());
                //homePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                //handler.removeCallbacksAndMessages(null);
                getActivity().startActivity(homePage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}