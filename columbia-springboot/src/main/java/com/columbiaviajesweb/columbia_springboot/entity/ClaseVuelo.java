package com.columbiaviajesweb.columbia_springboot.entity;

/** Clase en la que viaja el turista. */
public enum ClaseVuelo {
    TURISTA("Turista"),
    PRIMERA("Primera");

    private final String etiqueta;

    ClaseVuelo(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }
}
