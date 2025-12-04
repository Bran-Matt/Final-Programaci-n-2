package com.columbiaviajesweb.columbia_springboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Entidad base Usuario que representa a todos los usuarios del sistema
 * Utiliza herencia con la estrategia JOINED para crear tablas separadas
 * Lombok genera automáticamente getters, setters, constructores, etc.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;
    
    @Column(name = "codigo_usuario", unique = true, nullable = false, length = 50)
    private String codigoUsuario;
    
    @Column(name = "clave", nullable = false, length = 100)
    private String clave;
    
    @Column(name = "rol", nullable = false, length = 20)
    private String rol; // DUENIO, ADMINISTRADOR, VENDEDOR, CLIENTE
    
    @Column(name = "direccion", length = 200)
    private String direccion;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    /**
     * Constructor para clases herederas
     */
    public Usuario(String nombre, String apellido, String codigoUsuario, 
                   String clave, String rol, String direccion, String email, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.codigoUsuario = codigoUsuario;
        this.clave = clave;
        this.rol = rol;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
    }
}