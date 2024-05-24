package com.inventarios.handler.atributo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.util.*;

import com.inventarios.handler.atributo.response.AtributoResponseRest;
import com.inventarios.model.Articulo;
import com.inventarios.model.Atributo;
import com.inventarios.model.Responsable;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadAtributoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ATRIBUTO_TABLE = DSL.table("atributo");

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
    AtributoResponseRest responseRest = new AtributoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getAtributoResponse().setListaatributos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributos encontrados");
      output = new Gson().toJson(responseRest);
      return response.withStatusCode(200).withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
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

      Responsable responsable=new Responsable();
      responsable.setId(record.getValue("responsableid", Long.class));
      Articulo articulo=new Articulo();
      articulo.setId(record.getValue("articuloid", Long.class));
      atributo.setResponsable(responsable);
      atributo.setArticulo(articulo);
      listaAtributos.add(atributo);
    }
    return listaAtributos;
  }

}
