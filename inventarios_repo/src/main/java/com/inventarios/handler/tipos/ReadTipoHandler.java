package com.inventarios.handler.tipos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.services.ReadTipoAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;
public class ReadTipoHandler extends ReadTipoAbstractHandler {

  protected Result<Record> read() {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(TIPO_TABLE).fetch();
  }

}