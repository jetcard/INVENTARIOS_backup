package com.inventarios.handler.activos.response;

import com.inventarios.handler.ResponseRest;

public class ActivoResponseRest extends ResponseRest {
    private ActivoResponse activoResponse = new ActivoResponse();
    public ActivoResponse getActivoResponse() {
        return activoResponse;
    }
    public void setActivoResponse(ActivoResponse activoResponse) {
        this.activoResponse = activoResponse;
    }
}
