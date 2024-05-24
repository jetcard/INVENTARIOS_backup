package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.DeleteAtributoAbstractHandler;
import org.jooq.impl.DSL;

public class DeleteAtributoHandler extends DeleteAtributoAbstractHandler {
  protected void delete(long id) {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(ATRIBUTO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
