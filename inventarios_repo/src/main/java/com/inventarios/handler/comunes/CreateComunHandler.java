package com.inventarios.handler.comunes;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.comunes.services.CreateComunAbstractHandler;
import com.inventarios.model.Comun;
import org.jooq.impl.DSL;

public class CreateComunHandler extends CreateComunAbstractHandler {
  @Override
  protected void save(Comun comun, Long grupoID) {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(COMUN_TABLE)
            .set(DSL.field("descripcomun"), comun.getDescripcomun())
            .set(DSL.field("descripcortacomun"), comun.getDescripcortacomun())
            .set(DSL.field("grupoId"), grupoID)
            .execute();

  }

}
