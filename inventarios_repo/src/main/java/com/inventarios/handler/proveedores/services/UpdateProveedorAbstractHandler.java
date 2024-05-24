package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import com.inventarios.model.Proveedor;
import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class UpdateProveedorAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }
  protected abstract void update(Proveedor proveedor, Long id);
   @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
     input.setHeaders(headers);
     ProveedorResponseRest responseRest = new ProveedorResponseRest();
     APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
     List<Proveedor> list = new ArrayList<>();
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
         // Convertir el cuerpo de la solicitud a un objeto Proveedor
         Proveedor proveedorFromBody = new Gson().fromJson(body, Proveedor.class);
         // Verificar si el objeto Proveedor obtenido del cuerpo de la solicitud es válido
         if (proveedorFromBody != null) {
               DSLContext dsl = RDSConexion.getDSL();
               Optional<Proveedor> proveedoresearch = dsl.select()
                       .from(PROVEEDOR_TABLE)
                       .where(DSL.field("id", Long.class).eq(id))
                       .fetchOptionalInto(Proveedor.class);
               if (proveedoresearch.isPresent()) {
                 Proveedor proveedor = proveedoresearch.get();
                 proveedor.setNombreproveedor(proveedor.getNombreproveedor());
                 proveedor.setDescripproveedor(proveedor.getDescripproveedor());
                 dsl.update(PROVEEDOR_TABLE)
                         .set(DSL.field("nombreproveedor"), proveedorFromBody .getNombreproveedor())
                         .set(DSL.field("descripproveedor"), proveedorFromBody .getDescripproveedor())
                         .where(DSL.field("id").eq(proveedor.getId()))
                         .execute();
                 list.add(proveedor);
                 responseRest.getProveedorResponse().setListaproveedores(list);
                 responseRest.setMetadata("Respuesta ok", "00", "Proveedor actualizado");
               } else {
                 responseRest.setMetadata("Respuesta nok", "-1", "Proveedor no encontrado");
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