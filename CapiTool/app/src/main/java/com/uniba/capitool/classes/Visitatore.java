package com.uniba.capitool.classes;

import java.util.ArrayList;

public class Visitatore {

    //non usare spazi nei nomi delle variabili nel db e le variabili in java devonbo avere lo stesso nome degli attributi!
    private String uid;
    private String email;
    private String nome;
    private String cognome;
    private String dataNascita;
    private String ruolo;
    private ArrayList<Percorso> percorsi;

    public Visitatore() {
    }

    public Visitatore(String uid, String email, String nome, String cognome, String dataNascita, String ruolo, ArrayList<Percorso> percorsi) {
        this.uid = uid;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.ruolo = ruolo;
        this.percorsi = percorsi;
    }

    public Visitatore(String email, String nome, String cognome, String dataNascita, String ruolo) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.ruolo = ruolo;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<Percorso> getPercorsi() {
        return percorsi;
    }

    public void setPercorsi(ArrayList<Percorso> percorsi) {
        this.percorsi = percorsi;
    }

    protected boolean aggiungiPercorso(Percorso percorso){
        return percorsi.add(percorso);
    }

    protected boolean rimuoviPercorso(Percorso percorso){
        return percorsi.remove(percorso);
    }

    protected void aggiornaProfilo(){

    }

    protected void eliminaVisitatore(){

    }

}
