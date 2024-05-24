package com.inventarios.handler.comunes;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.comunes.services.BusquedaPorIdComunAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdComunHandler extends BusquedaPorIdComunAbstractHandler {
  protected Result<Record> busquedaPorNombreComun(String filter) {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(COMUN_TABLE)
            .where(COMUN_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

}