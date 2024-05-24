package com.inventarios.handler.comunes;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.comunes.services.DeleteComunAbstractHandler;
import org.jooq.impl.DSL;

public class DeleteComunHandler extends DeleteComunAbstractHandler {
  protected void delete(long id) {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(COMUN_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
