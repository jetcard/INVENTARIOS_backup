package com.inventarios.handler.articulos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.ReadArticuloAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;
public class ReadArticuloHandler extends ReadArticuloAbstractHandler {

  protected Result<Record> read() {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(ARTICULO_TABLE).fetch();
  }

}