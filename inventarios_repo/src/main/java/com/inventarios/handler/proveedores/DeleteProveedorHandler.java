package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.DeleteProveedorAbstractHandler;
import org.jooq.impl.DSL;

public class DeleteProveedorHandler extends DeleteProveedorAbstractHandler {
  protected void delete(long id) {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(PROVEEDOR_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
