package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Dueño de la cadena: consulta el listado de vendedores ordenado por facturación.
 */
@Entity
@Table(name = "duenios")
public class Duenio extends Usuario {

    @Override public Rol getRol() { return Rol.DUENIO; }
    @Override public String getPaginaInicio() { return "/duenio/ranking"; }
    @Override public boolean puedeVerRanking() { return true; }
}
