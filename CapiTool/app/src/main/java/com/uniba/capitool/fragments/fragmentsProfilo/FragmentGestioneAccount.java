package com.uniba.capitool.fragments.fragmentsProfilo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.activities.Login;
import com.uniba.capitool.classes.CardSitoCulturale;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;

public class FragmentGestioneAccount extends Fragment {

    Utente utente;
    CheckBox mostraPassword;
    CheckBox confermaInformativa;
    Object riferimentoSnapshotSitoAssociato;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestione_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utente = BasicMethod.getUtente();    //recupero l'utente che ha fatto il login dalla activity HomePage

        String email = utente.getEmail();
        TextInputEditText psw = view.findViewById(R.id.edit_text_password);
        psw.setTransformationMethod(PasswordTransformationMethod.getInstance());

        Button conferma = view.findViewById(R.id.confermaEliminazioneAccount);
        confermaInformativa = view.findViewById(R.id.boxInfoCancellazioneAccount);

        conferma.setEnabled(true);

        psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isFilled(psw)) {
                    psw.setError("Campo obbligatorio");
                }
            }
        });


        checkSitoAssociatoAlCuratore(utente.getUid());

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isFilled(psw)) {
                    psw.setError("Campo obbligatorio");
                }
                else {
                    if(!confermaInformativa.isChecked()) {
                        confermaInformativa.setError("Devi confermare di aver letto l'informativa");
                    }
                    else {
                        if(utente.getRuolo().equals("curatore") && riferimentoSnapshotSitoAssociato != null) {
                            Log.d("TAG", "Sito ancora presente");
                            Toast.makeText(getActivity(), "Impossibile eliminare l'account. Delega il tuo sito oppure eliminalo.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.e("TAG", "ramo else attivo");
                            String ruolo = utente.getRuolo();

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            // Get auth credentials from the user for re-authentication. The example below shows
                            // email and password credentials but there are multiple possible providers,
                            // such as GoogleAuthProvider or FacebookAuthProvider.
                            AuthCredential credential = EmailAuthProvider.getCredential(email, psw.getText().toString());

                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                                        DatabaseReference myRef = database.getReference("/");

                                        myRef.child("Utenti").child(user.getUid()).setValue(null);

                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Log.d("TAG", "Account eliminato");

                                                    Intent login = new Intent(getActivity(), Login.class);
                                                    startActivity(login);

                                                    Toast.makeText(((Login)getActivity()), "Account eliminato correttamente", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Log.d("TAG", "Errore eliminazione account");
                                                    Toast.makeText(getActivity(), "C'è stato un errore. Riprova", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d("TAG", "Error auth failed");
                                        psw.setError("Password errata");
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    public boolean isFilled(TextInputEditText text) {
        if(text.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    public void checkSitoAssociatoAlCuratore (String uidUtente) {

        Log.e("*****!!!!*****","controllo se c'è un sito associato al curatore!");
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        //-------------------------------------------------------------------------------------
        // Ricerca per Nome

        recentPostsQuery = myRef.child("Siti").orderByChild("uidCuratore").equalTo(uidUtente).limitToFirst(1);     //SELECT * FROM Siti WHERE getUid LIKE "..."
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.e("*****!!!!*****","SONO NEL FOR!!!");

                     riferimentoSnapshotSitoAssociato = snapshot.getValue();
                     if(riferimentoSnapshotSitoAssociato == null) {
                         Log.e("snap", "null");
                     }
                     else {
                         Log.e("snap", "filled");
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