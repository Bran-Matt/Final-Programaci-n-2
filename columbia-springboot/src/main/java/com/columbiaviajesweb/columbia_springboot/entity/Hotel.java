package com.columbiaviajesweb.columbia_springboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Entidad Hotel representa un hotel contratado por la cadena
 */
@Entity
@Table(name = "hoteles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", unique = true, nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;
    
    @Column(name = "ciudad", nullable = false, length = 100)
    private String ciudad;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "plazas_disponibles")
    private Integer plazasDisponibles;
    
    @Column(name = "precio")
    private Float precio;
    
    public Hotel(String nombre, String direccion, String ciudad, String telefono, 
                 Integer plazasDisponibles, Float precio) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.plazasDisponibles = plazasDisponibles;
        this.precio = precio;
    }
}