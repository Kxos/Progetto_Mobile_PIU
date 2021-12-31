package com.uniba.capitool.fragments.fragmentVisualizzaZoneSito;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class ItemOperaZona implements Serializable {
    String idOpera;
    String titolo;
    String descrizione;
    String idZona;  //salvo l'id della zona per avere un riferimento quando poi clicco nella recycler view delle opere, altrimenti ho la posizione dell'opera
                    // ma non la posizione della zona nella recycler view "Main"

    Drawable foto;

    public ItemOperaZona(String id, String titolo, String descrizione, String idZona) {
        this.idOpera = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.idZona=idZona;
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getIdOpera() {
        return idOpera;
    }

    public void setIdOpera(String idOpera) {
        this.idOpera = idOpera;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Drawable getFoto() {
        return foto;
    }

    public void setFoto(Drawable foto) {
        this.foto = foto;
    }
}
