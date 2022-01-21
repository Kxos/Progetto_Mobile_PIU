package com.uniba.capitool.classes;

import  java.util.ArrayList;

public class Percorso {

    private int id;
    private String nome;
    private ArrayList<Zona> zoneScelte;
    private int idSitoAssociato;
    private String descrizione;
    private boolean pubblico;

    public Percorso(int id, String nome, ArrayList<Zona> zoneScelte, int idSitoAssociato, String descrizione, boolean pubblico) {
        this.id = id;
        this.nome = nome;
        this.zoneScelte = zoneScelte;
        this.idSitoAssociato = idSitoAssociato;
        this.descrizione = descrizione;
        this.pubblico = pubblico;
    }

    protected void aggiornaPercorso(){

    }

    protected void eliminaPercorso(){

    }

    protected boolean aggiungiZonaScelta(Zona zonaScelta){
        return zoneScelte.add(zonaScelta);
    }

    protected boolean rimuoviZonaScelta(Zona zonaScelta){
        return zoneScelte.remove(zonaScelta);
    }


    //METODI GETTER & SETTER
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Zona> getZoneScelte() {
        return zoneScelte;
    }

    public void setZoneScelte(ArrayList<Zona> zoneScelte) {
        this.zoneScelte = zoneScelte;
    }

    public int getIdSitoAssociato() {
        return idSitoAssociato;
    }

    public void setIdSitoAssociato(int idSitoAssociato) {
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


}
