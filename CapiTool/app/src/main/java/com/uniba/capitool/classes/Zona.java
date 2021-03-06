package com.uniba.capitool.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Zona implements Serializable {


    private String id ;
    private String nome ;
    private String posizione;
    private List<Opera> Opere;


    public Zona(){} ;

    public Zona (String nome){
        this.nome = nome ;
        Opere = new ArrayList<>() ;
    }


    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Opera> getOpere() {
        return Opere;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setOpere(ArrayList<Opera> opere) {
        this.Opere = opere;
    }

    protected boolean aggiungiOpera(Opera opera) {
        return Opere.add(opera);
    }

    protected boolean rimuoviOpera(Opera opera){
        return Opere.remove(opera);
    }
}
