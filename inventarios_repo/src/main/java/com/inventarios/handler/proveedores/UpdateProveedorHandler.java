package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.UpdateProveedorAbstractHandler;
import com.inventarios.model.Proveedor;
import org.jooq.impl.DSL;
public class UpdateProveedorHandler extends UpdateProveedorAbstractHandler {
  protected void update(Proveedor proveedor, Long id) {
    var dsl = RDSConexion.getDSL();
    dsl.update(PROVEEDOR_TABLE)
      .set(DSL.field("nombreproveedor"), proveedor.getNombreproveedor())
      .set(DSL.field("descripproveedor"), proveedor.getDescripproveedor())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
