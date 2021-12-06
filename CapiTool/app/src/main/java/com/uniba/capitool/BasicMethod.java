package com.uniba.capitool;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

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
}
