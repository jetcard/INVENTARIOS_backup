package com.inventarios.handler.atributo.response;

import com.inventarios.model.Atributo;

import java.util.List;

public class AtributoResponse {
    private List<Atributo> listaatributos;
    public List<Atributo> getListaatributos() {
        return listaatributos;
    }
    public void setListaatributos(List<Atributo> listaatributos) {
        this.listaatributos = listaatributos;
    }
}
