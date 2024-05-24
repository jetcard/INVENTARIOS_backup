package com.inventarios.handler.atributos.response;

import com.inventarios.handler.ResponseRest;

public class AtributosResponseRest extends ResponseRest {
    private AtributosResponse atributosResponse = new AtributosResponse();
    public AtributosResponse getAtributosResponse() {
        return atributosResponse;
    }
    public void setAtributosResponse(AtributosResponse atributosResponse) {
        this.atributosResponse = atributosResponse;
    }
}
