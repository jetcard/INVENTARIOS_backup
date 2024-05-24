package com.inventarios.handler.articulos.response;

import com.inventarios.model.Articulo;
import java.util.List;

public class ArticuloResponse {
    private List<Articulo> listaarticulos;

    public List<Articulo> getListaarticulos() {
        return listaarticulos;
    }

    public void setListaarticulos(List<Articulo> listaarticulos) {
        this.listaarticulos = listaarticulos;
    }
}
