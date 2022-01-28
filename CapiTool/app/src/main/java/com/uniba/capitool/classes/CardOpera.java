package com.uniba.capitool.classes;

import android.net.Uri;
import android.widget.CheckBox;

import java.io.Serializable;

public class CardOpera implements Serializable {

    private String id ;
    private String titolo ;
    private String descrizione;
    private Uri foto;
    private String idFoto;
    private CheckBox checkBox;
    private String idZona;

    public CardOpera(String id, String titolo, String descrizione, Uri foto, String fotoOpera, CheckBox checkBox, String idZona) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.foto = foto;
        this.idFoto = fotoOpera;
        this.checkBox = checkBox;
        this.idZona = idZona;
    }

    public CardOpera() {
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

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public void setCheckBoxCheckedStatus(boolean status){ this.checkBox.setChecked(status); }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public String getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(String idFoto) {
        this.idFoto = idFoto;
    }

}
