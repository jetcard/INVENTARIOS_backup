package com.inventarios.handler.tipos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.services.CreateTipoAbstractHandler;
import org.jooq.impl.DSL;

public class CreateTipoHandler extends CreateTipoAbstractHandler {
  protected void save(String nombretipo, String descriptipo) {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(TIPO_TABLE)
      .set(DSL.field("nombretipo"), nombretipo)
      .set(DSL.field("descriptipo"), descriptipo)
      .execute();
  }

}
