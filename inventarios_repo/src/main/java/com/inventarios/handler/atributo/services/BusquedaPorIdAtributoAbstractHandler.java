package com.inventarios.handler.atributo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.atributo.response.AtributoResponseRest;
import com.inventarios.model.Articulo;
import com.inventarios.model.Atributo;
import java.util.*;
import com.inventarios.model.Responsable;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaPorIdAtributoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> ATRIBUTO_TABLE = DSL.table("atributo");
  protected static final org.jooq.Field<String> ATRIBUTO_TABLE_COLUMNA = DSL.field("nombreatributo", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreAtributo(String argv);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    AtributoResponseRest responseRest = new AtributoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreAtributo(idString);
      responseRest.getAtributoResponse().setListaatributos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributos encontrados");
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

  protected List<Atributo> convertResultToList(Result<Record> result) {
    List<Atributo> listaAtributos = new ArrayList<>();
    for (Record record : result) {
      Atributo atributo = new Atributo();
      atributo.setId(record.getValue("id", Long.class));
      atributo.setResponsable(record.getValue("responsableId", Responsable.class));
      atributo.setArticulo(record.getValue("articuloId", Articulo.class));
      atributo.setAtributos(record.getValue("descripatributo", List.class));
      listaAtributos.add(atributo);
    }
    return listaAtributos;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Atributo atributo = new Gson().fromJson(body, Atributo.class);
        if (atributo != null) {
          context.getLogger().log("atributo.getId() = " + atributo.getId());
          if (id.equals(atributo.getId())) {
            busquedaPorId(atributo.getId());
            list.add(atributo);
            responseRest.getAtributoResponse().setListaatributos(list);
            responseRest.setMetadata("Respuesta ok", "00", "Atributo actualizado");
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


