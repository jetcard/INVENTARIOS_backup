package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.BusquedaPorIdProveedorAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdProveedorHandler extends BusquedaPorIdProveedorAbstractHandler {
  protected Result<Record> busquedaPorNombreProveedor(String filter) {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(PROVEEDOR_TABLE)
            .where(PROVEEDOR_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

}