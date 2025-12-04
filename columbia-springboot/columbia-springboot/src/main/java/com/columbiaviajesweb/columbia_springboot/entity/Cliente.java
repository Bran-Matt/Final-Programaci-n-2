package com.columbiaviajesweb.columbia_springboot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * Entidad Cliente que hereda de Usuario
 * Tiene asignaciones a sucursal, hotel y vuelo
 */
@Entity
@Table(name = "clientes")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Cliente extends Usuario {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursalAsignada;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vuelo_id")
    private Vuelo vueloAsignado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotelAsignado;
    
    public Cliente() {
        super();
    }
    
    public Cliente(String nombre, String apellido, String codigoUsuario, 
                   String clave, String direccion, String email, String telefono,
                   Sucursal sucursal, Hotel hotel, Vuelo vuelo) {
        super(nombre, apellido, codigoUsuario, clave, "CLIENTE", direccion, email, telefono);
        this.sucursalAsignada = sucursal;
        this.hotelAsignado = hotel;
        this.vueloAsignado = vuelo;
    }
}