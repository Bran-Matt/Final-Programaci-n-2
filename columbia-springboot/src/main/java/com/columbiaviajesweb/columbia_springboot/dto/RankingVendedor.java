package com.columbiaviajesweb.columbia_springboot.dto;

import com.columbiaviajesweb.columbia_springboot.entity.Vendedor;

/**
 * Fila del listado de vendedores ordenado por facturación.
 * @param posicion puesto en el ranking (1 = el que más facturó)
 * @param porcentaje facturación relativa al primero (0 a 100), para la barra
 */
public record RankingVendedor(int posicion, Vendedor vendedor, double facturado, int reservas, double porcentaje) { }
