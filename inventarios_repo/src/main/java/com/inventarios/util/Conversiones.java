package com.inventarios.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Conversiones {
    public LocalDate convertirALocalDate(String fechaCompraStr) {
        LocalDate fecha = LocalDate.parse(fechaCompraStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return fecha;
    }
}
