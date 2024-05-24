package com.inventarios.handler.responsables;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.responsables.services.ReadResponsableAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;
public class ReadResponsableHandler extends ReadResponsableAbstractHandler {

  protected Result<Record> read() {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(RESPONSABLE_TABLE).fetch();
  }

}