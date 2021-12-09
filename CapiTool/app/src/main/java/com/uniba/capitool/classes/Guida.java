package com.uniba.capitool.classes;

import java.util.ArrayList;

public class Guida extends Visitatore{
    private String idPatentino;

    public Guida(String idPatentino) {
        this.idPatentino = idPatentino;
    }

    public Guida(String uid, String email, String username, String nome, String cognome, String dataNascita, String ruolo, ArrayList<Percorso> percorsi, String idPatentino) {
        super(uid, email, username, nome, cognome, dataNascita, ruolo, percorsi);
        this.idPatentino = idPatentino;
    }

    public Guida(String email, String username, String nome, String cognome, String dataNascita, String ruolo, String idPatentino) {
        super(email, username, nome, cognome, dataNascita, ruolo);
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
