package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.util.*;
import com.inventarios.model.Proveedor;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
public abstract class ReadProveedorAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
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
    ProveedorResponseRest responseRest = new ProveedorResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getProveedorResponse().setListaproveedores(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Proveedores encontrados");
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
  private String convertResultToJson(Result<Record> result) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    for (Record record : result) {
      Map<String, Object> recordMap = new LinkedHashMap<>();
      for (Field<?> field : result.fields()) {
        recordMap.put(field.getName(), record.get(field));
      }
      resultList.add(recordMap);
    }
    return new Gson().toJson(resultList);
  }
  protected List<Proveedor> convertResultToList(Result<Record> result) {
    List<Proveedor> listaProveedores = new ArrayList<>();
    for (Record record : result) {
      Proveedor proveedor = new Proveedor();
      proveedor.setId(record.getValue("id", Long.class));
      proveedor.setNombreproveedor(record.getValue("nombreproveedor", String.class));
      proveedor.setDescripproveedor(record.getValue("descripproveedor", String.class));
      listaProveedores.add(proveedor);
    }
    return listaProveedores;
  }

}