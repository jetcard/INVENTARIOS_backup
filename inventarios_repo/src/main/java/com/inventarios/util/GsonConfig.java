package com.inventarios.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Date;

public class GsonConfig {
    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new SqlDateDeserializer())
                .create();
    }
}
