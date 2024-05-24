package com.inventarios.handler.grupos.response;

import com.inventarios.handler.ResponseRest;

public class GrupoResponseRest extends ResponseRest {
    private GrupoResponse grupoResponse = new GrupoResponse();

    public GrupoResponse getGrupoResponse() {
        return grupoResponse;
    }

    public void setGrupoResponse(GrupoResponse grupoResponse) {
        this.grupoResponse = grupoResponse;
    }
}
