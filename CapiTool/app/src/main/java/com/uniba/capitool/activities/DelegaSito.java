package com.uniba.capitool.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.CardSitoCulturale;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.Utente;

public class DelegaSito extends AppCompatActivity {

    Utente delegato;
    Dialog dialog ;
    CardSitoCulturale sitoDaDelegare ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delega_sito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbarDelegateSite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        Curatore utente = (Curatore) bundle.getSerializable("utente");

        EditText editEmailDelegato = findViewById(R.id.edit_text_email);
        CheckBox boxInfo = findViewById(R.id.boxInfoCancellazioneAccount);
        Button buttonConferma = findViewById(R.id.button_conferma_delega);
        Button buttonAnnulla = findViewById(R.id.button_annulla_delega);

        buttonConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailDelegato = editEmailDelegato.getText().toString();

                if (emailDelegato.isEmpty()) {

                    editEmailDelegato.setError(getString(R.string.noEmailInsert));
                } else if (!boxInfo.isChecked()) {

                    boxInfo.setError(getString(R.string.boxInfoNotChecked));
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("/");
                    /***
                     * Controllo nel database se c'?? un curatore a cui ?? associata l'email inserita nel'editText
                     */

                    Query recentPostsQuery = myRef.child("Utenti").orderByChild("email").equalTo(emailDelegato).limitToFirst(1);
                    recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Utente utenteTrovato = new Utente();
                            // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                utenteTrovato=snapshot.getValue(Utente.class);
                                utenteTrovato.setUid(snapshot.getKey());
                            }

                            if (utenteTrovato==null) {

                                Toast.makeText(getApplicationContext(), getString(R.string.userNotFound), Toast.LENGTH_SHORT).show();
                            } else {

                                delegato = utenteTrovato;

                                 if(delegato.getRuolo().equals("curatore")){
                                    /**
                                     * Trovato il curatore a cui delegare il sito, controllo che non abbia gi?? un sito associato
                                     */

                                    Query recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(delegato.getUid()).limitToFirst(1);
                                    recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            CardSitoCulturale potenzialeSitoDelegato = new CardSitoCulturale() ;
                                            // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                potenzialeSitoDelegato = snapshot.getValue(CardSitoCulturale.class);

                                            }

                                            if (potenzialeSitoDelegato == null) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.userhaveAlreadySite), Toast.LENGTH_SHORT).show();
                                            }else{

                                                Query recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(utente.getUid()).limitToFirst(1);  //recupero il sito dell'utente che vuole delegare per poi cambiare Uid assocociato con quello del nuovo curatore
                                                recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        CardSitoCulturale sitoUtente = new CardSitoCulturale() ;
                                                        // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                            sitoUtente = snapshot.getValue(CardSitoCulturale.class);
                                                        }

                                                        if (sitoUtente != null) {

                                                            sitoDaDelegare = sitoUtente ;

                                                            dialog = new Dialog(DelegaSito.this) ;
                                                            dialog.setContentView(R.layout.delega_dialog);
                                                            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                                                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                            dialog.setCancelable(false);

                                                            Button dialog_confirm = dialog.findViewById(R.id.btn_confirm) ;
                                                            Button dialog_cancel = dialog.findViewById(R.id.btn_cancel) ;

                                                            dialog_confirm.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    myRef.child("Siti").child(sitoDaDelegare.getId()).child("uidCuratore").setValue(delegato.getUid());
                                                                    BasicMethod.alertDialog(DelegaSito.this, "", getString(R.string.SuccesfulDelegateSite), "");

                                                                    Intent intent = new Intent (DelegaSito.this, HomePage.class) ;
                                                                    Bundle bundle = new Bundle() ;
                                                                    intent.putExtra("uid", utente.getUid()); //Optional parameters
                                                                    intent.putExtra("nome", utente.getNome()); //Optional parameters
                                                                    intent.putExtra("cognome", utente.getCognome()); //Optional parameters
                                                                    intent.putExtra("email", utente.getEmail()); //Optional parameters??
                                                                    intent.putExtra("ruolo", utente.getRuolo()); //Optional parameters
                                                                    intent.putExtras(bundle);
                                                                    startActivity(intent);

                                                                    dialog.dismiss();

                                                                }
                                                            });

                                                            dialog_cancel.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    dialog.dismiss();
                                                                }
                                                            });

                                                            dialog.show();

                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "Errore nella delega :(", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        // Getting Post failed, log a message
                                                        Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
                                                    }
                                                });

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Getting Post failed, log a message
                                            Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
                                        }
                                    });

                                }else{
                                    Toast.makeText(getApplicationContext(), getString(R.string.errorDelegate2), Toast.LENGTH_SHORT).show();

                                    Log.e("Non ?? un curatore", "ruolo: " + delegato.getRuolo());

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Getting Post failed, log a message
                            Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
                        }
                    });
                }
            }

        });




        buttonAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (DelegaSito.this, HomePage.class) ;
                Bundle bundle = new Bundle() ;
                intent.putExtra("uid", utente.getUid()); //Optional parameters
                intent.putExtra("nome", utente.getNome()); //Optional parameters
                intent.putExtra("cognome", utente.getCognome()); //Optional parameters
                intent.putExtra("email", utente.getEmail()); //Optional parameters??
                intent.putExtra("ruolo", utente.getRuolo()); //Optional parameters
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             DelegaSito.super.onBackPressed();
            }
        });


    }
}