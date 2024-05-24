package com.inventarios.handler.comunes.response;

import com.inventarios.model.Comun;

import java.util.List;
public class ComunResponse {
    private List<Comun> listacomuns;
    public List<Comun> getListacomuns() {
        return listacomuns;
    }
    public void setListacomuns(List<Comun> listacomuns) {
        this.listacomuns = listacomuns;
    }
}
