package com.inventarios.handler.tipos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.services.UpdateTipoAbstractHandler;
import com.inventarios.model.Tipo;
import org.jooq.impl.DSL;
public class UpdateTipoHandler extends UpdateTipoAbstractHandler {

  protected void update(Tipo tipo, Long id) {
    var dsl = RDSConexion.getDSL();
    dsl.update(TIPO_TABLE)
      .set(DSL.field("nombretipo"), tipo.getNombretipo())
      .set(DSL.field("descriptipo"), tipo.getDescriptipo())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
