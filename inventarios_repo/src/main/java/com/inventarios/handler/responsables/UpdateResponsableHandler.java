package com.inventarios.handler.responsables;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.responsables.services.UpdateResponsableAbstractHandler;
import com.inventarios.model.Responsable;
import org.jooq.impl.DSL;
public class UpdateResponsableHandler extends UpdateResponsableAbstractHandler {

  protected void update(Responsable responsable, Long id) {
    var dsl = RDSConexion.getDSL();
    dsl.update(RESPONSABLE_TABLE)
      .set(DSL.field("arearesponsable"), responsable.getArearesponsable())
      .set(DSL.field("nombresyapellidos"), responsable.getNombresyapellidos())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
