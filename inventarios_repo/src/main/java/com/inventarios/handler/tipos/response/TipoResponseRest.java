package com.inventarios.handler.tipos.response;

import com.inventarios.handler.ResponseRest;

public class TipoResponseRest extends ResponseRest {
    private TipoResponse tipoResponse = new TipoResponse();

    public TipoResponse getTipoResponse() {
        return tipoResponse;
    }

    public void setTipoResponse(TipoResponse tipoResponse) {
        this.tipoResponse = tipoResponse;
    }
}
