package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.BusquedaPorIdAtributoAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdAtributoHandler extends BusquedaPorIdAtributoAbstractHandler {
  protected Result<Record> busquedaPorNombreAtributo(String filter) {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ATRIBUTO_TABLE)
            .where(ATRIBUTO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }
}