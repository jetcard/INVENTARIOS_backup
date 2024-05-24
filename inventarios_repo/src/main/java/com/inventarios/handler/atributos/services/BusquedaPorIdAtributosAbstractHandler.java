package com.inventarios.handler.atributos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.atributos.response.AtributosResponseRest;
import com.inventarios.model.Atributos;
import java.util.*;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaPorIdAtributosAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> ATRIBUTOS_TABLE = DSL.table("atributos");
  protected static final org.jooq.Field<String> ATRIBUTOS_TABLE_COLUMNA = DSL.field("nombreatributos", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreAtributos(String argv);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    AtributosResponseRest responseRest = new AtributosResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreAtributos(idString);
      responseRest.getAtributosResponse().setListaatributoss(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributoss encontrados");
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

  protected List<Atributos> convertResultToList(Result<Record> result) {
    List<Atributos> listaAtributoss = new ArrayList<>();
    for (Record record : result) {
      Atributos atributos = new Atributos();
      atributos.setId(record.getValue("id", Long.class));
      //atributos.setAtributo(record.getValue("nombreatributo", Atributo.class));
      atributos.setNombreatributo(record.getValue("nombreatributo", String.class));
      listaAtributoss.add(atributos);
    }
    return listaAtributoss;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Atributos atributos = new Gson().fromJson(body, Atributos.class);
        if (atributos != null) {
          context.getLogger().log("atributos.getId() = " + atributos.getId());
          if (id.equals(atributos.getId())) {
            busquedaPorId(atributos.getId());
            list.add(atributos);
            responseRest.getAtributosResponse().setListaatributoss(list);
            responseRest.setMetadata("Respuesta ok", "00", "Atributos actualizado");
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


