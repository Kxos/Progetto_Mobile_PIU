package com.uniba.capitool;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class FragmentRegistraCredenziali extends Fragment {

private FirebaseAuth mAuth;
Button avanti;
EditText email;
EditText password;
EditText confermaPassword;
CheckBox mostraPassword;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", email.getText().toString());
        outState.putString("password", password.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View v= inflater.inflate(R.layout.fragment_registra_credenziali, container, false);

       avanti= v.findViewById(R.id.avanti);
       email=v.findViewById(R.id.email);
       password=v.findViewById(R.id.password);
       confermaPassword=v.findViewById(R.id.confermaPassword);

        if (savedInstanceState != null) {
            email.setText(savedInstanceState.getString("email"));
            password.setText(savedInstanceState.getString("password"));
        } else {
           // randomGoodDeed = viewModel.generateRandomGoodDeed();
        }
        Bundle bundle = this.getArguments();
       if(bundle!=null){
           email.setText(bundle.get("email").toString());
           password.setText(bundle.get("password").toString());
       }
       mostraPassword=v.findViewById(R.id.mostraPassword);

       mAuth = FirebaseAuth.getInstance();

       //ascoltatore della CheckBox per mostrare la password
       mostraPassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mostraPassword.isChecked()){
                   password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                   confermaPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
               }else{
                   password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                   confermaPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

               }
           }
       });


       avanti.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               boolean erroreEmail=false;
               boolean errorePassword=false;

                //controllo se la mail è sicura
                if(BasicMethod.isEmailValid(email.getText().toString())==false){
                        email.setError("Inserisci una email valida");
                        erroreEmail=true;
                }

               //controllo se la password è sicura
                String messaggio=BasicMethod.isPasswordStrong(password.getText().toString());

                if(messaggio==null){      // Significa che la password è forte e non ho ricevuto messaggi di errore in ritorno /
                                                // Nota: if(messaggio.getText==null) non funziona
                    if(password.getText().toString().equals(confermaPassword.getText().toString())==false){
                        confermaPassword.setError("La password non corrisponde");
                        errorePassword=true;
                    }
                }else{
                    password.setError(messaggio);
                    errorePassword=true;
                }

                //se non ci sono errori di formato nella mail e la password è sicura procedo
                if(erroreEmail==false && errorePassword==false){

                    //con questo metodo controllo se l'email non è già presente in Authentication di Firebase
                    checkIfUserAlreadyExist(email.getText().toString());

                }

           }
       });

        return v;

    }

    public void checkIfUserAlreadyExist(String email1){

        //check email already exist or not.
        mAuth.fetchSignInMethodsForEmail(email1)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {

                            Log.e("TAG", "Is New User!");
                            confermaCredenziali(true);

                        } else {

                            Log.e("TAG", "Is Old User!");
                            email.setError("Utente già esistente");
                            confermaCredenziali(false);
                        }
                    }
                });

    }

    public void confermaCredenziali(boolean isNewUser){
        if(isNewUser==true){
            Log.d( "************************: ", "TUTTO OK");

            Bundle bundle = new Bundle();
            bundle.putString("email",email.getText().toString());
            bundle.putString("password",password.getText().toString());
            FragmentRegistraRuolo fragmentRegistraRuolo = new FragmentRegistraRuolo();
            fragmentRegistraRuolo.setArguments(bundle);

            FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragmentRegistraRuolo);
            fragmentTransaction.commit();

        }else{
            Log.d( "************************: ", "NON OK");
        }
    }

    /*
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

*/

}