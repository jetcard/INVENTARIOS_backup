package com.inventarios.handler.responsables.response;

import com.inventarios.model.Responsable;
import java.util.List;

public class ResponsableResponse {
    private List<Responsable> listaresponsables;

    public List<Responsable> getListaresponsables() {
        return listaresponsables;
    }

    public void setListaresponsables(List<Responsable> listaresponsables) {
        this.listaresponsables = listaresponsables;
    }
}
