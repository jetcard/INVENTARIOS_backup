package com.inventarios.handler.articulos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.util.*;

import com.inventarios.model.Articulo;
import com.inventarios.handler.articulos.response.ArticuloResponseRest;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadArticuloAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> ARTICULO_TABLE = DSL.table("articulo");
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
    ArticuloResponseRest responseRest = new ArticuloResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getArticuloResponse().setListaarticulos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Articulos encontrados");
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