package com.uniba.capitool.fragments.fragmentVisualizzaZoneSito;

import java.io.Serializable;
import java.util.List;

public class AllZona implements Serializable {
    String nomeZona;
    String id;
    String posizione;
    List<ItemOperaZona> listaOpereZona;

    public AllZona(String id, String nomeZona, List<ItemOperaZona> listaOpereZona) {
        this.id=id;
        this.nomeZona = nomeZona;
        this.listaOpereZona=listaOpereZona;
    }

    public AllZona(String id, String nomeZona, List<ItemOperaZona> listaOpereZona, String posizione) {
        this.id=id;
        this.nomeZona = nomeZona;
        this.listaOpereZona=listaOpereZona;
        this.posizione=posizione;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public String getNomeZona() {
        return nomeZona;
    }

    public void setNomeZona(String nomeZona) {
        this.nomeZona = nomeZona;
    }

    public List<ItemOperaZona> getListaOpereZona() {
        return listaOpereZona;
    }

    public void setListaOpereZona(List<ItemOperaZona> listaOpereZona) {
        this.listaOpereZona = listaOpereZona;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
