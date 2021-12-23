package com.uniba.capitool.classes;

import android.media.Image;
import android.net.Uri;

public class Opera {


    private String id ;
    private String titolo ;
    private String descrizione ;
    private Uri foto ;
    private Image qrCode ;

    private static int countId = 0 ;

    public Opera(){} ;

    public Opera (String titolo, String descrizione, Uri foto){
        this.titolo = titolo;
        this.descrizione = descrizione ;
        this.foto = foto ;
        this.qrCode = null ;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

    public Image getQrCode() {
        return qrCode;
    }

    public void setQrCode(Image qrCode) {
        this.qrCode = qrCode;
    }

}
