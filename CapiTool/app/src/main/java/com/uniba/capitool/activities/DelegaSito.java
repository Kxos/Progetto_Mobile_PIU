package com.uniba.capitool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Curatore;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

import java.util.ArrayList;

public class DelegaSito extends AppCompatActivity {

    Utente delegato;
    SitoCulturale sitoDaDelegare;

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
        CheckBox boxInfo = findViewById(R.id.boxInfoDelegate);
        Button buttonConferma = findViewById(R.id.button_conferma_delega);
        Button buttonAnnulla = findViewById(R.id.button_annulla_delega);

        String emailDelegato = editEmailDelegato.getText().toString();


        buttonConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailDelegato = editEmailDelegato.getText().toString();

                if (emailDelegato.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Non hai inserito nessuna email", Toast.LENGTH_SHORT).show();
                } else if (!boxInfo.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Devi confermare di aver letto l'informativa per procedere com la delega", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("/");
                    /***
                     * Controllo nel database se c'è un curatore a cui è associata l'email inserita nel'editText
                     */
                    Query recentPostsQuery = myRef.child("Utenti").orderByChild("email").equalTo(emailDelegato);
                    recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Utente> utenti = new ArrayList<>();
                            // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.e("*****!!!!*****", "SONO NEL FOR!!!");
                                utenti.add(snapshot.getValue(Utente.class));

                            }

                            if (utenti.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Nessun utente trovato", Toast.LENGTH_SHORT).show();
                            } else {


                                delegato = utenti.get(0);


                                if (delegato.getRuolo() != "curatore") {
                                    Toast.makeText(getApplicationContext(), "Non puoi delegare un sito ad un utente non curatore", Toast.LENGTH_SHORT).show();
                                } else {
                                    /**
                                     * Trovato il curatore a cui delegare il sito, controllo che non abbia già un sito associato
                                     */
                                    Log.e("*****!!!!*****", "E' un curaotre, controllo se c'è gia un sito");

                                    Query recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(delegato.getUid());
                                    recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            ArrayList<SitoCulturale> siti = new ArrayList<>();
                                            // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                Log.e("*****!!!!*****", "SONO NEL FOR!!!");
                                                siti.add(snapshot.getValue(SitoCulturale.class));
                                            }

                                            if (siti.size() != 0) {
                                                Toast.makeText(getApplicationContext(), "Non è possibile delegare il sito ad un curatore che ne ha già associato uno", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.e("*****!!!!*****", "Non ha un sito quindi posso delegare");


                                                Query recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(utente.getUid());  //recupero il sito dell'utente che vuole delegare per poi cambiare Uid assocociato con quello del nuovo curatore
                                                recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        ArrayList<SitoCulturale> siti = new ArrayList<>();
                                                        // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            Log.e("*****!!!!*****", "SONO NEL FOR!!!");
                                                            siti.add(snapshot.getValue(SitoCulturale.class));
                                                        }


                                                        if (siti.size() != 0) {
                                                            sitoDaDelegare = siti.get(0);
                                                            sitoDaDelegare.setUidCuratore(delegato.getUid());
                                                            Toast.makeText(getApplicationContext(), "Sito Delegato con Successo ! :)", Toast.LENGTH_SHORT).show();
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


    }
}