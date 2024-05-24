package com.inventarios.handler.grupos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.grupos.response.GrupoResponseRest;
import com.inventarios.model.Grupo;
import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class UpdateGrupoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }
  protected abstract void update(Grupo grupo, Long id);
   @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
     input.setHeaders(headers);
     GrupoResponseRest responseRest = new GrupoResponseRest();
     APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
     List<Grupo> list = new ArrayList<>();
     String output = "";
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
     try {
         String body = input.getBody();
         // Convertir el cuerpo de la solicitud a un objeto Grupo
         Grupo grupoFromBody = new Gson().fromJson(body, Grupo.class);
         // Verificar si el objeto Grupo obtenido del cuerpo de la solicitud es válido
         if (grupoFromBody != null) {
               DSLContext dsl = RDSConexion.getDSL();
               Optional<Grupo> grupoSearch = dsl.select()
                       .from(GRUPO_TABLE)
                       .where(DSL.field("id", Long.class).eq(id))
                       .fetchOptionalInto(Grupo.class);
               if (grupoSearch.isPresent()) {
                 Grupo grupo = grupoSearch.get();
                 grupo.setNombregrupo(grupo.getNombregrupo());
                 grupo.setDescripgrupo(grupo.getDescripgrupo());
                 dsl.update(GRUPO_TABLE)
                         .set(DSL.field("nombregrupo"), grupoFromBody .getNombregrupo())
                         .set(DSL.field("descripgrupo"), grupoFromBody .getDescripgrupo())
                         .where(DSL.field("id").eq(grupo.getId()))
                         .execute();
                 list.add(grupo);
                 responseRest.getGrupoResponse().setListagrupos(list);
                 responseRest.setMetadata("Respuesta ok", "00", "Grupo actualizado");
               } else {
                 responseRest.setMetadata("Respuesta nok", "-1", "Grupo no encontrado");
               }
               output = new Gson().toJson(responseRest);
               return response.withStatusCode(200)
                       .withBody(output);
         } else {
             return response
                     .withBody("Invalid group data in request body")
                     .withStatusCode(400);
         }
     } catch (Exception e) {
       responseRest.setMetadata("Respuesta nok", "-1", "Error al actualizar");
       return response
               .withBody(e.toString())
               .withStatusCode(500);
     }
   }
    /*GrupoResponseRest responseRest = new GrupoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    List<Grupo> list = new ArrayList<>();
    String output ="";
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

    try{
      Optional<Grupo> grupoSearch = grupoDao.findById(id);
      if (grupoSearch.isPresent()) {
        // se procederá a actualizar el registro
        grupoSearch.get().setApellidoPaterno(grupo.getApellidoPaterno());
        grupoSearch.get().setApellidoMaterno(grupo.getApellidoMaterno());
        Grupo grupo = save(grupo.getNombregrupo(), grupo.getDescripgrupo());
        if (grupo != null) {
          list.add(grupo);
          responseRest.getGrupoResponse().setListagrupos(list);
          responseRest.setMetadata("Respuesta ok", "00", "Grupo actualizado");
        } else {
          responseRest.setMetadata("Respuesta nok", "-1", "Grupo no encontrado");
          //return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
        }
      }
        return response.withStatusCode(200)
                .withBody(output);

  } catch (Exception e) {
    responseRest.setMetadata("Respuesta nok", "-1", "Error al actualizar");
    return response
            .withBody(e.toString())
            .withStatusCode(500);
  }*/

    /*
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
        Grupo grupo = new Gson().fromJson(body, Grupo.class);
        if (grupo != null) {
          context.getLogger().log("grupo.getId() = " + grupo.getId());
          if (id.equals(grupo.getId())) {
            update(grupo.getId(), grupo.getNombregrupo(), grupo.getDescripgrupo());
            list.add(grupo);
            responseRest.getGrupoResponse().setListagrupos(list);
            responseRest.setMetadata("Respuesta ok", "00", "Grupo actualizado");
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
      responseRest.setMetadata("Respuesta nok", "-1", "Error al actualizar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }*/

}