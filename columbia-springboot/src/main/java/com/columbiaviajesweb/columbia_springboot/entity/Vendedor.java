package com.columbiaviajesweb.columbia_springboot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * Entidad Vendedor que hereda de Usuario
 * Maneja facturación y gestión de clientes
 */
@Entity
@Table(name = "vendedores")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Vendedor extends Usuario {
    
    @Column(name = "facturacion")
    private Float facturacion = 0.0f;
    
    public Vendedor() {
        super();
    }
    
    public Vendedor(String nombre, String apellido, String codigoUsuario, 
                    String clave, String direccion, String email, String telefono) {
        super(nombre, apellido, codigoUsuario, clave, "VENDEDOR", direccion, email, telefono);
    }
    
    /**
     * Incrementa la facturación del vendedor
     */
    public void agregarFacturacion(Float monto) {
        if (this.facturacion == null) {
            this.facturacion = 0.0f;
        }
        this.facturacion += monto;
    }
}