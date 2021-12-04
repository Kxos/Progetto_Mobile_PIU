package com.uniba.capitool.classes;

import java.util.ArrayList;

public class Visitatore {
    private String dataNascita;      //non usare spazi nei nomi delle variabili nel db e le variabili in java devonbo avere lo stesso nome degli attributi!

    private String ruolo;
    private String cognome;

    private String psw;
    private String nome;

    private String email;

    private String username;

    private ArrayList<Percorso> percorsi;

    public Visitatore() {
    }

    public Visitatore(String dataNascita, String ruolo, String cognome, String psw, String nome, String email, String username) {
        this.dataNascita = dataNascita;
        this.ruolo = ruolo;
        this.cognome = cognome;
        this.psw = psw;
        this.nome = nome;
        this.email = email;
        this.username = username;
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

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //non mettere questo motodo pubblico, altrimenti nel db andra a scrivere questa stringa in un attributo
    protected String getAll(){
        String risultato=""+dataNascita+","+username+","+ruolo+","+email+","+nome+","+cognome+","+psw;
        return risultato;
    }

    protected boolean aggiungiPercorso(Percorso percorso){
        return percorsi.add(Percorso);
    }

    protected boolean rimuoviPercorso(Percorso percorso){
        return percorsi.remove(Percorso);
    }

    protected void aggiornaProfilo(){

    }

    protected void eliminaVisitatore(){

    }

}
