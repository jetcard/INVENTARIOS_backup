package com.inventarios.handler.comunes;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.comunes.services.ReadComunAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;
public class ReadComunHandler extends ReadComunAbstractHandler {
  protected Result<Record> read() {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(COMUN_TABLE).fetch();
  }

}