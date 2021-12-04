package com.uniba.capitool.classes;

import android.media.Image;

import java.util.ArrayList;
import java.util.Iterator;

public class Zona {




    private int id ;
    private String nome ;
    private ArrayList<Opera> opere ;

    private static int countId = 0;


    public Zona (String nome){
        this.nome = nome ;
        countId ++ ;
        this.id = countId ;
        opere = new ArrayList<Opera>() ;
    }



    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Opera> getOpere() {
        return opere;
    }

    public void setId(int id) {
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
        return opere.remove(opera) ;
    }
}
