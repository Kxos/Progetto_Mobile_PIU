package com.uniba.capitool.classes;

import android.net.Uri;
import android.widget.CheckBox;

import java.io.Serializable;

public class CardOpera implements Serializable {

    private String id ;
    private String titolo ;
    private Uri foto;
    private CheckBox checkBox;

    public CardOpera(String id, String titolo, Uri foto, CheckBox checkBox) {
        this.id = id;
        this.titolo = titolo;
        this.foto = foto;
        this.checkBox = checkBox;
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

}
