package com.uniba.capitool;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uniba.capitool.classes.Visitatore;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button_login;
        button_login = findViewById(R.id.button_login);

        email = findViewById(R.id.textfield_email);
        password = findViewById(R.id.textfield_password);

        TextView registrati;
        registrati = findViewById(R.id.textView_register);
        registrati.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        registrati.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Visitatore visit= new Visitatore();
                visit.setCognome("Nardo");
                Intent intentRegistrati = new Intent(Login.this, Registrati.class);
                intentRegistrati.putExtra("cognome",visit.getCognome());
                intentRegistrati.putExtra("nome",visit.getNome());
                Login.this.startActivity(intentRegistrati);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email_value = email.getText().toString();
                String password_value = password.getText().toString();

                if(isEmailValid(email_value)){
                    signIn(email_value,password_value);
                }else{
                    email.setError("Inserisci un Email");
                }
            }
        });

    }

    // Effettua il login tramite email e password. Gestito da AUthentication di Firebase
    public boolean signIn(String email, String password){

        if(email.isEmpty())   { email = "none"; }
        if(password.isEmpty()){ password = "none"; }

        final boolean[] risultato = {false};

        //inizializzazione Autenticazione Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("uid Utente", mAuth.getUid());
                            updateUI(user);
                            risultato[0] = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            risultato[0] =  false;
                        }
                    }
                });
        return risultato[0];
    }


    private void updateUI(FirebaseUser user) {
        if(user == null){
            Log.d("----------------------------------------------","---------------------------------------------------------");
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

            builder.setTitle("Account non trovato!");
            builder.setMessage("Controlla i dati inseriti e riprova.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog invalidDialogError = builder.create();
            invalidDialogError.show();

        }

        if(user != null) {
            Log.d("User_ID: ", user.getUid());

            // Passaggio da una Activity ad un altra
            Intent myIntent = new Intent(Login.this, HomePage.class);
            myIntent.putExtra("User_ID", user.getUid()); //Optional parameters
            Login.this.startActivity(myIntent);
        }

    }

    /***
     *
     * @param email - Email da verificare
     * @return Boolean
     */
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}