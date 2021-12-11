package com.uniba.capitool.classes;

import java.util.ArrayList;

public class Curatore extends Visitatore {
    private SitoCulturale sito;

    public Curatore(){

    }

    public Curatore(String uid, String email, String nome, String cognome, String dataNascita, String ruolo, ArrayList<Percorso> percorsi, SitoCulturale sito) {
        super(uid, email, nome, cognome, dataNascita, ruolo, percorsi);
        this.sito = sito;
    }

    public Curatore(String email, String nome, String cognome, String dataNascita, String ruolo) {
        super(email, nome, cognome, dataNascita, ruolo);
    }

    public SitoCulturale getSito() {
        return sito;
    }

    public void setSito(SitoCulturale sito) {
        this.sito = sito;
    }

    protected void aggiornaSito(){

    }

    protected void eliminaSito(){

    }

    protected void delegaSito(){

    }
}
