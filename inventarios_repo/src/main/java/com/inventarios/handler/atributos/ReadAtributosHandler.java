package com.inventarios.handler.atributos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.ReadAtributosAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class ReadAtributosHandler extends ReadAtributosAbstractHandler {
  protected Result<Record> read() {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(ATRIBUTOS_TABLE).fetch();
  }

}