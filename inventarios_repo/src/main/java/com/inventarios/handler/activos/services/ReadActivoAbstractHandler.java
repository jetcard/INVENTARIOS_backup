package com.inventarios.handler.activos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import com.inventarios.handler.activos.response.ActivoResponseRest;
import com.inventarios.model.Activo;
import com.inventarios.model.Grupo;
import com.inventarios.model.Responsable;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ACTIVO_TABLE = DSL.table("activo");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record> read();

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getActivoResponse().setListaactivos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Activos encontrados");
      output = new Gson().toJson(responseRest);
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
  }

  private String convertResultToJson(Result<Record> result) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    for (Record record : result) {
      Map<String, Object> recordMap = new LinkedHashMap<>();
      for (Field<?> field : result.fields()) {
        recordMap.put(field.getName(), record.get(field));
      }
      System.out.println("recordMap: "+recordMap);
      resultList.add(recordMap);
    }
    System.out.println("resultList: "+resultList);
    return new Gson().toJson(resultList);
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
      activo.setImporte(record.getValue("importe", BigDecimal.class));
      activo.setMoneda(record.getValue("moneda", String.class));
      ///activo.setResponsable(record.getValue("responsableId", Responsable.class));
      ///activo.setGrupo(record.getValue("grupoId", Grupo.class));
      //activo.setPicture(record.getValue("picture", byte[].class));
      listaActivos.add(activo);
    }
    return listaActivos;
  }

}
