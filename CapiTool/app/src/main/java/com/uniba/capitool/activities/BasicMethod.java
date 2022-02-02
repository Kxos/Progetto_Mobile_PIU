package com.uniba.capitool.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.classes.Visitatore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/***
 * Classe che contiene i Metodi che vengono più riutilizzati nell'App
 */
public class BasicMethod extends AppCompatActivity {
    
    static Utente utente;
    static NavController navController;

    public static void alertDialog(Activity activity, String messaggio, String titolo, String messaggioBottone){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(titolo);
        builder.setMessage(messaggio);
        builder.setPositiveButton(messaggioBottone, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog invalidDialogError = builder.create();
        invalidDialogError.show();
    }


    /***
     *
     * @return risultato: Stringa formattata con gli attributi di un Visitatore
     */
    public static String getAllVisitatore(Visitatore visitatore){
        String risultato=""+visitatore.getNome()+","+visitatore.getCognome()+","+visitatore.getEmail()+","+visitatore.getRuolo()+","+visitatore.getDataNascita();
        return risultato;
    }


    /***
     *
     * @param email: Email da verificare
     * @return Boolean
     */
    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String isPasswordStrong(CharSequence password) {

        String MAIUSC="QWERTYUIOPASDFGHJKLZXCVBNM";
        String minusc="qwertyuiopasdfghjklzxcvbnm";
        String special="|€<>,.-;:_!£$%&/(=)?^+*@#§/";

        String messaggio = null;
        if(password.length()<6){
            messaggio="Inserisci una password con almeno 6 caratteri";
        }else{
            int i, j;
            boolean letteraMaiuscola=false;
            boolean letteraminuscola=false;
            boolean carattereSpeciale=false;

            for(i=0; i<password.length(); i++){     // vedo ogni carattere della password per vedere se è una lettera MAIUSCOLA, minuscola o un carattere speciale
               if(letteraMaiuscola==false){ //in questo modo non controllo le latre lettere se ho già trovato una lettera MAIUSCOLA
                   for (j = 0; j < MAIUSC.length(); j++) {
                       if (password.charAt(i) == MAIUSC.charAt(j)) {  //ho trovato una lettera MAIUSCOLA nella password
                           letteraMaiuscola = true;
                           break;
                       }
                   }
               }
                if(letteraminuscola==false){
                    for(j=0; j<minusc.length(); j++){
                        if(password.charAt(i)==minusc.charAt(j)){   //ho trovato una lettera minuscola
                            letteraminuscola=true;
                            break;
                        }
                    }
                }

                if(carattereSpeciale==false){
                    for(j=0; j<special.length(); j++){
                        if(password.charAt(i)==special.charAt(j)){   //ho trovato una lettera minuscola
                            carattereSpeciale=true;
                            break;
                        }
                    }
                }
            } // fine ciclo controllo password

            if (letteraMaiuscola==false) {
                messaggio="Inserisci almeno una lettera Maiuscola";
            }

            if(letteraminuscola==false){
                messaggio="Inserisci almeno una lettera minuscola";
            }

            if(carattereSpeciale==false){
                messaggio="Inserisci almeno una carattere speciale (ad es. !/*+)";
            }
        }

        // se messaggio=null significa che la password è sicura
        return messaggio;
    }

    /***
     * Controlla che un utente è un curatore
     *
     * @param ruolo
     * @return boolean
     */
    public static boolean isCuratore(String ruolo){
        if(ruolo.equals("curatore")){ return true; }
        return false;
    }

    /***
     *
     * @param activity: Activity dove viene chiamato il metodo
     * @param utente: l'utente valorizzato dei suoi dati
     * @param activityDiDestinazione: Activity di destinazione
     * @return intent: Restituisce l'intent con il bundle
     */
    public static Intent putUtenteExtrasInIntent(Context activity, Utente utente, Class activityDiDestinazione){
        Intent intent = new Intent(activity, activityDiDestinazione);
        intent.putExtra("uid", utente.getUid()); //Optional parameters
        intent.putExtra("nome", utente.getNome()); //Optional parameters
        intent.putExtra("cognome", utente.getCognome()); //Optional parameters
        intent.putExtra("email", utente.getEmail()); //Optional parameters
        intent.putExtra("ruolo", utente.getRuolo()); //Optional parameters
        return intent;
    }

    /** CREAZIONE DELLA NAVIGATION DRAWER BAR -----------------------------------------------------------------------------*/

    /***
     *
     * Inizializza il Drawer laterale con gli item di navigation_RUOLO_menu
     *
     * @param toolbar: La toolbar predefinita
     * @return navController: Impostato dell navigationView configurato
     */
    public NavController startNavLateralMenu(Toolbar toolbar, Activity activity) {
        NavigationView navigationView = activity.findViewById(R.id.Home_Nav_Menu);
        navigationView.setItemIconTintList(toolbar.getBackgroundTintList());
        navigationView.setItemTextColor(null);
//        Navigation.findNavController()
//                .navigate(R.id.action_firstFragment_to_secondFragment);

        setNavLateralMenuOnUserRole(navigationView,activity);

        NavigationUI.setupWithNavController(navigationView, navController);

        // In base al nome del Fragment, cambia il Titolo sulla Toolbar
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                toolbar.setTitle(destination.getLabel());

                switch (destination.getId()){

                    // Torna al Login
                    case R.id.disconnetti:
                        Intent myIntent = new Intent(activity, Login.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(myIntent);
                        break;
                }
            }
        });

        return navController;
    }

    /***
     * Imposta il Menù laterale in base al Ruolo dell'utente
     *
     * @param navigationView: Viene passata la navigationView legata alla Toolbar
     */
    public static void setNavLateralMenuOnUserRole(NavigationView navigationView, Activity activity){
        navigationView.getMenu().clear();

        Bundle b = activity.getIntent().getExtras();

        if(BasicMethod.isCuratore(b.getString("ruolo"))){
            // utente = new Curatore();
          //  navController.setGraph(R.navigation.main);
            navController = Navigation.findNavController(activity, R.id.navHostFragmentCuratore);

            View navHostVisitatore = activity.findViewById(R.id.navHostFragmentVisitatore);
            navHostVisitatore.setVisibility(View.GONE);

            navigationView.inflateMenu(R.menu.navigation_curatore_menu);
        }else{
            //  utente = new Visitatore();
            navController = Navigation.findNavController(activity, R.id.navHostFragmentVisitatore);

            //nascondo la view del curatore altrimenti andrebbe in sovraimpressione a quella del Visitatore, perché hanno start-fragment differenti
            View navHostCuratore = activity.findViewById(R.id.navHostFragmentCuratore);
            navHostCuratore.setVisibility(View.GONE);
            navigationView.inflateMenu(R.menu.navigation_visitatore_menu);

        }

        if(b!=null){

            utente = new Utente();

            utente.setUid(b.getString("uid"));
            utente.setNome(b.getString("nome"));
            utente.setCognome(b.getString("cognome"));
            utente.setEmail(b.getString("email"));
            utente.setRuolo(b.getString("ruolo"));
            //Log.e("RUOLO UTENTE: ", ""+utente.getRuolo());


        }else{
            BasicMethod.alertDialog(activity, "C'è stato un errore nel caricare i tuoi dati, sarai riportato alla login", "Errore caricamento dati", "OK");
            Intent login= new Intent(activity, Login.class);
            activity.startActivity(login);

        }

        View headerView = navigationView.getHeaderView(0);

        TextView headerNome    = headerView.findViewById(R.id.headerNome);
        TextView headerCognome = headerView.findViewById(R.id.headerCognome);
        TextView headerEmail   = headerView.findViewById(R.id.headerEmail);
        CircleImageView headerFotoProfilo = headerView.findViewById(R.id.imageProfile);

        headerNome.setText(utente.getNome());
        headerCognome.setText(utente.getCognome());
        headerEmail.setText(utente.getEmail());
        /**
         * Leggo l'immagine del profilo utente e la inserisco nella foto del navigation drawer
         * */
        letturaImmagineDB(headerFotoProfilo,activity);

    }

    /***
     *
     * @param headerCircleImageView: Immagine da impostare nel Drawer Laterale
     */
    public static void letturaImmagineDB(CircleImageView headerCircleImageView, Activity activity){

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
                Glide.with(activity)
                        .load(downloadUrl)
                        .into(headerCircleImageView);
            }
        });

    }

    /** FINE CREAZIONE DELLA NAVIGATION DRAWER BAR -----------------------------------------------------------------------------*/

    /***
     * Ottiene le info dell'utente, valorizzate in fase di Login
     *
     * @return utente: E' l'utente valorizzato in fase di Login
     */
    public static Utente getUtente() {
        return utente;
    }


    public static String toLower(String phrase){

        StringBuilder phraseLower = new StringBuilder(phrase);

        for(int i=0; i<phraseLower.length(); i++){
            phraseLower.setCharAt(i,Character.toLowerCase(phraseLower.charAt(i)));
        }

        return phraseLower.toString();
    }


    /***
     * Controlla se una stringa è formata solo da cifre
     * @param costoIngresso
     * @return boolean: true --> costoIngresso è valido
     *                  false --> costoIngresso non è valido
     */
    public static boolean stringIsInteger(String costoIngresso) {
        StringBuilder costoBuilder = new StringBuilder(costoIngresso);

        for (int i=0; i<costoBuilder.length(); i++) {
            if(costoBuilder.charAt(i) == '0' || costoBuilder.charAt(i) == '1' || costoBuilder.charAt(i) == '2'||
                    costoBuilder.charAt(i) == '3' || costoBuilder.charAt(i) == '4' ||costoBuilder.charAt(i) == '5' ||
                    costoBuilder.charAt(i) == '6' || costoBuilder.charAt(i) == '7' ||costoBuilder.charAt(i) == '8' ||costoBuilder.charAt(i) == '9') {

            }else {return false;}
        }
        return true;
    }

    /***
     * Controlla che un nome sia valido
     * @param name
     * @return boolean: true --> name è valido
     *                  false --> name non è valido
     */
    public static boolean checkIfNameIsAcceptable (String name) {
        StringBuilder nameBuilder = new StringBuilder(name);
        if(nameBuilder.charAt(0) == ' ') {
            return false;
        }
        for(int i=0; i<nameBuilder.length(); i++) {
            if(!isLetter(nameBuilder.charAt(i))) {
                if(i<nameBuilder.length()-1 && nameBuilder.charAt(i) == ' ' && isLetter(nameBuilder.charAt(i+1)) && isLetter(nameBuilder.charAt(i-1)) ) {

                }else {
                    return false;
                }

            }
        }

        return true;
    }

    /***
     * Controlla che un indirizzo sia valido
     * @param address
     * @return boolean: true --> address è valido
     *                  false --> address non è valido
     */
    public static boolean checkIfAddressIsAcceptable (String address) {
        StringBuilder addressBuilder = new StringBuilder(address);
        char carattere=' ';
        if(!isLetter(addressBuilder.charAt(0)) || addressBuilder.charAt(0) == ' ') {
            return false;
        }

        for(int i=1; i<addressBuilder.length(); i++) {
            if(!isLetter(addressBuilder.charAt(i))) {
                if(stringIsInteger(String.valueOf(addressBuilder.charAt(i)))) {
                    int j=i-1;

                    while( stringIsInteger(String.valueOf(addressBuilder.charAt(j)))) {
                        if(!stringIsInteger(String.valueOf(addressBuilder.charAt(j-1)))) {
                            carattere = addressBuilder.charAt(j-1);
                        }

                        j--;
                    }
                    if(carattere != ' ') {
                        return false;
                    }
                }else {
                    if(addressBuilder.charAt(i) != ' ') {
                        return false;
                    }

                }
            }
        }

        return true;
    }


    public static boolean isLetter (char lettera) {
        StringBuilder letteraBuilder = new StringBuilder();
        letteraBuilder.append(toLower(String.valueOf(lettera)));

        return letteraBuilder.charAt(0) == 'a' || letteraBuilder.charAt(0) == 'b' || letteraBuilder.charAt(0) == 'c' || letteraBuilder.charAt(0) == 'd'
                || letteraBuilder.charAt(0) == 'e' || letteraBuilder.charAt(0) == 'f' || letteraBuilder.charAt(0) == 'g' || letteraBuilder.charAt(0) == 'h'
                || letteraBuilder.charAt(0) == 'i' || letteraBuilder.charAt(0) == 'j' || letteraBuilder.charAt(0) == 'k' || letteraBuilder.charAt(0) == 'l'
                || letteraBuilder.charAt(0) == 'm' || letteraBuilder.charAt(0) == 'n' || letteraBuilder.charAt(0) == 'o' || letteraBuilder.charAt(0) == 'p'
                || letteraBuilder.charAt(0) == 'q' || letteraBuilder.charAt(0) == 'r' || letteraBuilder.charAt(0) == 's' || letteraBuilder.charAt(0) == 't'
                || letteraBuilder.charAt(0) == 'u' || letteraBuilder.charAt(0) == 'v' || letteraBuilder.charAt(0) == 'w' || letteraBuilder.charAt(0) == 'x'
                || letteraBuilder.charAt(0) == 'y' || letteraBuilder.charAt(0) == 'z';
    }

    /***
     * Rende grande ogni carattere inziale di ogni parola all'interno di una frase
     * @param phrase
     * @return phraseBuilder.toString()
     */
    public static String setUpperPhrase(String phrase) {
        StringBuilder phraseBuilder = new StringBuilder(phrase);

        for(int i=0; i<phraseBuilder.length(); i++) {
            if(i==0) {
                phraseBuilder.setCharAt(i, Character.toUpperCase(phraseBuilder.charAt(i)));
            }

            if (i>0 && phraseBuilder.charAt(i-1) == ' ') {
                phraseBuilder.setCharAt(i, Character.toUpperCase(phraseBuilder.charAt(i)));
            }
        }
        return phraseBuilder.toString();
    }
        public static void apriCalendario (Activity activity, EditText dataNascita){

            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int anno, int mese,
                                      int giorno) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, anno);
                    myCalendar.set(Calendar.MONTH, mese);
                    myCalendar.set(Calendar.DAY_OF_MONTH, giorno);
                    updateEditTextDataNascita(myCalendar, dataNascita);
                }

            };

            new DatePickerDialog(activity, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

        private static void updateEditTextDataNascita (Calendar myCalendar, EditText dataNascita){
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALIAN);

            dataNascita.setText(sdf.format(myCalendar.getTime()));


        }

    }

