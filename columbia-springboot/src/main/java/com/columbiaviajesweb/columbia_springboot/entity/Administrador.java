package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Administrador del sistema: administra todo.
 */
@Entity
@Table(name = "administradores")
public class Administrador extends Usuario {

    @Override public Rol getRol() { return Rol.ADMINISTRADOR; }
    @Override public String getPaginaInicio() { return "/panel"; }
    @Override public boolean puedeGestionarReservas() { return true; }
    @Override public boolean puedeAdministrar() { return true; }
    @Override public boolean puedeVerRanking() { return true; }
}
