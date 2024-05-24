package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.CreateProveedorAbstractHandler;
import org.jooq.impl.DSL;
public class CreateProveedorHandler extends CreateProveedorAbstractHandler {
  protected void save(String nombreproveedor, String descripproveedor) {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(PROVEEDOR_TABLE)
      .set(DSL.field("nombreproveedor"), nombreproveedor)
      .set(DSL.field("descripproveedor"), descripproveedor)
      .execute();
  }

}
