package com.inventarios.handler.responsables.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.util.*;

import com.inventarios.model.Responsable;
import com.inventarios.handler.responsables.response.ResponsableResponseRest;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadResponsableAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
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
    ResponsableResponseRest responseRest = new ResponsableResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getResponsableResponse().setListaresponsables(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Responsables encontrados");
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