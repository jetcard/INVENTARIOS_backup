package com.inventarios.handler.responsables.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.model.Responsable;
import java.util.HashMap;
import java.util.Map;
import com.inventarios.handler.responsables.response.ResponsableResponseRest;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class CreateResponsableAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }
  protected abstract void save(String arearesponsable, String nombresyapellidos);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ResponsableResponseRest responseRest = new ResponsableResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    //String body = "{\"nombregrupo\":\"categoria\",\"descripgrupo\":\"sa\"}";
    String body = input.getBody();
    context.getLogger().log("body " + body);
    String output ="";
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Responsable responsable = new Gson().fromJson(body, Responsable.class);
        if (responsable != null) {
          save(responsable.getArearesponsable(), responsable.getNombresyapellidos());
          responseRest.setMetadata("Respuesta ok", "00", "Responsable guardado");
        }
        output = new Gson().toJson(responseRest);
      }
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }
}