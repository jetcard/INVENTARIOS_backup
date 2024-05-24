package com.inventarios.handler.tipos.response;

import com.inventarios.model.Tipo;
import java.util.List;

public class TipoResponse {
    private List<Tipo> listatipos;

    public List<Tipo> getListatipos() {
        return listatipos;
    }

    public void setListatipos(List<Tipo> listatipos) {
        this.listatipos = listatipos;
    }
}
