package com.uniba.capitool.classes;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class SitoCulturale implements Serializable {

    private String id;
    private String nome;
    private String indirizzo;
    private String orarioApertura;
    private String orarioChiusura;
    private String costoBiglietto;
    private String citta;
    private Image foto;
    private ArrayList<Zona> Zone = new ArrayList<>();
    private String uidCuratore;

    public SitoCulturale(){

    }

    public SitoCulturale(String id, String nome, String indirizzo, String orarioApertura, String orarioChiusura, String costoBiglietto, String citta, Image foto, ArrayList<Zona> zone, String uidCuratore) {

        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
        this.costoBiglietto = costoBiglietto;
        this.citta = citta;
        this.foto = foto;
        this.Zone = zone;
        this.uidCuratore = uidCuratore;
    }


    public SitoCulturale(String id, String nome, String indirizzo, String orarioApertura, String orarioChiusura, String costoBiglietto, String citta, String uidCuratore) {

        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
        this.costoBiglietto = costoBiglietto;
        this.citta = citta;

        this.uidCuratore = uidCuratore;
    }



    protected boolean aggiungiZona(Zona zona) {
        return Zone.add(zona);
    }

    protected boolean rimuoviZona(Zona zona) {
        return Zone.remove(zona);
    }

    // Getter - Setter
    //-----------------------------------------------------------------------------------------
    public void setId(String id) { this.id = id; }

    public void setNome(String nome) { this.nome = nome; }

    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public void setOrarioApertura(String orarioApertura) { this.orarioApertura = orarioApertura; }

    public void setOrarioChiusura(String orarioChiusura) { this.orarioChiusura = orarioChiusura; }

    public void setCostoBiglietto(String costoBiglietto) { this.costoBiglietto = costoBiglietto; }

    public void setCitta(String citta) { this.citta = citta; }

    public void setFoto(Image foto) { this.foto = foto; }

    public void setZone (ArrayList<Zona> zone){ this.Zone = zone ;}

    public void setUidCuratore(String uidCuratore){ this.uidCuratore = uidCuratore ;}



    public String getId() { return id; }

    public String getNome() { return nome; }

    public String getIndirizzo() { return indirizzo; }

    public String getOrarioApertura() { return orarioApertura; }

    public String getOrarioChiusura() { return orarioChiusura; }

    public String getCostoBiglietto() { return costoBiglietto; }

    public String getCitta() { return citta; }

    public Image getFoto() { return foto; }

    public ArrayList<Zona> getZone() {
        return Zone;
    }

    public String getUidCuratore(){return uidCuratore ;}


}
