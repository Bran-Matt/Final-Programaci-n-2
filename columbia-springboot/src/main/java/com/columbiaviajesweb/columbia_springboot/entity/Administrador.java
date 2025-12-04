package com.columbiaviajesweb.columbia_springboot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * Entidad Administrador que hereda de Usuario
 * Puede gestionar vendedores y clientes pero no dueños
 */
@Entity
@Table(name = "administradores")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Administrador extends Usuario {
    
    public Administrador() {
        super();
    }
    
    public Administrador(String nombre, String apellido, String codigoUsuario, 
                         String clave, String direccion, String email, String telefono) {
        super(nombre, apellido, codigoUsuario, clave, "ADMINISTRADOR", direccion, email, telefono);
    }
}