package com.uniba.capitool.classes;

import android.media.Image;
import android.net.Uri;

public class Opera {


    private String id ;
    private String titolo ;
    private String descrizione ;
    private Image qrCode;
    private String idZona;
    private Uri foto;
    private String idFoto;

    private static int countId = 0 ;

    public Opera(){} ;

    public Opera(String id, String titolo, String descrizione, Image qrCode, String idZona, Uri foto, String fotoOpera) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.qrCode = qrCode;
        this.idZona = idZona;
        this.foto = foto;
        this.idFoto = fotoOpera;
    }

    public Opera(String id, String titolo, String descrizione, String idZona, String idFoto) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.idZona = idZona;
        this.idFoto=idFoto;
    }

    public Opera(String id, String titolo, String descrizione, String idZona) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.idZona = idZona;
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public static int getCountId() {
        return countId;
    }

    public static void setCountId(int countId) {
        Opera.countId = countId;
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

    public Image getQrCode() {
        return qrCode;
    }

    public void setQrCode(Image qrCode) {
        this.qrCode = qrCode;
    }

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

    public String getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(String idFoto) {
        this.idFoto = idFoto;
    }

}
