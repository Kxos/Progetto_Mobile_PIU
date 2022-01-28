package com.uniba.capitool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.SitoCulturale;

public class EliminaSito extends AppCompatActivity {

    boolean passwordCorretta ;
    SitoCulturale sitoTrovato;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimina_sito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbarDeleteSite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        Curatore utente = (Curatore) bundle.getSerializable("utente");


        TextInputEditText editPasswordCuratore = findViewById(R.id.edit_text_password);
        CheckBox boxInfo = findViewById(R.id.boxInfoDelete);
        Button buttonConferma = findViewById(R.id.button_conferma_delega);
        Button buttonAnnulla = findViewById(R.id.button_annulla_delega);



        editPasswordCuratore.setTransformationMethod(PasswordTransformationMethod.getInstance());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminaSito.super.onBackPressed();
            }
        });

        buttonAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EliminaSito.this, HomePage.class);
                Bundle bundle = new Bundle();
                intent.putExtra("uid", utente.getUid()); //Optional parameters
                intent.putExtra("nome", utente.getNome()); //Optional parameters
                intent.putExtra("cognome", utente.getCognome()); //Optional parameters
                intent.putExtra("email", utente.getEmail()); //Optional parametersù
                intent.putExtra("ruolo", utente.getRuolo()); //Optional parameters
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //TODO temporaneo, da eliminare
        editPasswordCuratore.setText("Cur123/");
        boxInfo.setChecked(true);

        buttonConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passwordInserita = editPasswordCuratore.getText().toString();


                if (passwordInserita.isEmpty()) {
                    editPasswordCuratore.setError(getString(R.string.noPswInsert));
                    //Toast.makeText(getApplicationContext(), "Non hai inserito nessuna password", Toast.LENGTH_SHORT).show();
                } else if (!boxInfo.isChecked()) {
                    boxInfo.setError(getString(R.string.boxInfoNotChecked));
                    //Toast.makeText(getApplicationContext(), "Devi confermare di aver letto l'informativa per procedere all'eliminazione", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseUser datiUtente = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(datiUtente.getEmail(), passwordInserita);

                    Log.e("Dati trovati", "email: " + datiUtente.getEmail() + " ----- uid: " + datiUtente.getUid() +  " -----  passord inserita: " + passwordInserita);

                    datiUtente.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.e("TASK", "completato con SUCCESSO, password CORRETTA") ;



                                    Log.e("Elimina sito", "PWS CORETTA, ora passo all'eliminazione") ;


                                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                                    DatabaseReference myRef = database.getReference("/");

                                    Query recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(datiUtente.getUid()).limitToFirst(1);
                                    recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            for (DataSnapshot shot : snapshot.getChildren()) {
                                                sitoTrovato = shot.getValue(SitoCulturale.class);
                                                Log.e("FOR","Ho trovato il sito *");

                                            }



                                            if(sitoTrovato == null){
                                                Log.e("Risultato ricerca sito", "SITO NON TROVATO") ;
                                            }else{

                                                Log.e("Sito trovato", "Nome: " + sitoTrovato.getNome() + "----- idSito: " + sitoTrovato.getId());

                                                myRef.child("Siti").child(sitoTrovato.getId()).setValue(null) ;
                                                Log.e("Elimina sito", "Sito elimianto!") ;


                                                BasicMethod.alertDialog(EliminaSito.this, "", "Sito eliminato con successo!", "");



                                                Intent intent = new Intent (EliminaSito.this, HomePage.class) ;
                                                Bundle bundle = new Bundle() ;
                                                intent.putExtra("uid", utente.getUid()); //Optional parameters
                                                intent.putExtra("nome", utente.getNome()); //Optional parameters
                                                intent.putExtra("cognome", utente.getCognome()); //Optional parameters
                                                intent.putExtra("email", utente.getEmail()); //Optional parametersù
                                                intent.putExtra("ruolo", utente.getRuolo()); //Optional parameters
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                }


                                            }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
                                        }

                                    });

                            } else {
                                editPasswordCuratore.setError("Password ERRATA");
                                Log.e("TASK", "TASK FALLITO, PASSWORD errata") ;
                            }
                        }
                    });


                }
            }


        });
    }
}