package com.inventarios.handler.grupos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.grupos.services.DeleteGrupoAbstractHandler;
import org.jooq.impl.DSL;

public class DeleteGrupoHandler extends DeleteGrupoAbstractHandler {
  protected void delete(long id) {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(GRUPO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
