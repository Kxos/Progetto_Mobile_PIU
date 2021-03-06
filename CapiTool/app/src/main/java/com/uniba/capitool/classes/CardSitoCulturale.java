package com.uniba.capitool.classes;

import android.net.Uri;

public class CardSitoCulturale {

    private Uri foto;
    private String id;
    private String nome;
    private String indirizzo;
    private String orarioApertura;
    private String orarioChiusura;
    private String costoBiglietto;
    private String citta;


    public CardSitoCulturale() {
    }

    public CardSitoCulturale(Uri foto, String id, String nome, String indirizzo, String orarioApertura, String orarioChiusura, String costoBiglietto, String citta) {
        this.foto = foto;
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
        this.costoBiglietto = costoBiglietto;
        this.citta = citta;
    }

    public Uri getFoto() {
        return foto;
    }

    public String getId() { return id; }

    public String getNome() {
        return nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getCitta() { return citta; }

    public String getCostoBiglietto() { return costoBiglietto; }

    public String getOrarioChiusura() { return orarioChiusura; }

    public String getOrarioApertura() { return orarioApertura; }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIndirizzo(String descrizione) {
        this.indirizzo = descrizione;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public void setId(String id) { this.id = id; }

    public void setOrarioApertura(String orarioApertura) { this.orarioApertura = orarioApertura; }

    public void setOrarioChiusura(String orarioChiusura) { this.orarioChiusura = orarioChiusura; }

    public void setCostoBiglietto(String costoBiglietto) { this.costoBiglietto = costoBiglietto; }
}
