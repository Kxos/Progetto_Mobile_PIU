package com.uniba.capitool.classes;

import android.media.Image;

public class Opera {


    private String id ;
    private String titolo ;
    private String descrizione ;
    private Image foto ;
    private Image qrCode ;

    private static int countId = 0 ;

    public Opera(){} ;

    public Opera (String titolo, String descrizione, Image foto){
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

    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }

    public Image getQrCode() {
        return qrCode;
    }

    public void setQrCode(Image qrCode) {
        this.qrCode = qrCode;
    }

}
