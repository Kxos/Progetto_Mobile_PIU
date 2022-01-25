package com.uniba.capitool.classes;

public class CardPercorso {

    private String id;
    private String nome;
    private String idSitoAssociato;
    private String nomeSitoAssociato;
    private String cittaSitoAssociato;
    private String descrizione;
    private boolean pubblico;

    public CardPercorso(String id, String nome, String idSitoAssociato, String nomeSitoAssociato, String cittaSitoAssociato, String descrizione, boolean pubblico) {
        this.id = id;
        this.nome = nome;
        this.idSitoAssociato = idSitoAssociato;
        this.nomeSitoAssociato = nomeSitoAssociato;
        this.cittaSitoAssociato = cittaSitoAssociato;
        this.descrizione = descrizione;
        this.pubblico = pubblico;
    }

    public CardPercorso() {
    }

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
}
