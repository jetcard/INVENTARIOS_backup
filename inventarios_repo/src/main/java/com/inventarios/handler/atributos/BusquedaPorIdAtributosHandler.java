package com.inventarios.handler.atributos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.BusquedaPorIdAtributosAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdAtributosHandler extends BusquedaPorIdAtributosAbstractHandler {
  protected Result<Record> busquedaPorNombreAtributos(String filter) {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ATRIBUTOS_TABLE)
            .where(ATRIBUTOS_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

}