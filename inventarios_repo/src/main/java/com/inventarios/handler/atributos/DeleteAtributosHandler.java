package com.inventarios.handler.atributos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.DeleteAtributosAbstractHandler;
import org.jooq.impl.DSL;

public class DeleteAtributosHandler extends DeleteAtributosAbstractHandler {
  protected void delete(long id) {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(ATRIBUTOS_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
