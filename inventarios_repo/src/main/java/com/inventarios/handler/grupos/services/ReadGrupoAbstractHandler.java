package com.inventarios.handler.grupos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.util.*;
import com.inventarios.model.Grupo;
import com.inventarios.handler.grupos.response.GrupoResponseRest;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadGrupoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
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
    GrupoResponseRest responseRest = new GrupoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getGrupoResponse().setListagrupos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Grupos encontrados");
      output = new Gson().toJson(responseRest);
      return response.withStatusCode(200).withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }

  protected List<Grupo> convertResultToList(Result<Record> result) {
    List<Grupo> listaGrupos = new ArrayList<>();
    for (Record record : result) {
      Grupo grupo = new Grupo();
      grupo.setId(record.getValue("id", Long.class));
      grupo.setNombregrupo(record.getValue("nombregrupo", String.class));
      grupo.setDescripgrupo(record.getValue("descripgrupo", String.class));
      listaGrupos.add(grupo);
    }
    return listaGrupos;
  }

}