package com.inventarios.util;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SqlDateDeserializer implements JsonDeserializer<Date> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Date deserialize(JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws JsonParseException {
        try {
            return new Date(DATE_FORMAT.parse(json.getAsString()).getTime());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}
