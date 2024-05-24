package com.inventarios.handler.atributos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.UpdateAtributosAbstractHandler;
import org.jooq.impl.DSL;

public class UpdateAtributosHandler extends UpdateAtributosAbstractHandler {
  protected void update(Long id, String descripatributo, String nombreatributo) {
    var dsl = RDSConexion.getDSL();
    //DSLContext dsl = DependencyFactory.getDSL();
    dsl.update(ATRIBUTOS_TABLE)
      //.set(DSL.field("descripatributo"), descripatributo)
      .set(DSL.field("nombreatributo"), nombreatributo)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
