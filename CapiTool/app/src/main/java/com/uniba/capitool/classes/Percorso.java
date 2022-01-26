package com.uniba.capitool.classes;

import  java.util.ArrayList;

public class Percorso {

    private String id;
    private String nome;
    private ArrayList<Opera> opereScelte;
    private String idSitoAssociato;
    private String nomeSitoAssociato;
    private String cittaSitoAssociato;
    private String idUtente;
    private String idUtenteSelezionatoPercorsoTraPreferiti;
    private String descrizione;
    private boolean pubblico;


    public Percorso(){
        this.idUtenteSelezionatoPercorsoTraPreferiti = "";
    }

    public Percorso(String id, String nome, ArrayList<Opera> opereScelte, String idSitoAssociato, String nomeSitoAssociato, String cittaSitoAssociato, String idUtente, String idUtenteSelezionatoPercorsoTraPreferiti, String descrizione, boolean pubblico) {
        this.id = id;
        this.nome = nome;
        this.opereScelte = opereScelte;
        this.idSitoAssociato = idSitoAssociato;
        this.nomeSitoAssociato = nomeSitoAssociato;
        this.cittaSitoAssociato = cittaSitoAssociato;
        this.idUtente = idUtente;
        this.idUtenteSelezionatoPercorsoTraPreferiti = "";
        this.descrizione = descrizione;
        this.pubblico = pubblico;
    }

    protected void aggiornaPercorso(){

    }

    protected void eliminaPercorso(){

    }

    protected boolean aggiungiZonaScelta(Opera zonaScelta){
        return opereScelte.add(zonaScelta);
    }

    protected boolean rimuoviZonaScelta(Zona zonaScelta){
        return opereScelte.remove(zonaScelta);
    }


    //METODI GETTER & SETTER
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Opera> getOpereScelte() {
        return opereScelte;
    }

    public void setOpereScelte(ArrayList<Opera> opereScelte) {
        this.opereScelte = opereScelte;
    }

    public String getIdSitoAssociato() {
        return idSitoAssociato;
    }

    public void setIdSitoAssociato(String idSitoAssociato) {
        this.idSitoAssociato = idSitoAssociato;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean isPubblico() {
        return pubblico;
    }

    public void setPubblico(boolean pubblico) {
        this.pubblico = pubblico;
    }

    public String getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(String idUtente) {
        this.idUtente = idUtente;
    }

    public String getNomeSitoAssociato() {
        return nomeSitoAssociato;
    }

    public void setNomeSitoAssociato(String nomeSitoAssociato) {
        this.nomeSitoAssociato = nomeSitoAssociato;
    }

    public String getCittaSitoAssociato() {
        return cittaSitoAssociato;
    }

    public void setCittaSitoAssociato(String cittaSitoAssociato) {
        this.cittaSitoAssociato = cittaSitoAssociato;
    }

    public String getIdUtenteSelezionatoPercorsoTraPreferiti() {
        return idUtenteSelezionatoPercorsoTraPreferiti;
    }

    public void setIdUtenteSelezionatoPercorsoTraPreferiti(String idUtenteSelezionatoPercorsoTraPreferiti) {
        this.idUtenteSelezionatoPercorsoTraPreferiti = idUtenteSelezionatoPercorsoTraPreferiti;
    }
}
