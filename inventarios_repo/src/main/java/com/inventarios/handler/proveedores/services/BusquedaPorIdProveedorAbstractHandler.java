package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import com.inventarios.model.Proveedor;

import java.util.*;

import com.inventarios.model.Responsable;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaPorIdProveedorAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected static final org.jooq.Field<String> PROVEEDOR_TABLE_COLUMNA = DSL.field("nombreproveedor", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreProveedor(String argv);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ProveedorResponseRest responseRest = new ProveedorResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreProveedor(idString);
      responseRest.getProveedorResponse().setListaproveedores(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Proveedors encontrados");
      output = new Gson().toJson(responseRest);
      return response.withStatusCode(200)
                    .withBody(output);
  } catch (Exception e) {
        responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
        .withStatusCode(500);
        }

  }

  protected List<Proveedor> convertResultToList(Result<Record> result) {
    List<Proveedor> listaProveedors = new ArrayList<>();
    for (Record record : result) {
      Proveedor proveedor = new Proveedor();
      proveedor.setId(record.getValue("id", Long.class));
      proveedor.setNombreproveedor(record.getValue("nombreproveedor", String.class));
      proveedor.setDescripproveedor(record.getValue("descripproveedor", String.class));
      listaProveedors.add(proveedor);
    }
    return listaProveedors;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Proveedor proveedor = new Gson().fromJson(body, Proveedor.class);
        if (proveedor != null) {
          context.getLogger().log("proveedor.getId() = " + proveedor.getId());
          if (id.equals(proveedor.getId())) {
            busquedaPorId(proveedor.getId());
            list.add(proveedor);
            responseRest.getProveedorResponse().setListaproveedors(list);
            responseRest.setMetadata("Respuesta ok", "00", "Proveedor actualizado");
          } else {
            return response
                    .withBody("Id no coincide con el id del body")
                    .withStatusCode(400);
          }
        }
        output = new Gson().toJson(responseRest);
      }
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al eliminar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }*/

  /*private String convertResultToJson(Result<Record> result) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    for (Record record : result) {
      Map<String, Object> recordMap = new HashMap<>();
      for (Field<?> field : result.fields()) {
        recordMap.put(field.getName(), record.get(field));
      }
      resultList.add(recordMap);
    }
    return new Gson().toJson(resultList);
  }*/


