package com.columbiaviajesweb.columbia_springboot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * Entidad Dueño que hereda de Usuario
 * Representa al dueño de la cadena de agencias
 * Puede realizar todas las operaciones administrativas
 */
@Entity
@Table(name = "duenios")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Duenio extends Usuario {
    
    /**
     * Constructor por defecto requerido por JPA
     */
    public Duenio() {
        super();
    }
    
    /**
     * Constructor completo para crear un Dueño
     */
    public Duenio(String nombre, String apellido, String codigoUsuario, 
                  String clave, String direccion, String email, String telefono) {
        super(nombre, apellido, codigoUsuario, clave, "DUENIO", direccion, email, telefono);
    }
}