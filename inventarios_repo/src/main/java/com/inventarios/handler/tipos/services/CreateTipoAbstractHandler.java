package com.inventarios.handler.tipos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.model.Tipo;
import java.util.HashMap;
import java.util.Map;
import com.inventarios.handler.tipos.response.TipoResponseRest;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class CreateTipoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }
  protected abstract void save(String nombretipo, String descriptipo);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    TipoResponseRest responseRest = new TipoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String body = input.getBody();
    context.getLogger().log("body " + body);
    String output ="";
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Tipo tipo = new Gson().fromJson(body, Tipo.class);
        if (tipo != null) {
          save(tipo.getNombretipo(), tipo.getDescriptipo());
          responseRest.setMetadata("Respuesta ok", "00", "Tipo guardado");
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