package com.uniba.capitool.classes;

import java.util.ArrayList;


public class Zona {


    private String id ;
    private String nome ;
    private ArrayList<Opera> opere;


    public Zona(){} ;

    public Zona (String nome){
        this.nome = nome ;
        opere = new ArrayList<Opera>() ;
    }



    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Opera> getOpere() {
        return opere;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setOpere(ArrayList<Opera> opere) {
        this.opere = opere;
    }

    protected boolean aggiungiOpera(Opera opera) {
        return opere.add(opera);
    }

    protected boolean rimuoviOpera(Opera opera){
        return opere.remove(opera);
    }
}
