package com.uniba.capitool.classes;

public class Guida extends Visitatore{
    private String idPatentino;

    public Guida(String idPatentino) {
        this.idPatentino = idPatentino;
    }

    public Guida(String dataNascita, String ruolo, String cognome, String psw, String nome, String email, String username, String idPatentino) {
        super(dataNascita, ruolo, cognome, psw, nome, email, username);
        this.idPatentino = idPatentino;
    }


    public Guida() {
    }

    public String getIdPatentino() {
        return idPatentino;
    }

    public void setIdPatentino(String idPatentino) {
        this.idPatentino = idPatentino;
    }
}
