package com.uniba.capitool.activities;

import static com.uniba.capitool.activities.BasicMethod.isEmailValid;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Visitatore;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    EditText email;
    TextInputEditText password;
    ProgressBar progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button_login;
        button_login = findViewById(R.id.button_login);

        email = findViewById(R.id.textfield_email);
        password = findViewById(R.id.textfield_password);

        progressCircle =findViewById(R.id.progressLogin);

        /**
         *
         * Set di email e password di curatore così accediamo più facilemnte
         *
         * */
        // TODO -- DA RIMUOVERE, ACCESSO VELOCE IN LOGIN
        //email.setText("Curatore@gmail.com");
        //password.setText("Cur123/");
        
        TextView registrati;
        registrati = findViewById(R.id.textView_register);
        registrati.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        registrati.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Visitatore visit= new Visitatore();

                Intent intentRegistrati = new Intent(Login.this, Registrati.class);

                Login.this.startActivity(intentRegistrati);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressCircle.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                String email_value = email.getText().toString();
                String password_value = password.getText().toString();

                if(isEmailValid(email_value)){
                    signIn(email_value,password_value);
                }else{
                    email.setError(getString(R.string.insertEmail));
                    progressCircle.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }


            }
        });

    }

    // Effettua il login tramite email e password. Gestito da AUthentication di Firebase
    public void signIn(String email, String password){

        if(email.isEmpty())   { email = "none"; }
        if(password.isEmpty()){ password = "none"; }

        //inizializzazione Autenticazione Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);

                        }
                    }
                });
    }


    /***
     * Mostra un alert se ci sono problemi nelle credenziali dell'utente,
     * Altrimenti procede con la fase di Login
     *
     * @param user: Utente considerato
     */
    private void updateUI(FirebaseUser user) {
        if(user == null){
            BasicMethod.alertDialog(this, getString(R.string.checkInsertData), getString(R.string.accountNotFound), "Ok");
            progressCircle.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        if(user != null) {

            getUserFromDB(user);
        }

    }

    /***
     * Ottiene tutti gli attributi di un utente
     *
     * @param user: Utente effettuante il Login
     */
    public void getUserFromDB(FirebaseUser user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        Query recentPostsQuery = myRef.child("Utenti").orderByChild("email").equalTo(user.getEmail());     //SELECT * FROM Utenti WHERE email="test@example.it"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Visitatore> visitatori = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i valori trovati dalla query, anche se è 1 solo
                    Visitatore visitatore=snapshot.getValue(Visitatore.class);  //così facendo si associa un oggetto della query nell'oggetto della classe
                    visitatori.add(visitatore);                                 //se ci sarà più di un risultato nella query creiamo una lista di oggetti per gestirli da codice
                }

                /**
                //ciclo for per scorrere la lista ottenuta dal db
                int contatore=0;
                for(Visitatore visitatore : visitatori){
                    Log.e("OGGETTO RESTITUITO "+(contatore+1)+" della lista visitatori", BasicMethod.getAllVisitatore(visitatori.get(contatore)));
                    contatore++;
                }
                 */

                //Log.d("Lunghezza lista della query", String.valueOf(visitatori.size()));
                goToHomePage(user.getUid(), visitatori.get(0).getNome(), visitatori.get(0).getCognome(), visitatori.get(0).getEmail(), visitatori.get(0).getRuolo());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }

    /***
     * Va alla HomePage passando i dati dell'utente Loggato
     *
     * @param uid
     * @param nome
     * @param cognome
     * @param email
     */
    public void goToHomePage(String uid, String nome, String cognome, String email, String ruolo){
        Intent myIntent = new Intent(Login.this, HomePage.class);
        myIntent.putExtra("uid", uid); //Optional parameters
        myIntent.putExtra("nome", nome); //Optional parameters
        myIntent.putExtra("cognome", cognome); //Optional parameters
        myIntent.putExtra("email", email); //Optional parametersù
        myIntent.putExtra("ruolo", ruolo); //Optional parameters
        Login.this.startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

    }
}