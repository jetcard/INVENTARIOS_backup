package com.inventarios.handler.responsables;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.responsables.services.DeleteResponsableAbstractHandler;
import org.jooq.impl.DSL;

public class DeleteResponsableHandler extends DeleteResponsableAbstractHandler {

  protected void delete(long id) {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(RESPONSABLE_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
