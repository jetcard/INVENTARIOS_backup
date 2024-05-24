package com.inventarios.handler.activos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.services.UpdateActivoAbstractHandler;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.sql.Date;

public class UpdateActivoHandler extends UpdateActivoAbstractHandler {
  protected void update(Long id, String codinventario, String modelo, String marca, String nroSerie, Date fechaingreso, BigDecimal importe,
                        String moneda) {
    var dsl = RDSConexion.getDSL();
    //DSLContext dsl = DependencyFactory.getDSL();
    dsl.update(ACTIVO_TABLE)
      .set(DSL.field("codinventario"), codinventario)
      .set(DSL.field("modelo"), modelo)
      .set(DSL.field("marca"), marca)
      .set(DSL.field("nroSerie"), nroSerie)
      .set(DSL.field("fechaingreso"), fechaingreso)
      .set(DSL.field("importe"), importe)
      .set(DSL.field("moneda"), moneda)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
