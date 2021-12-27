package com.uniba.capitool.classes;

import java.util.ArrayList;


public class Zona {


    private String id ;
    private String nome ;
    private ArrayList<Opera> Opere;


    public Zona(){} ;

    public Zona (String nome){
        this.nome = nome ;
        Opere = new ArrayList<Opera>() ;
    }



    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Opera> getOpere() {
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
