package com.inventarios.handler.articulos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.response.ArticuloResponseRest;
import com.inventarios.model.Articulo;
import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class UpdateArticuloAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> ARTICULO_TABLE = DSL.table("articulo");
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }
  protected abstract void update(Articulo articulo, Long id);
   @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
     input.setHeaders(headers);
     ArticuloResponseRest responseRest = new ArticuloResponseRest();
     APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
     List<Articulo> list = new ArrayList<>();
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

         Articulo articuloFromBody = new Gson().fromJson(body, Articulo.class);

         if (articuloFromBody != null) {
               DSLContext dsl = RDSConexion.getDSL();
               Optional<Articulo> articuloSearch = dsl.select()
                       .from(ARTICULO_TABLE)
                       .where(DSL.field("id", Long.class).eq(id))
                       .fetchOptionalInto(Articulo.class);
               if (articuloSearch.isPresent()) {
                 Articulo articulo = articuloSearch.get();
                 articulo.setNombrearticulo(articulo.getNombrearticulo());
                 articulo.setDescriparticulo(articulo.getDescriparticulo());
                 dsl.update(ARTICULO_TABLE)
                         .set(DSL.field("nombrearticulo"), articuloFromBody .getNombrearticulo())
                         .set(DSL.field("descriparticulo"), articuloFromBody .getDescriparticulo())
                         .where(DSL.field("id").eq(articulo.getId()))
                         .execute();
                 list.add(articulo);
                 responseRest.getArticuloResponse().setListaarticulos(list);
                 responseRest.setMetadata("Respuesta ok", "00", "Articulo actualizado");
               } else {
                 responseRest.setMetadata("Respuesta nok", "-1", "Articulo no encontrado");
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