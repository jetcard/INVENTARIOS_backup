package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.ReadProveedorAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;
public class ReadProveedorHandler extends ReadProveedorAbstractHandler {
  protected Result<Record> read() {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(PROVEEDOR_TABLE).fetch();
  }

}