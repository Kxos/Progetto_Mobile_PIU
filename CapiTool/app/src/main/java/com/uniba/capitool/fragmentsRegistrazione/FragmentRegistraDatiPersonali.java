package com.uniba.capitool.fragmentsRegistrazione;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uniba.capitool.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class FragmentRegistraDatiPersonali extends Fragment {

    EditText username;
    EditText nome;
    EditText cognome;
    EditText dataNascita;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registra_dati_personali, container, false);

   /*     Bundle bundle = this.getArguments();

        if(bundle != null){
            // handle your code here.
            Log.d("onCreateView: ", bundle.get("email").toString());
            //bundle.putString("nome",nome.getText().toString());
           // bundle.putString("cognome",cognome.getText().toString());
           // bundle.putString("dataNascita",username.getText().toString());
        }else{
            //Bundle bundle = new Bundle();
            //bundle.putString("ruolo",ruolo.getText().toString()); // Put anything what you want
        }*/

        //leggere il file SharedPreferences
        SharedPreferences datiRegistrazioneUtente = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(datiRegistrazioneUtente!=null){
            String emailTrovata = datiRegistrazioneUtente.getString("email", "");
            String passwordTrovata = datiRegistrazioneUtente.getString("password", "");
            String ruoloTrovato = datiRegistrazioneUtente.getString("ruolo", "");
            Log.e("DATI SharedPreferences ", ""+emailTrovata+" , "+passwordTrovata+" , "+ruoloTrovato);

        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

        username=v.findViewById(R.id.username);
        nome=v.findViewById(R.id.nome);
        cognome=v.findViewById(R.id.cognome);
        dataNascita=v.findViewById(R.id.dataNascita);
        Button conferma= v.findViewById(R.id.conferma);

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erroreDatiPersonali=false;

                if(username==null){
                    username.setError("Inserisci un username");
                    erroreDatiPersonali=true;
                }else if(nome==null){
                    nome.setError("Inserisci il tuo nome");
                    erroreDatiPersonali=true;
                }else if(cognome==null){
                    cognome.setError("Inserisci il tuo cognome");
                    erroreDatiPersonali=true;
                }else if(dataNascita==null){
                    dataNascita.setError("Inserisci la tua data di nascita");
                    erroreDatiPersonali=true;
                }

                if(erroreDatiPersonali==false){

                    //vado avanti

                }
            }
        });

       username.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });




        dataNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apriCalendario();
               /* Calendar c= Calendar.getInstance();
                int year= c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day= c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getActivity().getSupportFragmentManager(), "date picker");*/


            }
        });
        return v;
    }

    public void apriCalendario(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int anno, int mese,
                                  int giorno) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, anno);
                myCalendar.set(Calendar.MONTH, mese);
                myCalendar.set(Calendar.DAY_OF_MONTH, giorno);
                updateEditTextDataNascita();
            }

        };
        new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        /*dataNascita.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });*/
    }

    private void updateEditTextDataNascita() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALIAN);

        dataNascita.setText(sdf.format(myCalendar.getTime()));
    }



    public void insertQueryUtente(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://e-culture-tool-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");



      /*  //INSERT di un nuovo oggetto
        Visitatore visitatore= new Visitatore("18/11/99", "visitatore", "De Matteis", "5985", "Vincenzo", "vitoiann@gmail.com", "vito56");
        myRef=database.getReference("/Utenti/9/");
        myRef.setValue(visitatore);*/
    }
}