package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.ReadAtributoAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class ReadAtributoHandler extends ReadAtributoAbstractHandler {
  protected Result<Record> read() {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(ATRIBUTO_TABLE).fetch();
  }
}