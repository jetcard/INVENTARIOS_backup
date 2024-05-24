package com.inventarios.handler.comunes.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.util.*;

import com.inventarios.handler.comunes.response.ComunResponseRest;
import com.inventarios.model.Comun;
import com.inventarios.model.Grupo;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadComunAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> COMUN_TABLE = DSL.table("comun");

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
    ComunResponseRest responseRest = new ComunResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getComunResponse().setListacomuns(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Comuns encontrados");
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

  protected List<Comun> convertResultToList(Result<Record> result) {
    List<Comun> listaComuns = new ArrayList<>();
    for (Record record : result) {
      Comun comun = new Comun();
      comun.setId(record.getValue("id", Long.class));
      comun.setDescripcomun(record.getValue("modelo", String.class));
      comun.setDescripcortacomun(record.getValue("marca", String.class));

      //comun.setGrupo(record.getValue("grupoId", Grupo.class));
      ///comun.setPicture(record.getValue("picture", byte[].class));
      listaComuns.add(comun);
    }
    return listaComuns;
  }

}
