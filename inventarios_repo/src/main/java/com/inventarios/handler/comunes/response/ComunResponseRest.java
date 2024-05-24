package com.inventarios.handler.comunes.response;

import com.inventarios.handler.ResponseRest;
public class ComunResponseRest extends ResponseRest {
    private ComunResponse comunResponse = new ComunResponse();
    public ComunResponse getComunResponse() {
        return comunResponse;
    }
    public void setComunResponse(ComunResponse comunResponse) {
        this.comunResponse = comunResponse;
    }
}
