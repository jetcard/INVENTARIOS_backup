package com.inventarios.handler.activos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.services.ReadActivoAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class ReadActivoHandler extends ReadActivoAbstractHandler {
  protected Result<Record> read() {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(ACTIVO_TABLE).fetch();
  }

}