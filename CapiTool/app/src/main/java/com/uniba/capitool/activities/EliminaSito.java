package com.uniba.capitool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


        EditText editPasswordCuratore = findViewById(R.id.edit_text_password);
        CheckBox boxInfo = findViewById(R.id.boxInfoDelete);
        Button buttonConferma = findViewById(R.id.button_conferma_delega);
        Button buttonAnnulla = findViewById(R.id.button_annulla_delega);

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
                    editPasswordCuratore.setError("Non hai inserito nessuna password");
                    //Toast.makeText(getApplicationContext(), "Non hai inserito nessuna password", Toast.LENGTH_SHORT).show();
                } else if (!boxInfo.isChecked()) {
                    boxInfo.setError("Devi confermare di aver letto l'informativa per procedere all'eliminazione");
                    //Toast.makeText(getApplicationContext(), "Devi confermare di aver letto l'informativa per procedere all'eliminazione", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseUser datiUtente = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(datiUtente.getEmail(), passwordInserita);

                    Log.e("Dati trovati", "email: " + datiUtente.getEmail() + "-----  passord inserita: " + passwordInserita);

                    datiUtente.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.e("Password inserita", "password corretta");
                                passwordCorretta = true;
                            } else {
                                editPasswordCuratore.setError("Password errata");
                                Log.e("Password inserita", "password ERRATA");
                                passwordCorretta = false;

                            }
                        }
                    });

                    Log.e("Valore di password corretta", passwordCorretta == true ? "corretta" : "non corretta") ;

                    if (passwordCorretta) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference("/Siti/");

                        Query recentPostsQuery = myRef.orderByChild("uidCuratore").equalTo(datiUtente.getUid()).limitToFirst(1);
                        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                sitoTrovato = new SitoCulturale();

                                for (DataSnapshot shot : snapshot.getChildren()) {
                                    sitoTrovato = (SitoCulturale) shot.getValue(SitoCulturale.class);
                                    sitoTrovato.setId(shot.getKey());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
                            }

                        });

                        if(sitoTrovato == null){
                            Toast.makeText(getApplicationContext(), "NEssun sito trovato", Toast.LENGTH_SHORT ) ;
                        }else{

                            Log.e("Sito trovato", "Nome: " + sitoTrovato.getNome() + "----- idSito: " + sitoTrovato.getId());

                            //myRef.child(sitoTrovato.getId()).setValue(null) ;
                                   /* Log.e("Elimina sito", "Sito elimianto!") ;
                                    Toast.makeText(getApplicationContext(),"Sito Eliminato con successo:)", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent (EliminaSito.this, HomePage.class) ;
                                    Bundle bundle = new Bundle() ;
                                    intent.putExtra("uid", utente.getUid()); //Optional parameters
                                    intent.putExtra("nome", utente.getNome()); //Optional parameters
                                    intent.putExtra("cognome", utente.getCognome()); //Optional parameters
                                    intent.putExtra("email", utente.getEmail()); //Optional parametersù
                                    intent.putExtra("ruolo", utente.getRuolo()); //Optional parameters
                                    intent.putExtras(bundle);
                                    startActivity(intent);*/
                        }

                    }else{
                        Log.e("Azioni dopo password corretta", "Non ho fatto nient'altro !!! *** ");
                    }


                }
            }


        });
    }
}