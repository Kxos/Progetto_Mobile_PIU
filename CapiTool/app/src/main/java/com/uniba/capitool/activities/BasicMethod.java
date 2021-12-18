package com.uniba.capitool.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.classes.Visitatore;

public class BasicMethod {

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


}
