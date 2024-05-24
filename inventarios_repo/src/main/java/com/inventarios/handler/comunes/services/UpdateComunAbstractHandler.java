package com.inventarios.handler.comunes.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.model.Comun;
import java.util.HashMap;
import java.util.Map;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class UpdateComunAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> COMUN_TABLE = DSL.table("comun");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }

  protected abstract void update(Long id, String descripcomun, String descripcortacomun);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    Long id = null;
    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      return response
              .withBody("Invalid id in path")
              .withStatusCode(400);
    }
    String body = input.getBody();
    context.getLogger().log("body " + body);
    String output ="";
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Comun comun = new Gson().fromJson(body, Comun.class);
        if (comun != null) {
          if (id.equals(comun.getId())) {
            update(comun.getId(), comun.getDescripcomun(), comun.getDescripcortacomun());
          } else {
            return response
                    .withBody("Id in path does not match id in body")
                    .withStatusCode(400);
          }
        }
        output =String.format("{ \"message\": \"%s\" }", body);
      }
      return response.withStatusCode(200)
              .withBody(output);

    } catch (Exception e) {
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }
}