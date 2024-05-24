package com.inventarios.handler.grupos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.grupos.services.ReadGrupoAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;
public class ReadGrupoHandler extends ReadGrupoAbstractHandler {
  protected Result<Record> read() {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(GRUPO_TABLE).fetch();
  }

}