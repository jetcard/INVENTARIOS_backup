package com.inventarios.handler.responsables;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.responsables.services.CreateResponsableAbstractHandler;
import org.jooq.impl.DSL;

public class CreateResponsableHandler extends CreateResponsableAbstractHandler {
  protected void save(String arearesponsable, String nombresyapellidos) {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(RESPONSABLE_TABLE)
      .set(DSL.field("arearesponsable"), arearesponsable)
      .set(DSL.field("nombresyapellidos"), nombresyapellidos)
      .execute();
  }

}
