package com.inventarios.handler.atributo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.handler.atributo.response.AtributoResponseRest;
import com.inventarios.model.*;

import java.util.*;

import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateAtributoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    protected final static Table<Record> ATRIBUTO_TABLE = DSL.table("atributo");
    protected final static Table<Record> ATRIBUTOS_TABLE = DSL.table("atributos");
    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }
    protected abstract int getAtributoID(Atributo atributo);
    protected abstract void save(Atributo atributo, List<Atributos> atributosList);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        input.setHeaders(headers);
        AtributoResponseRest responseRest = new AtributoResponseRest();
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        List<Atributo> list = new ArrayList<>();
        List<Atributos> atributosList = new ArrayList<>();
        String output ="";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(input.getBody());
            String body = input.getBody();
            if (body != null && !body.isEmpty()) {
                logger.log(body);
                Atributo atributo = new Gson().fromJson(body, Atributo.class);
                if (atributo != null) {
                    logger.log("atributo = "+atributo);
                    Long responsableId = jsonNode.get("responsableId").asLong();
                    logger.log("responsableId = "+responsableId);
                    Long articuloId = jsonNode.get("articuloId").asLong();
                    logger.log("articuloId = "+articuloId);

                    Responsable responsable=new Responsable();
                    responsable.setId(responsableId);

                    Articulo articulo=new Articulo();
                    articulo.setId(articuloId);

                    atributo.setResponsable(responsable);
                    atributo.setArticulo(articulo);

                    atributo.getResponsable().setId(responsableId);
                    atributo.getArticulo().setId(articuloId);

                    long atributoID = getAtributoID(atributo);
                    atributo.setId(atributoID);

                    JsonNode atributosNode = jsonNode.get("atributos");
                    if (atributosNode != null && atributosNode.isArray()) {
                        for (JsonNode atributoNode : atributosNode) {
                            //atributoNode.get("atributoid").asText();
                            //logger.log("atributoNode.get(atributoid).asText() = " + atributoNode.get("atributoid")!=null?atributoNode.get("atributoid").asText():"");
                            Atributos atributos = new Atributos();
                            atributos.setAtributo(atributo);
                            atributos.setNombreatributo(atributoNode.get("nombreatributo")!=null?atributoNode.get("nombreatributo").asText():"");
                            atributosList.add(atributos);
                        }
                    }
                    atributo.setAtributos(atributosList);
                    save(atributo, atributosList);
                    list.add(atributo);
                    responseRest.getAtributoResponse().setListaatributos(list);
                    responseRest.setMetadata("Respuesta ok", "00", "Atributo guardado");
                }
                output = new Gson().toJson(responseRest);
            }
            return response.withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
                    .withStatusCode(500);
        }
    }
}