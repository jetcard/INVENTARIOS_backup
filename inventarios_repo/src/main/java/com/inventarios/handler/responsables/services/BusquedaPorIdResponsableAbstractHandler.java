package com.inventarios.handler.responsables.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.responsables.response.ResponsableResponseRest;
import com.inventarios.model.Responsable;
import java.util.*;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaPorIdResponsableAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> RESPONSABLE_TABLE = DSL.table("responsable");
  protected static final org.jooq.Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreResponsable(String argv);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ResponsableResponseRest responseRest = new ResponsableResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreResponsable(idString);
      responseRest.getResponsableResponse().setListaresponsables(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Responsables encontrados");
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

  protected List<Responsable> convertResultToList(Result<Record> result) {
    List<Responsable> listaResponsables = new ArrayList<>();
    for (Record record : result) {
      Responsable responsable = new Responsable();
      responsable.setId(record.getValue("id", Long.class));
      responsable.setArearesponsable(record.getValue("arearesponsable", String.class));
      responsable.setNombresyapellidos(record.getValue("nombresyapellidos", String.class));
      listaResponsables.add(responsable);
    }
    return listaResponsables;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Responsable responsable = new Gson().fromJson(body, Responsable.class);
        if (responsable != null) {
          context.getLogger().log("responsable.getId() = " + responsable.getId());
          if (id.equals(responsable.getId())) {
            busquedaPorId(responsable.getId());
            list.add(responsable);
            responseRest.getResponsableResponse().setListaresponsables(list);
            responseRest.setMetadata("Respuesta ok", "00", "Responsable actualizado");
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


