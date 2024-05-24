package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.model.Proveedor;
import java.util.HashMap;
import java.util.Map;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class CreateProveedorAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }
  protected abstract void save(String nombreproveedor, String descripproveedor);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ProveedorResponseRest responseRest = new ProveedorResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String body = input.getBody();
    //String body = "{ \"nombreproveedor\": \"hello world\", \"descripproveedor\": \"As\" }"; //

    context.getLogger().log("body " + body);
    String output ="";
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Proveedor proveedor = new Gson().fromJson(body, Proveedor.class);
        if (proveedor != null) {
          save(proveedor.getNombreproveedor(), proveedor.getDescripproveedor());
          responseRest.setMetadata("Respuesta ok", "00", "Proveedor guardado");
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