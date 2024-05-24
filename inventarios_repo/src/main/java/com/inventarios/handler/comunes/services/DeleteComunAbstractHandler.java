package com.inventarios.handler.comunes.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inventarios.model.Comun;

import java.util.HashMap;
import java.util.Map;

import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
//import org.springframework.web.bind.annotation.CrossOrigin;

public abstract class DeleteComunAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> COMUN_TABLE = DSL.table("comun");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "DELETE");
  }

  protected abstract void delete(long id);

  @Override
 
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers)
      .withStatusCode(200);
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Comun comun = new Gson().fromJson(body, Comun.class);
        if (comun != null) {
          //delete(2);
          delete(comun.getId());
        }
      }
      return response;
    } catch (Exception e) {
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }
}

