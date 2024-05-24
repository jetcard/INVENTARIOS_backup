package com.inventarios.handler.comunes;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.comunes.services.UpdateComunAbstractHandler;
import org.jooq.impl.DSL;

public class UpdateComunHandler extends UpdateComunAbstractHandler {
  protected void update(Long id, String descripcomun, String descripcortacomun) {
    var dsl = RDSConexion.getDSL();
    //DSLContext dsl = DependencyFactory.getDSL();
    dsl.update(COMUN_TABLE)
      .set(DSL.field("descripcomun"), descripcomun)
      .set(DSL.field("descripcortacomun"), descripcortacomun)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
