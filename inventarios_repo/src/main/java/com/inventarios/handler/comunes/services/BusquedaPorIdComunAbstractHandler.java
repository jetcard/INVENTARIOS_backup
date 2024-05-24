package com.inventarios.handler.comunes.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.comunes.response.ComunResponseRest;
import com.inventarios.model.Comun;
import java.util.*;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaPorIdComunAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> COMUN_TABLE = DSL.table("comun");
  protected static final org.jooq.Field<String> COMUN_TABLE_COLUMNA = DSL.field("nombrecomun", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreComun(String argv);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ComunResponseRest responseRest = new ComunResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreComun(idString);
      responseRest.getComunResponse().setListacomuns(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Comuns encontrados");
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

  protected List<Comun> convertResultToList(Result<Record> result) {
    List<Comun> listaComuns = new ArrayList<>();
    for (Record record : result) {
      Comun comun = new Comun();
      comun.setId(record.getValue("id", Long.class));
      comun.setDescripcortacomun(record.getValue("descripcortacomun", String.class));
      comun.setDescripcomun(record.getValue("descripcomun", String.class));
      listaComuns.add(comun);
    }
    return listaComuns;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Comun comun = new Gson().fromJson(body, Comun.class);
        if (comun != null) {
          context.getLogger().log("comun.getId() = " + comun.getId());
          if (id.equals(comun.getId())) {
            busquedaPorId(comun.getId());
            list.add(comun);
            responseRest.getComunResponse().setListacomuns(list);
            responseRest.setMetadata("Respuesta ok", "00", "Comun actualizado");
          } else {
            return response
                    .withBody("Id no coincide con el id del body")
                    .withStatusCode(400);
          }
        }
        output = new Gson().toJson(responseRest);
      }
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al eliminar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }*/

  /*private String convertResultToJson(Result<Record> result) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    for (Record record : result) {
      Map<String, Object> recordMap = new HashMap<>();
      for (Field<?> field : result.fields()) {
        recordMap.put(field.getName(), record.get(field));
      }
      resultList.add(recordMap);
    }
    return new Gson().toJson(resultList);
  }*/


