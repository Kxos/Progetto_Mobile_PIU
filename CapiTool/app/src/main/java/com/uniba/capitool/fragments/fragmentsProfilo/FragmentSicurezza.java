package com.uniba.capitool.fragments.fragmentsProfilo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.*;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.classes.Utente;

public class FragmentSicurezza extends Fragment {

    Utente utente;
    CheckBox mostraPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sicurezza, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utente= BasicMethod.getUtente();    //recupero l'utente che ha fatto il login dalla activity HomePage

        String email = utente.getEmail();
        TextInputEditText vecchiaPsw = view.findViewById(R.id.textfield_VecchiaPassword);
        TextInputEditText nuovaPsw = view.findViewById(R.id.textfield_NuovaPassword);
        TextInputEditText confermaPsw = view.findViewById(R.id.textfield_ConfermaPassword);
        Button conferma = view.findViewById(R.id.confermaModificaPassword);
        mostraPassword = view.findViewById(R.id.mostraPassword);

        conferma.setEnabled(true);

        vecchiaPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isFilled(vecchiaPsw)) {
                    vecchiaPsw.setError("Campo obbligatorio");
                }
            }
        });

        nuovaPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isFilled(nuovaPsw)) {
                    nuovaPsw.setError("Campo obbligatorio");
                }
            }
        });

        confermaPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isFilled(confermaPsw)) {
                    confermaPsw.setError("Campo obbligatorio");
                }
            }
        });

        //ascoltatore della CheckBox per mostrare la password
        mostraPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mostraPassword.isChecked()){
                    vecchiaPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    nuovaPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confermaPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    vecchiaPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    nuovaPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confermaPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean errorePassword=false;

                if(!isFilled(vecchiaPsw)) {
                    vecchiaPsw.setError("Campo obbligatorio");
                }
                if(!isFilled(nuovaPsw)) {
                    nuovaPsw.setError("Campo obbligatorio");
                }
                if(!isFilled(confermaPsw)) {
                    confermaPsw.setError("Campo obbligatorio");
                }

                if(isFilled(vecchiaPsw) && isFilled(nuovaPsw) && isFilled(confermaPsw)) {
                    //controllo se la password è sicura
                    String messaggio=BasicMethod.isPasswordStrong(nuovaPsw.getText().toString());

                    if(messaggio==null){      // Significa che la password è forte e non ho ricevuto messaggi di errore in ritorno /

                        if(nuovaPsw.getText().toString().equals(confermaPsw.getText().toString())==false){
                            confermaPsw.setError("La password non corrisponde");
                            errorePassword=true;
                        }
                        else if(nuovaPsw.getText().toString().equals(vecchiaPsw.getText().toString())==true) {
                            nuovaPsw.setError("La nuova password è uguale alla vecchia");
                            errorePassword=true;
                        }
                    }else{

                        if(messaggio.equals("pswReq1")){
                            nuovaPsw.setError(getString(R.string.pswReq1));
                        }else if(messaggio.equals("pswReq2")){
                            nuovaPsw.setError(getString(R.string.pswReq2));
                        }else if(messaggio.equals("pswReq3")){
                            nuovaPsw.setError(getString(R.string.pswReq3));
                        }else{
                            nuovaPsw.setError(getString(R.string.pswReq4));
                        }

                        errorePassword=true;
                    }

                    //se i campi sono compilati, non ci sono errori di formato e la password è sicura procedo
                    if(errorePassword==false) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        // Get auth credentials from the user for re-authentication. The example below shows
                        // email and password credentials but there are multiple possible providers,
                        // such as GoogleAuthProvider or FacebookAuthProvider.
                        AuthCredential credential = EmailAuthProvider.getCredential(email, vecchiaPsw.getText().toString());

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(nuovaPsw.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "Password updated");
                                                Toast.makeText(((HomePage)getActivity()), "Password aggiornata correttamente", Toast.LENGTH_LONG).show();
                                            } else {
                                                Log.d("TAG", "Error password not updated");
                                                Toast.makeText(((HomePage)getActivity()), "C'è stato un errore. Riprova", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Log.d("TAG", "Error auth failed");
                                    vecchiaPsw.setError("Password errata");
                                }
                            }
                        });
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

}