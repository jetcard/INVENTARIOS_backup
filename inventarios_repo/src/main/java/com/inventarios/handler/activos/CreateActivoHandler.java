package com.inventarios.handler.activos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.services.CreateActivoAbstractHandler;
import com.inventarios.model.Activo;
import org.jooq.impl.DSL;

public class CreateActivoHandler extends CreateActivoAbstractHandler {
  @Override
  protected void save(Activo activo, Long responsableID, Long tipoID, Long grupoID, Long articuloID) {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("codinventario"), activo.getCodinventario())
            .set(DSL.field("modelo"), activo.getModelo())
            .set(DSL.field("marca"), activo.getMarca())
            .set(DSL.field("nroserie"), activo.getNroserie())
            .set(DSL.field("fechaingreso"), activo.getFechaingreso())
            .set(DSL.field("importe"), activo.getImporte())
            .set(DSL.field("moneda"), activo.getMoneda())
            .set(DSL.field("responsableID"), responsableID)
            .set(DSL.field("tipoID"), tipoID)
            .set(DSL.field("grupoId"), grupoID)
            .set(DSL.field("articuloID"), articuloID)
            .execute();
  }
}
