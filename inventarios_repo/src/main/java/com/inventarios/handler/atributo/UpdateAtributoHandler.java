package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.UpdateAtributoAbstractHandler;
import com.inventarios.model.Articulo;
import com.inventarios.model.Responsable;
import org.jooq.impl.DSL;

public class UpdateAtributoHandler extends UpdateAtributoAbstractHandler {
  protected void update(Long id, Responsable responsable, Articulo articulo) {
    var dsl = RDSConexion.getDSL();
    dsl.update(ATRIBUTO_TABLE)
      .set(DSL.field("responsable"), responsable)
      .set(DSL.field("articulo"), articulo)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
