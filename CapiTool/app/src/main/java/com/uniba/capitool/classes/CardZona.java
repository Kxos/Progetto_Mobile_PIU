package com.uniba.capitool.classes;

import android.net.Uri;
import android.widget.CheckBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardZona implements Serializable {
    private String id ;
    private String nome ;
    private List<Opera> Opere;
    private CheckBox checkBox;
    private String idSito;

    public CardZona (String id,String nome,CheckBox checkBox, String idSito){
        this.nome = nome ;
        Opere = new ArrayList<>() ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Opera> getOpere() {
        return Opere;
    }

    public void setOpere(List<Opera> opere) {
        Opere = opere;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
    public void setCheckBoxCheckedStatus(boolean status){ this.checkBox.setChecked(status); }


    public String getIdSito() {
        return idSito;
    }

    public void setIdSito(String idSito) {
        this.idSito = idSito;
    }
}
