package com.uniba.capitool.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.classes.Visitatore;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasicMethod extends AppCompatActivity {

    DrawerLayout drawerLayout;
    static Utente utente;

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

        setNavLateralMenuOnUserRole(navigationView,activity);

        NavController navController = Navigation.findNavController(activity, R.id.navHostFragment);
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
    public void setNavLateralMenuOnUserRole(NavigationView navigationView, Activity activity){
        navigationView.getMenu().clear();

        Bundle b = activity.getIntent().getExtras();

        if(BasicMethod.isCuratore(b.getString("ruolo"))){
            // utente = new Curatore();
            navigationView.inflateMenu(R.menu.navigation_curatore_menu);
        }else{
            //  utente = new Visitatore();
            navigationView.inflateMenu(R.menu.navigation_visitatore_menu);
        }

        if(b!=null){

            utente = new Utente();

            utente.setUid(b.getString("uid"));
            utente.setNome(b.getString("nome"));
            utente.setCognome(b.getString("cognome"));
            utente.setEmail(b.getString("email"));
            utente.setRuolo(b.getString("ruolo"));
        }else{
            BasicMethod.alertDialog(this, "C'è stato un errore nel caricare i tuoi dati, sarai riportato alla login", "Errore caricamento dati", "OK");
            Intent login= new Intent(activity, Login.class);
            this.startActivity(login);

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
    public void letturaImmagineDB(CircleImageView headerCircleImageView, Activity activity){

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

    /***
     * Ottiene le info dell'utente, valorizzte in fase di Login
     *
     * @return utente: E' l'utente valorizzato in fase di Login
     */
    public static Utente getUtente() {
        return utente;
    }

    public String toLower(String phrase){
        StringBuilder phraseLower = new StringBuilder(phrase);

        for(int i=0; i<phraseLower.length(); i++){
            phraseLower.setCharAt(i,Character.toLowerCase(phraseLower.charAt(i)));
        }

        return phraseLower.toString();
    }

}
