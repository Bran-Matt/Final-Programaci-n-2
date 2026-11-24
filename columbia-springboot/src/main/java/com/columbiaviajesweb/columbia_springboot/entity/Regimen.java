package com.columbiaviajesweb.columbia_springboot.entity;

/**
 * Régimen de hospedaje. Cada régimen aplica un factor sobre el precio por noche del hotel.
 */
public enum Regimen {
    MEDIA_PENSION("Media pensión", 1.0),
    PENSION_COMPLETA("Pensión completa", 1.35);

    private final String etiqueta;
    private final double factorPrecio;

    Regimen(String etiqueta, double factorPrecio) {
        this.etiqueta = etiqueta;
        this.factorPrecio = factorPrecio;
    }

    public String getEtiqueta() { return etiqueta; }
    public double getFactorPrecio() { return factorPrecio; }
}
