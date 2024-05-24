package com.inventarios.handler.responsables.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.responsables.response.ResponsableResponseRest;
import com.inventarios.model.Responsable;
import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class UpdateResponsableAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }
  protected abstract void update(Responsable responsable, Long id);
   @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
     input.setHeaders(headers);
     ResponsableResponseRest responseRest = new ResponsableResponseRest();
     APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
     List<Responsable> list = new ArrayList<>();
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

         Responsable responsableFromBody = new Gson().fromJson(body, Responsable.class);

         if (responsableFromBody != null) {
               DSLContext dsl = RDSConexion.getDSL();
               Optional<Responsable> responsableSearch = dsl.select()
                       .from(RESPONSABLE_TABLE)
                       .where(DSL.field("id", Long.class).eq(id))
                       .fetchOptionalInto(Responsable.class);
               if (responsableSearch.isPresent()) {
                 Responsable responsable = responsableSearch.get();
                 responsable.setArearesponsable(responsable.getArearesponsable());
                 responsable.setNombresyapellidos(responsable.getNombresyapellidos());
                 dsl.update(RESPONSABLE_TABLE)
                         .set(DSL.field("arearesponsable"), responsableFromBody.getArearesponsable())
                         .set(DSL.field("nombresyapellidos"), responsableFromBody.getNombresyapellidos())
                         .where(DSL.field("id").eq(responsable.getId()))
                         .execute();
                 list.add(responsable);
                 responseRest.getResponsableResponse().setListaresponsables(list);
                 responseRest.setMetadata("Respuesta ok", "00", "Responsable actualizado");
               } else {
                 responseRest.setMetadata("Respuesta nok", "-1", "Responsable no encontrado");
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
}