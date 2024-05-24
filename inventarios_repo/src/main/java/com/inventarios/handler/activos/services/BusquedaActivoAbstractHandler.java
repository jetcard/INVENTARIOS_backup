package com.inventarios.handler.activos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.activos.response.ActivoResponseRest;
import com.inventarios.model.Activo;
import com.inventarios.util.Conversiones;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BusquedaActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final Table<Record> ACTIVO_TABLE = DSL.table("activo");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaActivo(String codinventario, String modelo, String marca, String nroSerie, LocalDate fechaCompraDesde, LocalDate fechaCompraHasta);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    // Obtener los parámetros de consulta
    Map<String, String> queryParameters = input.getQueryStringParameters();
    String codinventario = queryParameters != null ? queryParameters.get("codinventario") : null;
    logger.log("codinventario: " + codinventario);
    String modelo = queryParameters != null ? queryParameters.get("modelo") : null;
    logger.log("modelo: " + modelo);
    String marca = queryParameters != null ? queryParameters.get("marca") : null;
    logger.log("marca: " + marca);
    String nroSerie = queryParameters != null ? queryParameters.get("nroserie") : null;
    logger.log("nroserie: " + nroSerie);
    String fechaCompraDesde = queryParameters != null ? queryParameters.get("fechadesde") : null;
    logger.log("fechadesde: " + fechaCompraDesde);
    String fechaCompraHasta = queryParameters != null ? queryParameters.get("fechahasta") : null;
    logger.log("fechahasta: " + fechaCompraHasta);
    String output = "";
    LocalDate fechaDesde = new Conversiones().convertirALocalDate(fechaCompraDesde);
    LocalDate fechaHasta = new Conversiones().convertirALocalDate(fechaCompraHasta);
    try {
      Result<Record> result = busquedaActivo(codinventario, modelo, marca, nroSerie, fechaDesde, fechaHasta);
      responseRest.getActivoResponse().setListaactivos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Activos encontrados");
      output = new Gson().toJson(responseRest);
      return response.withStatusCode(200)
                    .withBody(output);
    } catch (Exception e) {
        responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
                    .withStatusCode(500);
    }
  }

  protected List<Activo> convertResultToList(Result<Record> result) {
    List<Activo> listaActivos = new ArrayList<>();
    for (Record record : result) {
      Activo activo = new Activo();
      activo.setId(record.getValue("id", Long.class));
      activo.setCodinventario(record.getValue("codinventario", String.class));
      activo.setModelo(record.getValue("modelo", String.class));
      activo.setMarca(record.getValue("marca", String.class));
      activo.setNroserie(record.getValue("nroserie", String.class));
      activo.setFechaingreso(record.getValue("fechaingreso", Date.class));
      activo.setMoneda(record.getValue( "nroserie", String.class));
      activo.setImporte(record.getValue("importe", BigDecimal.class));
      //Responsable responsable = new Responsable();
      //activo.setResponsable(responsable);
      ///activo.setResponsable(record.getValue("responsableId", Responsable.class));
      ///activo.setGrupo(record.getValue("grupoId", Grupo.class));
      listaActivos.add(activo);
    }
    return listaActivos;
  }
}