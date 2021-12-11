package com.uniba.capitool.classes;

import android.media.Image;

import java.util.ArrayList;
import java.util.Date;

public class SitoCulturale {

    private int id;
    private String nome;
    private String indirizzo;
    private Date orarioApertura;
    private Date orarioChiusura;
    private float costoBiglietto;
    private String citta;
    private Image foto;
    private ArrayList<Zona> zone;

    public SitoCulturale(){

    }

    public SitoCulturale(int id, String nome, String indirizzo, Date orarioApertura, Date orarioChiusura, float costoBiglietto, String citta, Image foto, ArrayList<Zona> zone) {
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
        this.costoBiglietto = costoBiglietto;
        this.citta = citta;
        this.foto = foto;
        this.zone = zone;
    }

    protected boolean aggiungiZona(Zona zona) {
        return zone.add(zona);
    }

    protected boolean rimuoviZona(Zona zona) {
        return zone.remove(zona);
    }

    // Getter - Setter
    //-----------------------------------------------------------------------------------------
    public void setId(int id) { this.id = id; }

    public void setNome(String nome) { this.nome = nome; }

    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public void setOrarioApertura(Date orarioApertura) { this.orarioApertura = orarioApertura; }

    public void setOrarioChiusura(Date orarioChiusura) { this.orarioChiusura = orarioChiusura; }

    public void setCostoBiglietto(float costoBiglietto) { this.costoBiglietto = costoBiglietto; }

    public void setCitta(String citta) { this.citta = citta; }

    public void setFoto(Image foto) { this.foto = foto; }

    public int getId() { return id; }

    public String getNome() { return nome; }

    public String getIndirizzo() { return indirizzo; }

    public Date getOrarioApertura() { return orarioApertura; }

    public Date getOrarioChiusura() { return orarioChiusura; }

    public float getCostoBiglietto() { return costoBiglietto; }

    public String getCitta() { return citta; }

    public Image getFoto() { return foto; }

}
