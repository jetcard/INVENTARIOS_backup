package com.inventarios.handler.grupos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.grupos.services.UpdateGrupoAbstractHandler;
import com.inventarios.model.Grupo;
import org.jooq.impl.DSL;
public class UpdateGrupoHandler extends UpdateGrupoAbstractHandler {
  protected void update(Grupo grupo, Long id) {
    var dsl = RDSConexion.getDSL();
    dsl.update(GRUPO_TABLE)
      .set(DSL.field("nombregrupo"), grupo.getNombregrupo())
      .set(DSL.field("descripgrupo"), grupo.getDescripgrupo())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
