package com.uniba.capitool.fragments.fragmentVisualizzaZoneSito;

import android.net.Uri;

public class ItemOperaZona {
    String id;
    String titolo;
    Uri foto;

    public ItemOperaZona(String id, String titolo) {
        this.id = id;
        this.titolo = titolo;
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
