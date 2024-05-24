package com.inventarios.handler.atributos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.CreateAtributosAbstractHandler;
import com.inventarios.model.Atributos;
import org.jooq.impl.DSL;

public class CreateAtributosHandler extends CreateAtributosAbstractHandler {
  @Override
  protected void save(Atributos atributos, Long grupoID) {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(ATRIBUTOS_TABLE)
            //.set(DSL.field("descripatributo"), atributos.getDescripatributo())
            .set(DSL.field("nombreatributo"), atributos.getNombreatributo())
            .set(DSL.field("grupoId"), grupoID)
            .execute();

  }

}
