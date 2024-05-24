package com.inventarios.handler.atributos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.util.*;

import com.inventarios.handler.atributos.response.AtributosResponseRest;
import com.inventarios.model.Atributos;
import com.inventarios.model.Grupo;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadAtributosAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ATRIBUTOS_TABLE = DSL.table("atributos");

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
    AtributosResponseRest responseRest = new AtributosResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getAtributosResponse().setListaatributoss(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributoss encontrados");
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
      System.out.println("recordMap: "+recordMap);
      resultList.add(recordMap);
    }
    System.out.println("resultList: "+resultList);
    return new Gson().toJson(resultList);
  }

  protected List<Atributos> convertResultToList(Result<Record> result) {
    List<Atributos> listaAtributoss = new ArrayList<>();
    for (Record record : result) {
      Atributos atributos = new Atributos();
      atributos.setId(record.getValue("id", Long.class));
      ///atributos.setDescripatributo(record.getValue("modelo", String.class));
      atributos.setNombreatributo(record.getValue("marca", String.class));

      //atributos.setGrupo(record.getValue("grupoId", Grupo.class));
      ///atributos.setPicture(record.getValue("picture", byte[].class));
      listaAtributoss.add(atributos);
    }
    return listaAtributoss;
  }

}
