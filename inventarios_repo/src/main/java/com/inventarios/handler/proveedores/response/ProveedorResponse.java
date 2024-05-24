package com.inventarios.handler.proveedores.response;

import com.inventarios.model.Proveedor;
import java.util.List;

public class ProveedorResponse {
    private List<Proveedor> listaproveedores;

    public List<Proveedor> getListaproveedores() {
        return listaproveedores;
    }

    public void setListaproveedores(List<Proveedor> listaproveedores) {
        this.listaproveedores = listaproveedores;
    }
}
