package com.columbiaviajesweb.columbia_springboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Entidad Sucursal representa una sucursal de la cadena de agencias
 */
@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo", unique = true, nullable = false, length = 10)
    private String codigo;
    
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    /**
     * Constructor para crear sucursales sin ID
     */
    public Sucursal(String codigo, String direccion, String email, String telefono) {
        this.codigo = codigo;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
    }
}