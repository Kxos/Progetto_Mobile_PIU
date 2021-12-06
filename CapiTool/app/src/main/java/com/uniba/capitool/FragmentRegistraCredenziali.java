package com.uniba.capitool;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.internal.GenericIdpActivity;
import com.uniba.capitool.BasicMethod;

public class FragmentRegistraCredenziali extends Fragment {

private FirebaseAuth mAuth;
Button avanti;
EditText email;
EditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View v= inflater.inflate(R.layout.fragment_registra_credenziali, container, false);

       avanti= v.findViewById(R.id.avanti);
       email=v.findViewById(R.id.email);
       password=v.findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

       avanti.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

                createAccount(email.getText().toString(), password.getText().toString());

           }
       });


        return v;



    }

    //crea un nuovo utente con umail e password che userà per accedervi. Gestito da Auth di Firebase
    public void createAccount(String email, String password){


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUIfragment(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUIfragment(null);
                        }
                    }
                });

    }

    private void updateUIfragment(FirebaseUser user) {
        if(user!=null){

            Bundle bundle = new Bundle();
            bundle.putString("email",email.getText().toString()); // Put anything what you want
            bundle.putString("password",password.getText().toString());
            FragmentRegistraRuolo fragmentRegistraRuolo = new FragmentRegistraRuolo();
            fragmentRegistraRuolo.setArguments(bundle);

            FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragmentRegistraRuolo);
            fragmentTransaction.commit();

        }else{

            BasicMethod.alertDialog(getActivity(), "Credenziali esistenti", "Errore", "Ok");
        }
    }



}