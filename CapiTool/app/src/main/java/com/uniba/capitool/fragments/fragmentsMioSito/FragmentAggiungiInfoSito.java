package com.uniba.capitool.fragments.fragmentsMioSito;

import static com.uniba.capitool.activities.BasicMethod.limitDigit;

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
import android.text.InputFilter;
import android.text.Spanned;
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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FragmentAggiungiInfoSito extends Fragment {
    SitoCulturale sitoCulturale;
    String nomeSito;
    String imageUriString;
    EditText nomeCitta;
    TextView orarioChiusura;
    TextView  orarioApertura;
    EditText costoIngresso;
    EditText indirizzo;
    int hour, minute;
    private String blockCharacterSet = "- ";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_aggiungi_info_sito, container, false);
        nomeCitta = v.findViewById(R.id.Città);
        orarioChiusura =v.findViewById(R.id.Timer2);
        orarioApertura =v.findViewById(R.id.Timer);
        costoIngresso =v.findViewById(R.id.CostoIngresso);
        indirizzo =v.findViewById(R.id.Indirizzo);
        Button btnConferma = v.findViewById(R.id.btnConferma);
        sitoCulturale = new SitoCulturale();
        costoIngresso.setFilters(new InputFilter[] { filter });

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences!=null){
            String cittaTrovata = sharedPreferences.getString("citta", "");
            String orarioChiusuraTrovato =  sharedPreferences.getString("orarioChiusura", "");
            String orarioAperturaTrovato =  sharedPreferences.getString("orarioApertura", "");
            String costoIngressoTrovato =  sharedPreferences.getString("costoIngresso", "");
            String indirizzoTrovato = sharedPreferences.getString("indirizzo", "");
            nomeSito = sharedPreferences.getString("nome","");
            nomeCitta.setText(cittaTrovata);
            orarioChiusura.setText(orarioChiusuraTrovato);
            orarioApertura.setText(orarioAperturaTrovato);
            costoIngresso.setText(costoIngressoTrovato);
            indirizzo.setText(indirizzoTrovato);

            try {
                imageUriString = sharedPreferences.getString("url", "");
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }else{

        }

        SharedPreferences.Editor datiSito = sharedPreferences.edit();

    nomeCitta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("citta", nomeCitta.getText().toString());
                datiSito.apply();
            }
        });

        orarioChiusura.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("orarioChiusura", orarioChiusura.getText().toString());
                datiSito.apply();
            }
        });

        orarioApertura.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("orarioApertura", orarioApertura.getText().toString());
                datiSito.apply();
            }
        });

        costoIngresso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("costoIngresso", costoIngresso.getText().toString());
                datiSito.apply();
            }
        });

        indirizzo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                datiSito.putString("indirizzo", indirizzo.getText().toString());
                datiSito.apply();
            }
        });




        btnConferma.setOnClickListener(new View.OnClickListener() {
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

                if(!BasicMethod.isFloat(costoIngresso.getText().toString())){
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
                    addSitoOnLastPosition();
                    datiSito.clear();
                    datiSito.commit();
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

        // Inflate the layout for this fragment
        return v;
    }

    public void addSitoOnLastPosition(){

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        Query recentPostsQuery = myRef.child("Siti");     //SELECT * FROM Siti

        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                insertQuery(dataSnapshot.getChildrenCount(), database, myRef);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });


    }

    // TODO - VERIFICARE ERRORE SULL'ISTANZIAZIONE DI SITO ( (int)counterIndex )
    public void insertQuery(long counterIndex,  FirebaseDatabase database, DatabaseReference myRef){

        String key = myRef.push().getKey();


        //INSERT di un nuovo oggetto
        SitoCulturale sito= new SitoCulturale(key,BasicMethod.toLower(nomeSito), indirizzo.getText().toString(), orarioApertura.getText().toString(),
                                                orarioChiusura.getText().toString(),limitDigit(Float.parseFloat(costoIngresso.getText().toString())),
                                                    BasicMethod.toLower(nomeCitta.getText().toString()), mAuth.getCurrentUser().getUid());



        myRef=database.getReference("Siti").child(key);

        myRef.setValue(sito);

        StorageReference fileReference= FirebaseStorage.getInstance().getReference().child("fotoSiti").child(key);

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Caricamento");
        pd.show();



        fileReference.putFile(Uri.parse(imageUriString)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                pd.dismiss();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Utente utente = BasicMethod.getUtente();
                Intent homePage = new Intent(getActivity(), HomePage.class);
                homePage.putExtra("cognome",utente.getCognome());
                homePage.putExtra("nome",utente.getNome());
                homePage.putExtra("uid",utente.getUid());
                homePage.putExtra("email",utente.getEmail());
                homePage.putExtra("ruolo",utente.getRuolo());
                //handler.removeCallbacksAndMessages(null);
                getActivity().startActivity(homePage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}

