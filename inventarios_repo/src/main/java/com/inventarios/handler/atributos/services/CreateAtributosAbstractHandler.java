package com.inventarios.handler.atributos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.response.AtributosResponseRest;
import com.inventarios.model.Atributos;
import java.util.*;
import com.inventarios.model.Grupo;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateAtributosAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> ATRIBUTOS_TABLE = DSL.table("atributos");
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

    protected abstract void save(Atributos atributos, Long grupoID);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        input.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        AtributosResponseRest responseRest = new AtributosResponseRest();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        List<Atributos> list = new ArrayList<>();
        String output = "";
        String contentTypeHeader = input.getHeaders().get("Content-Type");
        logger.log("Content-Type: " + contentTypeHeader);
        try {


            String body = input.getBody();
            //String body = "{\"modelo\":\"AS\",\"marca\":\"AS\",\"nroserie\":\"1\",\"fechacompra\":\"1\",\"importe\":777,\"grupoId\":1,\"account\":55555}";
            logger.log("######################################### BODY ################################################");
            logger.log(body);
            logger.log("###############################################################################################");



            Atributos atributos = new Gson().fromJson(body, Atributos.class);

            logger.log("debe llegar aquí 1 ya tenemos Atributos");
            if (atributos == null) {
                return response
                        .withBody("El cuerpo de la solicitud no contiene datos válidos para un atributos")
                        .withStatusCode(400);
            }
            logger.log("debe llegar aquí 2");
            // Obtener el ID del grupo del objeto Atributos
            //Long grupoId = atributos.getGrupo().getId();


            logger.log("Atributos: ");

            //if (atributos != null) {
            logger.log("Atributos.getId() = " + atributos.getId());
            //logger.log("Atributos.getDescripatributo() = " + atributos.getDescripatributo());
            logger.log("Atributos.getNombreatributo() = " + atributos.getNombreatributo());

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
            /*String bodyparaque = "{\"modelo\":\"+atributos.getModelo()+\",\"marca\":\"+atributos.getMarca()+" +
                    "\",\"nroserie\":\"+atributos.getNroserie()+\",\"fechacompra\":\"+atributos.getFechacompra()+" +
                    "\",\"importe\":+atributos.getImporte()+,\"grupoId\":1,\"account\":+atributos.getAccount()}";
            logger.log("bodyparaque: ");
            logger.log(bodyparaque);*/
            DSLContext dsl = RDSConexion.getDSL();
            Optional<Grupo> grupoSearch = dsl.select()
                    .from(GRUPO_TABLE)
                    .where(DSL.field("id", Long.class).eq(grupoID))
                    .fetchOptionalInto(Grupo.class);
            if (!grupoSearch.isPresent()) {
                //Long grupoId = atributos.getGrupo().getId();

                return response
                        .withBody("El grupo especificado no existe")
                        .withStatusCode(404);
            }
            //if (grupoSearch.isPresent()) {
            logger.log("grupoSearch.isPresent()");
        /*    logger.log("Atributos.getGrupo I  : "+atributos.getGrupo());
            atributos.setGrupo(grupoSearch.get());
            logger.log("Atributos.getGrupo II : "+atributos.getGrupo());*/
          /*byte[] compressedPicture = null;
          if (atributos.getPicture() != null) {
            compressedPicture = Util.compressZLib(atributos.getPicture());
          }*/
            logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
            save(atributos, grupoID);
            logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
            list.add(atributos);
            responseRest.getAtributosResponse().setListaatributoss(list);
            responseRest.setMetadata("Respuesta ok", "00", "Atributos guardado");
              /*} else {
                responseRest.setMetadata("Respuesta nok", "-1", "Atributos no encontrado");
              }*/
            output = new Gson().toJson(responseRest);
            return response.withStatusCode(200)
                    .withBody(output);
            /*} else {
              return response
                      .withBody("Invalid group data in request body, atributos is null")
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

