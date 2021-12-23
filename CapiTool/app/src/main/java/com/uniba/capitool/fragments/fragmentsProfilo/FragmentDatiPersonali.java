package com.uniba.capitool.fragments.fragmentsProfilo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.classes.Visitatore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentDatiPersonali extends Fragment {
    private static final int SELECT_IMAGE_CODE = 1;
    private Uri imageUri;
    CircleImageView fotoProfilo;
    TextInputEditText dataNascita;
    Utente utente;
    ProgressBar progressBar;
    Button conferma;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dati_personali, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText email= view.findViewById(R.id.text_email);
        TextInputEditText nome= view.findViewById(R.id.text_nome);
        TextInputEditText cognome= view.findViewById(R.id.text_cognome);
        dataNascita= view.findViewById(R.id.text_dataNascita);
        TextInputLayout layoutPatentino= view.findViewById(R.id.layout_numeroPatentino);
        TextInputEditText patentino= view.findViewById(R.id.text_numeroPatentino);
        fotoProfilo = view.findViewById(R.id.imageProfile);
        progressBar = view.findViewById(R.id.progress_circularDatiPersonali);
        ScrollView scrollView=view.findViewById(R.id.scrollViewDatiPersonali);

        conferma= view.findViewById(R.id.confermaModifiche);
        conferma.setEnabled(false);

        utente=BasicMethod.getUtente();    //recupero l'utente che ha fatto il login dalla activity HomePage
        email.setText(utente.getEmail());
        nome.setText(utente.getNome());
        cognome.setText(utente.getCognome());

        /**
         * Cerco se l'utente ha già una imagine di profilo, se si la carico
         *  TODO: se l'utente non ha nessun immagine impostata si genera un errore (fare un controllo)
         * */
        letturaImmagineDB();

        if(!utente.getRuolo().equals("guida")){
            layoutPatentino.setHint("");
            patentino.setEnabled(false);
            patentino.setBackground(null);
            patentino.setFocusable(false);

            ConstraintLayout constraintLayout = view.findViewById(R.id.constraint_layout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.confermaModifiche,ConstraintSet.TOP,R.id.layout_dataNascita,ConstraintSet.BOTTOM,30);
           // constraintSet.connect(R.id.imageView,ConstraintSet.TOP,R.id.check_answer2, ConstraintSet.TOP,0);
            constraintSet.applyTo(constraintLayout);
        }

        setDataNascitaUtente(utente.getEmail());

        fotoProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleziona foto profilo"), SELECT_IMAGE_CODE);

            }
        });

        nome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(nome.getText().toString().equals("")){
                    nome.setError("Inserisci un nome valido");
                    conferma.setEnabled(false);
                }else{
                    conferma.setEnabled(true);
                }
            }
        });

        cognome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(cognome.getText().toString().equals("")){
                    cognome.setError("Inserisci un cognome valido");
                    conferma.setEnabled(false);
                }else{
                    conferma.setEnabled(true);
                }
            }
        });

        dataNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no view has focus else hide KEYBOARD
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                BasicMethod.apriCalendario(getActivity(), dataNascita);
                conferma.setEnabled(true);
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                updateDatiUtente(nome.getText().toString(), cognome.getText().toString(), dataNascita.getText().toString());
            }
        });

    }


    private void updateDatiUtente(String nome, String cognome, String dataNascita) {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef;

        myRef=database.getReference("/Utenti/"+utente.getUid()+"/nome");
        myRef.setValue(nome);

        myRef=database.getReference("/Utenti/"+utente.getUid()+"/cognome");
        myRef.setValue(cognome);

        myRef=database.getReference("/Utenti/"+utente.getUid()+"/dataNascita");
        myRef.setValue(dataNascita);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                conferma.setEnabled(false);
                //TODO non compare il toast
                Toast.makeText(((HomePage)getActivity()), "Dati personali aggiornati", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 1 && data!=null){
            Log.e("***********************************************************","");
            imageUri=data.getData();
            if(imageUri!=null){
                fotoProfilo.setImageURI(imageUri);

                StorageReference fileReference= FirebaseStorage.getInstance().getReference().child("fotoUtenti").child(utente.getUid());

                final ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setMessage("Caricamento");
                pd.show();

                /**
                 * Upload dell'immagine selezionata da Android in Firebase Storage
                 * */
                fileReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        pd.dismiss();
                    }
                });
            }
        }
    }


    public void setDataNascitaUtente(String email){

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("/");

            Query recentPostsQuery = myRef.child("Utenti").orderByChild("email").equalTo(email);     //SELECT * FROM Utenti WHERE ruolo="visitatore"
            recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    List<Visitatore> visitatori=new ArrayList<>();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i valori trovati dalla query, anche se è 1 solo
                        Visitatore visitatore=snapshot.getValue(Visitatore.class);  //così facendo si associa un l'oggetto della query nell'oggetto della classe
                        visitatori.add(visitatore);                                 //se ci sarà più di un risultato nella query creiamo una lista di oggetti per gestirli da codice
                    }

                    //setto nella EditText il valore della data trovata
                    setDataNascita(visitatori.get(0).getDataNascita());
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Getting Post failed, log a message
                    Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
                }
            });
    }

    private void setDataNascita(String dataN) {
        dataNascita.setText(dataN);
    }

    public void letturaImmagineDB(){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/fotoUtenti/" + utente.getUid());

        /**
         * Scarica il "DownloadURL" che ci serve per leggere l'immagine dal DB e metterla in una ImageView
         * */
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                //do something with downloadurl
                Glide.with(getActivity())
                        .load(downloadUrl)
                        .into(fotoProfilo);
            }

        });

    }

}