package com.inventarios.handler.comunes.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.comunes.response.ComunResponseRest;
import com.inventarios.model.Comun;
import java.util.*;
import com.inventarios.model.Grupo;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateComunAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> COMUN_TABLE = DSL.table("comun");
    protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
    final static Map<String, String> headers = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }

    protected abstract void save(Comun comun, Long grupoID);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        input.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        ComunResponseRest responseRest = new ComunResponseRest();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        List<Comun> list = new ArrayList<>();
        String output = "";
        String contentTypeHeader = input.getHeaders().get("Content-Type");
        logger.log("Content-Type: " + contentTypeHeader);
        try {


            String body = input.getBody();
            //String body = "{\"modelo\":\"AS\",\"marca\":\"AS\",\"nroserie\":\"1\",\"fechacompra\":\"1\",\"importe\":777,\"grupoId\":1,\"account\":55555}";
            logger.log("######################################### BODY ################################################");
            logger.log(body);
            logger.log("###############################################################################################");



            Comun comun = new Gson().fromJson(body, Comun.class);

            logger.log("debe llegar aquí 1 ya tenemos Comun");
            if (comun == null) {
                return response
                        .withBody("El cuerpo de la solicitud no contiene datos válidos para un comun")
                        .withStatusCode(400);
            }
            logger.log("debe llegar aquí 2");
            // Obtener el ID del grupo del objeto Comun
            //Long grupoId = comun.getGrupo().getId();


            logger.log("Comun: ");

            //if (comun != null) {
            logger.log("Comun.getId() = " + comun.getId());
            logger.log("Comun.getDescripcomun() = " + comun.getDescripcomun());
            logger.log("Comun.getDescripcortacomun() = " + comun.getDescripcortacomun());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);

            // Acceder a los datos del cuerpo de la solicitud
            String grupoId = jsonNode.get("grupoId").asText();

            logger.log("id from path grupoId --->  " + grupoId);
            Long grupoID = null;
            try {
                grupoID = Long.parseLong(grupoId);
                logger.log("ID del grupo: " + grupoID);
            } catch (NumberFormatException e) {
                return response
                        .withBody("Invalid id in path")
                        .withStatusCode(400);
            }
            /*String bodyparaque = "{\"modelo\":\"+comun.getModelo()+\",\"marca\":\"+comun.getMarca()+" +
                    "\",\"nroserie\":\"+comun.getNroserie()+\",\"fechacompra\":\"+comun.getFechacompra()+" +
                    "\",\"importe\":+comun.getImporte()+,\"grupoId\":1,\"account\":+comun.getAccount()}";
            logger.log("bodyparaque: ");
            logger.log(bodyparaque);*/
            DSLContext dsl = RDSConexion.getDSL();
            Optional<Grupo> grupoSearch = dsl.select()
                    .from(GRUPO_TABLE)
                    .where(DSL.field("id", Long.class).eq(grupoID))
                    .fetchOptionalInto(Grupo.class);
            if (!grupoSearch.isPresent()) {
                //Long grupoId = comun.getGrupo().getId();

                return response
                        .withBody("El grupo especificado no existe")
                        .withStatusCode(404);
            }
            //if (grupoSearch.isPresent()) {
            logger.log("grupoSearch.isPresent()");
            logger.log("Comun.getGrupo I  : "+comun.getGrupo());
            comun.setGrupo(grupoSearch.get());
            logger.log("Comun.getGrupo II : "+comun.getGrupo());
          /*byte[] compressedPicture = null;
          if (comun.getPicture() != null) {
            compressedPicture = Util.compressZLib(comun.getPicture());
          }*/
            logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
            save(comun, grupoID);
            logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
            list.add(comun);
            responseRest.getComunResponse().setListacomuns(list);
            responseRest.setMetadata("Respuesta ok", "00", "Comun guardado");
              /*} else {
                responseRest.setMetadata("Respuesta nok", "-1", "Comun no encontrado");
              }*/
            output = new Gson().toJson(responseRest);
            return response.withStatusCode(200)
                    .withBody(output);
            /*} else {
              return response
                      .withBody("Invalid group data in request body, comun is null")
                      .withStatusCode(400);
            }*/
        /*} else {

      }*/
        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al insertar");
            return response
                    .withBody(new Gson().toJson(responseRest))
                    .withStatusCode(500);
        }
    }

}

