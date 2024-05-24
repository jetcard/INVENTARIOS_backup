package com.inventarios.handler.grupos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.grupos.services.CreateGrupoAbstractHandler;
import org.jooq.impl.DSL;

public class CreateGrupoHandler extends CreateGrupoAbstractHandler {
  protected void save(String nombregrupo, String descripgrupo) {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(GRUPO_TABLE)
      .set(DSL.field("nombregrupo"), nombregrupo)
      .set(DSL.field("descripgrupo"), descripgrupo)
      .execute();
  }

}
