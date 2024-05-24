package com.inventarios.handler.activos.services;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.response.ActivoResponseRest;
import com.inventarios.model.*;
import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> ACTIVO_TABLE = DSL.table("activo");
    protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
    protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
    protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
    protected final static Table<Record> ARTICULO_TABLE = DSL.table("articulo");

    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }

    protected abstract void save(Activo activo, Long responsableID, Long tipoID, Long grupoID, Long articuloID);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        input.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        ActivoResponseRest responseRest = new ActivoResponseRest();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        List<Activo> list = new ArrayList<>();
        String output = "";
        String contentTypeHeader = input.getHeaders().get("Content-Type");
        logger.log("Content-Type: " + contentTypeHeader);
        try {
            String body = input.getBody();
            //String body ="{\"modelo\":\"sygfd\",\"marca\":\"fdsfdsa\",\"nroserie\":\"fas4343432\",\"fechacompra\":\"2024-10-05\",\"importe\":24,\"account\":2,\"responsableId\":1, \"grupoId\": 1, \"tipoId\": 1, \"articuloId\": 2 }";
            //String body = "{\"modelo\":\"AS\",\"marca\":\"AS\",\"nroserie\":\"1\",\"fechacompra\":\"1\",\"importe\":777,\"grupoId\":1,\"account\":55555}";
            logger.log("######################################### BODY ################################################");
            logger.log(body);
            logger.log("###############################################################################################");

            //PostgreSQL utiliza el formato yyyy-MM-dd
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Activo activo = gson.fromJson(body, Activo.class);

            /*Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new SqlDateDeserializer())
                    .create();
            //Activo activo = new Gson().fromJson(body, Activo.class);
            Activo activo = gson.fromJson(body, Activo.class);*/

            logger.log("debe llegar aquí 1 ya tenemos Activo");
            if (activo == null) {
                return response
                        .withBody("El cuerpo de la solicitud no contiene datos válidos para un activo")
                        .withStatusCode(400);
            }
            logger.log("debe llegar aquí 2");
            logger.log("Activo: ");
            //if (activo != null) {
            logger.log("Activo.getId() = " + activo.getId());
            logger.log("Activo.getCodinventario() = " + activo.getCodinventario());
            logger.log("Activo.getModelo() = " + activo.getModelo());
            logger.log("Activo.getMarca() = " + activo.getMarca());
            logger.log("Activo.getNroserie() = " + activo.getNroserie());
            //logger.log("Activo = " + activo.getGrupo().getId());
            //logger.log("Activo = " + activo.getGrupo().getNombregrupo());
            //logger.log("Activo = " + activo.getGrupo().getDescripgrupo());
            logger.log("Activo.getFechaIngreso() = " + activo.getFechaingreso());
            logger.log("Activo.getMoneda() = " + activo.getMoneda());
            logger.log("Activo.getImporte() = " + activo.getImporte());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);

            String responsableId = jsonNode.get("responsableId").asText();
            String tipoId = jsonNode.get("tipoId").asText();
            String grupoId = jsonNode.get("grupoId").asText();
            String articuloId = jsonNode.get("articuloId").asText();
            Long responsableID = null;
            Long tipoID = null;
            Long grupoID = null;
            Long articuloID = null;
            try {
                responsableID = Long.parseLong(responsableId);
                tipoID = Long.parseLong(tipoId);
                grupoID = Long.parseLong(grupoId);
                articuloID = Long.parseLong(articuloId);
            } catch (NumberFormatException e) {
                return response
                        .withBody("Invalid id in path")
                        .withStatusCode(400);
            }
            DSLContext dsl = RDSConexion.getDSL();

            Optional<Responsable> responsableSearch = dsl.select()
                    .from(RESPONSABLE_TABLE)
                    .where(DSL.field("id", Long.class).eq(responsableID))
                    .fetchOptionalInto(Responsable.class);
            if (!responsableSearch.isPresent()) {
                return response
                        .withBody("El responsable especificado no existe")
                        .withStatusCode(404);
            }

            Optional<Tipo> tipoSearch = dsl.select()
                    .from(TIPO_TABLE)
                    .where(DSL.field("id", Long.class).eq(tipoID))
                    .fetchOptionalInto(Tipo.class);
            if (!tipoSearch.isPresent()) {
                return response
                        .withBody("El tipo especificado no existe")
                        .withStatusCode(404);
            }

            Optional<Grupo> grupoSearch = dsl.select()
                    .from(GRUPO_TABLE)
                    .where(DSL.field("id", Long.class).eq(grupoID))
                    .fetchOptionalInto(Grupo.class);
            if (!grupoSearch.isPresent()) {
                return response
                        .withBody("El grupo especificado no existe")
                        .withStatusCode(404);
            }

            Optional<Articulo> articuloSearch = dsl.select()
                    .from(ARTICULO_TABLE)
                    .where(DSL.field("id", Long.class).eq(articuloID))
                    .fetchOptionalInto(Articulo.class);
            if (!articuloSearch.isPresent()) {
                return response
                        .withBody("El articulo especificado no existe")
                        .withStatusCode(404);
            }

            activo.setResponsable(responsableSearch.get());
            activo.setTipo(tipoSearch.get());
            activo.setGrupo(grupoSearch.get());
            activo.setArticulo(articuloSearch.get());

            logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
            save(activo, responsableID, tipoID, grupoID, articuloID);
            logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
            list.add(activo);
            responseRest.getActivoResponse().setListaactivos(list);
            responseRest.setMetadata("Respuesta ok", "00", "Activo guardado");

            output = new Gson().toJson(responseRest);
            return response.withStatusCode(200)
                    .withBody(output);

        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al insertar");
            return response
                    .withBody(new Gson().toJson(responseRest))
                    .withStatusCode(500);
        }
    }

}

