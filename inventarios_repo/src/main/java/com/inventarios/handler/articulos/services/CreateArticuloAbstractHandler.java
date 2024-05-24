package com.inventarios.handler.articulos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.model.Articulo;
import java.util.HashMap;
import java.util.Map;
import com.inventarios.handler.articulos.response.ArticuloResponseRest;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class CreateArticuloAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ARTICULO_TABLE = DSL.table("articulo");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }
  protected abstract void save(String nombrearticulo, String descriparticulo);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ArticuloResponseRest responseRest = new ArticuloResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String body = input.getBody();
    context.getLogger().log("body " + body);
    String output ="";
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Articulo articulo = new Gson().fromJson(body, Articulo.class);
        if (articulo != null) {
          save(articulo.getNombrearticulo(), articulo.getDescriparticulo());
          responseRest.setMetadata("Respuesta ok", "00", "Articulo guardado");
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