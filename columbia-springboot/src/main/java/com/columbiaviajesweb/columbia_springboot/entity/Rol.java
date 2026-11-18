package com.columbiaviajesweb.columbia_springboot.entity;

/**
 * Tipos de usuario que pueden acceder al sistema.
 * Cada rol sabe crear una instancia vacía de su clase concreta (patrón fábrica).
 */
public enum Rol {
    CLIENTE("Cliente", "bi-person") {
        @Override public Usuario crearUsuario() { return new Cliente(); }
    },
    VENDEDOR("Vendedor", "bi-briefcase") {
        @Override public Usuario crearUsuario() { return new Vendedor(); }
    },
    ADMINISTRADOR("Administrador", "bi-shield-lock") {
        @Override public Usuario crearUsuario() { return new Administrador(); }
    },
    DUENIO("Dueño", "bi-gem") {
        @Override public Usuario crearUsuario() { return new Duenio(); }
    };

    private final String etiqueta;
    private final String icono;

    Rol(String etiqueta, String icono) {
        this.etiqueta = etiqueta;
        this.icono = icono;
    }

    /** Crea un usuario vacío del tipo correspondiente a este rol. */
    public abstract Usuario crearUsuario();

    public String getEtiqueta() { return etiqueta; }
    public String getIcono() { return icono; }
}
