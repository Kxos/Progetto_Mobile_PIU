package com.uniba.capitool.fragments.fragmentVisualizzaZoneSito;

import android.net.Uri;

public class ItemOperaZona {
    String id;
    String titolo;
    String descrizione;
    String idZona;
    Uri foto;

    public ItemOperaZona(String id, String titolo, String descrizione, String idZona) {
        this.id = id;
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
}
