package com.uniba.capitool.classes;

public class Curatore extends Visitatore {
    private SitoCulturale sito;


    public Curatore(){

    }

    public Curatore(SitoCulturale sito) {
        this.sito = sito;
    }

    public Curatore(int id, String dataNascita, String ruolo, String cognome, String psw, String nome, String email, String username, SitoCulturale sito) {
        super(id, dataNascita, ruolo, cognome, psw, nome, email, username);
        this.sito = sito;
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
