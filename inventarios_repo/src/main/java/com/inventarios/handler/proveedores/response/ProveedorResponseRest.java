package com.inventarios.handler.proveedores.response;

import com.inventarios.handler.ResponseRest;

public class ProveedorResponseRest extends ResponseRest {
    private ProveedorResponse proveedorResponse = new ProveedorResponse();

    public ProveedorResponse getProveedorResponse() {
        return proveedorResponse;
    }

    public void setProveedorResponse(ProveedorResponse proveedorResponse) {
        this.proveedorResponse = proveedorResponse;
    }
}
