package com.uniba.capitool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

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
import com.uniba.capitool.classes.CardSitoCulturale;
import com.uniba.capitool.classes.Curatore;

public class EliminaSito extends AppCompatActivity {

    boolean passwordCorretta ;
    CardSitoCulturale sitoTrovato;

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
        CheckBox boxInfo = findViewById(R.id.boxInfoCancellazioneAccount);
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


        buttonConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passwordInserita = editPasswordCuratore.getText().toString();


                if (passwordInserita.isEmpty()) {
                    editPasswordCuratore.setError(getString(R.string.noPswInsert));

                } else if (!boxInfo.isChecked()) {
                    boxInfo.setError(getString(R.string.boxInfoNotChecked));

                } else {

                    FirebaseUser datiUtente = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(datiUtente.getEmail(), passwordInserita);


                    datiUtente.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                                    DatabaseReference myRef = database.getReference("/");

                                    Query recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(datiUtente.getUid()).limitToFirst(1);
                                    recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            for (DataSnapshot shot : snapshot.getChildren()) {
                                                sitoTrovato = shot.getValue(CardSitoCulturale.class);

                                            }



                                            if(sitoTrovato == null){

                                            }else{

                                                myRef.child("Siti").child(sitoTrovato.getId()).setValue(null) ;


                                                BasicMethod.alertDialog(EliminaSito.this, "", getString(R.string.correctDeleteSite), "");


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
                               
                            }
                        }
                    });


                }
            }


        });
    }
}