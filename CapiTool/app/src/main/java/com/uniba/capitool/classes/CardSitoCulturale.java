package com.uniba.capitool.classes;

import android.media.Image;

public class CardSitoCulturale {

    private Image foto;
    private String nome;
    private String descrizione;
    private String citta;


    public CardSitoCulturale() {
    }

    public CardSitoCulturale(Image foto, String nome, String descrizione, String citta) {
        this.foto = foto;
        this.nome = nome;
        this.descrizione = descrizione;
        this.citta = citta;
    }

    public Image getFoto() {
        return foto;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getCitta() {
        return citta;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }
}
