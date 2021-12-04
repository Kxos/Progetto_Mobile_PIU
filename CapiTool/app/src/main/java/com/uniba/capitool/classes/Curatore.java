package com.uniba.capitool.classes;

public class Curatore extends Visitatore {
    private Sito_Culturale sito;


    public Curatore(){

    }

    public Curatore(Sito_Culturale sito) {
        this.sito = sito;
    }

    public Curatore(String dataNascita, String ruolo, String cognome, String psw, String nome, String email, String username, Sito_Culturale sito) {
        super(dataNascita, ruolo, cognome, psw, nome, email, username);
        this.sito = sito;
    }

    public Sito_Culturale getSito() {
        return sito;
    }

    public void setSito(Sito_Culturale sito) {
        this.sito = sito;
    }

    protected void aggiornaSito(){

    }

    protected void eliminaSito(){

    }

    protected void delegaSito(){

    }
}
