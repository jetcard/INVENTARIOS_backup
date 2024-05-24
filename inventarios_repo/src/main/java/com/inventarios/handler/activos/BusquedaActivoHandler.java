package com.inventarios.handler.activos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.services.BusquedaActivoAbstractHandler;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.jooq.impl.DSL.field;

public class BusquedaActivoHandler extends BusquedaActivoAbstractHandler {

  /*private static final Map<String, Field<String>> campoMap = new HashMap<>();

  static {
    campoMap.put("inputfecha", DSL.field("fechacompra", String.class));
  }*/
  protected Result<Record> busquedaActivo(String codinventario, String modelo, String marca, String nroSerie, LocalDate fechaCompraDesde, LocalDate fechaCompraHasta) {
    DSLContext dsl = RDSConexion.getDSL();
    Condition condition = DSL.trueCondition();
    if (codinventario != null && !codinventario.isEmpty()) {
      condition = condition.and(field("codinventario").likeIgnoreCase("%" + codinventario + "%"));
    }
    if (modelo != null && !modelo.isEmpty()) {
      condition = condition.and(field("modelo").likeIgnoreCase("%" + modelo + "%"));
    }
    if (marca != null && !marca.isEmpty()) {
      condition = condition.and(field("marca").likeIgnoreCase("%" + marca + "%"));
    }
    if (nroSerie != null && !nroSerie.isEmpty()) {
      condition = condition.and(field("nroserie").likeIgnoreCase("%" + nroSerie + "%"));
    }
    if (fechaCompraDesde != null && fechaCompraHasta != null) {
      condition = condition.and(field("fechaingreso").between(fechaCompraDesde).and(fechaCompraHasta));
    }
    return dsl.select()
            .from(ACTIVO_TABLE)
            .where(condition)
            .fetch();


    /*if (fechaCompraDesde != null && fechaCompraHasta != null) {
      // Use both 'fechaingreso' and 'fechaegreso' for date range condition
      condition = condition.and(field("fechaingreso").between(fechaCompraDesde).and(fechaCompraHasta)
              .or(field("fechaegreso").between(fechaCompraDesde).and(fechaCompraHasta)));
    }*/
   /* if (fechaCompraDesde != null && fechaCompraHasta != null) {
      condition = condition.and(field("fechaingreso").between(fechaCompraDesde).and(fechaCompraHasta));
    }*/
    /*if (fechaCompraDesde != null && !fechaCompraDesde.isEmpty() && fechaCompraHasta != null && !fechaCompraHasta.isEmpty()) {
      condition = condition.and(field("fechacompra").between(fechaCompraDesde).and(fechaCompraHasta));
    } else if (fechaCompraDesde != null && !fechaCompraDesde.isEmpty()) {
      condition = condition.and(field("fechacompra").greaterOrEqual(fechaCompraDesde));
    } else if (fechaCompraHasta != null && !fechaCompraHasta.isEmpty()) {
      condition = condition.and(field("fechacompra").lessOrEqual(fechaCompraHasta));
    }*/



    /*return dsl.select()
            .from(ACTIVO_TABLE)
            .where(field("modelo").eq(modelo)
                    .and(field("marca").eq(marca))
                    .and(field("nroserie").eq(nroSerie))
                    .and(field("fechacompra").eq(fechaCompra)))
            .fetch();*/
    /*return dsl.select()
            .from(ACTIVO_TABLE)
            .where(DSL.field(ACTIVO_TABLE_COLUMNA).eq(id))
            .fetch();*/
    /*Field<String> field = campoMap.get(campo);
    if (field == null) {
      throw new IllegalArgumentException("Campo de búsqueda no válido: " + campo);
    }
    return dsl.select()
            .from(ACTIVO_TABLE)
            .where(field.eq(id))
            .fetch();*/
    /*return dsl.select()
            .from(ACTIVO_TABLE)
            .where(ACTIVO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();*/
    /*switch (campo) {
      case "inputfecha":
        return dsl.select()
                .from(ACTIVO_TABLE)
                .where(ACTIVO_TABLE_COLUMNA.eq(id)) // Suponiendo que 'id' corresponde a la fecha
                .fetch();
      // Otros casos según los diferentes tipos de búsqueda que puedas tener
      default:
        throw new IllegalArgumentException("Campo de búsqueda no válido: " + campo);
    }*/
  }
}