package com.inventarios.handler.grupos.response;

import com.inventarios.model.Grupo;
import java.util.List;

public class GrupoResponse {
    private List<Grupo> listagrupos;

    public List<Grupo> getListagrupos() {
        return listagrupos;
    }

    public void setListagrupos(List<Grupo> listagrupos) {
        this.listagrupos = listagrupos;
    }
}
