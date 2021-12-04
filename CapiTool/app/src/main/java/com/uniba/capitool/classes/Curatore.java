package com.uniba.capitool.classes;

public class Curatore extends Visitatore {
    private Sito sito;


    public Curatore(){

    }

    public Curatore(Sito sito) {
        this.sito = sito;
    }

    public Curatore(String dataNascita, String ruolo, String cognome, String psw, String nome, String email, String username, Sito sito) {
        super(dataNascita, ruolo, cognome, psw, nome, email, username);
        this.sito = sito;
    }

    public Sito getSito() {
        return sito;
    }

    public void setSito(Sito sito) {
        this.sito = sito;
    }

    protected void aggiornaSito(){

    }

    protected void eliminaSito(){

    }

    protected void delegaSito(){

    }
}
