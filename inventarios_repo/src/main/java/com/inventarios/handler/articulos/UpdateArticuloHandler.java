package com.inventarios.handler.articulos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.UpdateArticuloAbstractHandler;
import com.inventarios.model.Articulo;
import org.jooq.impl.DSL;
public class UpdateArticuloHandler extends UpdateArticuloAbstractHandler {

  protected void update(Articulo articulo, Long id) {
    var dsl = RDSConexion.getDSL();
    dsl.update(ARTICULO_TABLE)
      .set(DSL.field("nombrearticulo"), articulo.getNombrearticulo())
      .set(DSL.field("descriparticulo"), articulo.getDescriparticulo())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
