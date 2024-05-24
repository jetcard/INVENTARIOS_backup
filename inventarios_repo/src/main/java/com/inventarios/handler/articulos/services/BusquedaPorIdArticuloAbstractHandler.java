package com.inventarios.handler.articulos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.articulos.response.ArticuloResponseRest;
import com.inventarios.model.Articulo;

import java.util.*;

import com.inventarios.model.Responsable;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaPorIdArticuloAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> ARTICULO_TABLE = DSL.table("articulo");
  protected static final org.jooq.Field<String> ARTICULO_TABLE_COLUMNA = DSL.field("nombrearticulo", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreArticulo(String argv);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ArticuloResponseRest responseRest = new ArticuloResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreArticulo(idString);
      responseRest.getArticuloResponse().setListaarticulos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Articulos encontrados");
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

  protected List<Articulo> convertResultToList(Result<Record> result) {
    List<Articulo> listaArticulos = new ArrayList<>();
    for (Record record : result) {
      Articulo articulo = new Articulo();
      articulo.setId(record.getValue("id", Long.class));
      articulo.setNombrearticulo(record.getValue("nombrearticulo", String.class));
      articulo.setDescriparticulo(record.getValue("descriparticulo", String.class));
      listaArticulos.add(articulo);
    }
    return listaArticulos;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Articulo articulo = new Gson().fromJson(body, Articulo.class);
        if (articulo != null) {
          context.getLogger().log("articulo.getId() = " + articulo.getId());
          if (id.equals(articulo.getId())) {
            busquedaPorId(articulo.getId());
            list.add(articulo);
            responseRest.getArticuloResponse().setListaarticulos(list);
            responseRest.setMetadata("Respuesta ok", "00", "Articulo actualizado");
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


