package com.inventarios.handler.atributo.response;

import com.inventarios.handler.ResponseRest;
public class AtributoResponseRest extends ResponseRest {
    private AtributoResponse atributoResponse = new AtributoResponse();
    public AtributoResponse getAtributoResponse() {
        return atributoResponse;
    }
    public void setAtributoResponse(AtributoResponse atributoResponse) {
        this.atributoResponse = atributoResponse;
    }
}
