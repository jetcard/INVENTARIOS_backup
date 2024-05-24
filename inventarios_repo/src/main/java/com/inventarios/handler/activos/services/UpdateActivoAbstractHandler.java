package com.inventarios.handler.activos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.model.Activo;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class UpdateActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ACTIVO_TABLE = DSL.table("activo");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }

  protected abstract void update(Long id, String codinventario, String modelo, String marca, String nroSerie, Date fechaCompra,
                                 BigDecimal importe, String moneda
                                 //String categoryId,
                                 //String picture
                                 );

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
        Activo activo = new Gson().fromJson(body, Activo.class);
        if (activo != null) {
          if (id.equals(activo.getId())) {
            update(activo.getId(), activo.getCodinventario(), activo.getModelo(), activo.getMarca(), activo.getNroserie(), activo.getFechaingreso(),
                    activo.getImporte(), activo.getMoneda());
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