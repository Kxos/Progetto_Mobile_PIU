package com.uniba.capitool.classes;

import android.net.Uri;

public class CardOpera {

    private String id ;
    private String titolo ;
    private Uri foto ;

    public CardOpera() {
    }

    public CardOpera(String id, String titolo, Uri foto) {
        this.id = id;
        this.titolo = titolo;
        this.foto = foto;
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
