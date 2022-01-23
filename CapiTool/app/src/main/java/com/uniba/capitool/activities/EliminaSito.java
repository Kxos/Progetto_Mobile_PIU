package com.uniba.capitool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Curatore;

public class EliminaSito extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimina_sito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbarDeleteSite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras() ;
        Curatore utente = (Curatore) bundle.getSerializable("utente") ;


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
        });


        buttonConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passwordInserita =  editPasswordCuratore.getText().toString() ;

                if (passwordInserita.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Non hai inserito nessuna password", Toast.LENGTH_SHORT).show();
                } else if (!boxInfo.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Devi confermare di aver letto l'informativa per procedere all'eliminazione", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("/");



                }
            }
        });




    }
}