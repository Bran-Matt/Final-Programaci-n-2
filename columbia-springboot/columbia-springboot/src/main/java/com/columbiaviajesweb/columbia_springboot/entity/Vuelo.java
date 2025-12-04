package com.columbiaviajesweb.columbia_springboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Entidad Vuelo representa un vuelo regular contratado por la cadena
 */
@Entity
@Table(name = "vuelos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vuelo implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_vuelo", unique = true, nullable = false)
    private Integer numeroVuelo;
    
    @Column(name = "fecha", length = 50)
    private String fecha;
    
    @Column(name = "origen", length = 100)
    private String origen;
    
    @Column(name = "destino", length = 100)
    private String destino;
    
    @Column(name = "plazas_totales")
    private Integer plazasTotales;
    
    @Column(name = "plazas_turista")
    private Integer plazasTurista;
    
    @Column(name = "precio")
    private Float precio;
    
    public Vuelo(Integer numeroVuelo, String fecha, String origen, String destino, 
                 Integer plazasTotales, Integer plazasTurista, Float precio) {
        this.numeroVuelo = numeroVuelo;
        this.fecha = fecha;
        this.origen = origen;
        this.destino = destino;
        this.plazasTotales = plazasTotales;
        this.plazasTurista = plazasTurista;
        this.precio = precio;
    }
}