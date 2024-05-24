package com.inventarios.handler.articulos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.BusquedaPorIdArticuloAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdArticuloHandler extends BusquedaPorIdArticuloAbstractHandler {
  protected Result<Record> busquedaPorNombreArticulo(String filter) {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ARTICULO_TABLE)
            .where(ARTICULO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

}