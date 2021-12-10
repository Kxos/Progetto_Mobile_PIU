package com.uniba.capitool.activity.fragmentsRegistrazione;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.activity.HomePage;
import com.uniba.capitool.classes.*;
import com.uniba.capitool.classes.Visitatore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class FragmentRegistraDatiPersonali extends Fragment {

    FirebaseAuth mAuth;
    EditText nome;
    EditText cognome;
    EditText dataNascita;
    EditText numeroPatentino;
    Button conferma;

    String emailTrovata;
    String passwordTrovata;
    String ruoloTrovato="";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor datiRegistrazioneUtente;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registra_dati_personali, container, false);

        nome=v.findViewById(R.id.nome);
        cognome=v.findViewById(R.id.cognome);
        dataNascita=v.findViewById(R.id.dataNascita);
        numeroPatentino=v.findViewById(R.id.numeroPatentino);
        conferma= v.findViewById(R.id.conferma);

        //leggere il file SharedPreferences
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(sharedPreferences!=null){

            emailTrovata = sharedPreferences.getString("email", "");
            passwordTrovata = sharedPreferences.getString("password", "");
            ruoloTrovato = sharedPreferences.getString("ruolo", "");

            dataNascita.setText(sharedPreferences.getString("dataNascita", ""));
            nome.setText(sharedPreferences.getString("nome", ""));
            cognome.setText(sharedPreferences.getString("cognome", ""));

        }else{
            Log.e( "onCreateView: ", "SharedPreferences non trovato");
        }

        if(!ruoloTrovato.equals("guida")){

            numeroPatentino.setHint("");
            numeroPatentino.setEnabled(false);
            numeroPatentino.setBackground(null);
            numeroPatentino.setFocusable(false);
        }

        //metodo per abilitare le modifiche al file sharedPreferences
        datiRegistrazioneUtente=sharedPreferences.edit();

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erroreDatiPersonali=false;

                if(nome.getText().toString().equals("")){
                    nome.setError("Inserisci il tuo nome");
                    erroreDatiPersonali=true;
                }else if(cognome.getText().toString().equals("")){
                    cognome.setError("Inserisci il tuo cognome");
                    erroreDatiPersonali=true;
                }else if(dataNascita.getText().toString().equals("")){
                    dataNascita.setError("Inserisci la tua data di nascita");
                    erroreDatiPersonali=true;
                }

                if(ruoloTrovato.equals("guida") && numeroPatentino.getText().toString().equals("")){
                    numeroPatentino.setError("Inserisci il tuo numero di patentino");
                    erroreDatiPersonali=true;
                }

                if(erroreDatiPersonali==false){
                    //vado avanti e insrisco l'utente
                    insertUtente();
                }
            }
        });

        nome.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               datiRegistrazioneUtente.putString("nome", nome.getText().toString());
               datiRegistrazioneUtente.apply();
           }
       });

        cognome.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               datiRegistrazioneUtente.putString("cognome", cognome.getText().toString());
               datiRegistrazioneUtente.apply();
           }
       });

        dataNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no view has focus else hide KEYBOARD
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                apriCalendario();
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

    }

    private void updateEditTextDataNascita() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALIAN);

        dataNascita.setText(sdf.format(myCalendar.getTime()));

        datiRegistrazioneUtente.putString("dataNascita", dataNascita.getText().toString());
        datiRegistrazioneUtente.apply();
    }

    public void insertUtente(){

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(emailTrovata, passwordTrovata)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            insertUtenteRealtimeDB(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void insertUtenteRealtimeDB(FirebaseUser user){

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/Utenti/"+user.getUid());

        if(ruoloTrovato.equals("visitatore")){
            Visitatore visitatore= new Visitatore(emailTrovata, nome.getText().toString(), cognome.getText().toString(),
                    dataNascita.getText().toString(), ruoloTrovato);
            myRef.setValue(visitatore);
        }

        if(ruoloTrovato.equals("curatore")){
            Curatore curatore= new Curatore(emailTrovata, nome.getText().toString(), cognome.getText().toString(),
                    dataNascita.getText().toString(), ruoloTrovato);
            myRef.setValue(curatore);
        }

        if(ruoloTrovato.equals("guida")){
            Guida guida= new Guida(emailTrovata, nome.getText().toString(), cognome.getText().toString(),
                    dataNascita.getText().toString(), ruoloTrovato, numeroPatentino.getText().toString());
            myRef.setValue(guida);
        }

        //bisogna aspettare il caricamento nel db altrimenti va avanti e non scive i dati nel DB!
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent homePage = new Intent(getActivity(), HomePage.class);
                homePage.putExtra("email", emailTrovata); //Optional parameters
                homePage.putExtra("nome", nome.getText().toString());
                homePage.putExtra("cognome", cognome.getText().toString());
                homePage.putExtra("uid", user.getUid());
                getActivity().startActivity(homePage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}