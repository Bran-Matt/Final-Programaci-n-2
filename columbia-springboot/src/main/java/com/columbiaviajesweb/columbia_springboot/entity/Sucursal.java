package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Sucursal de la cadena de agencias.
 */
@Entity
@Table(name = "sucursales")
@Getter
@Setter
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 10)
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false, length = 150)
    private String direccion;

    @Email(message = "E-mail inválido")
    @Column(length = 100)
    private String email;

    @Column(length = 30)
    private String telefono;

    public Sucursal() { }

    public Sucursal(String codigo, String direccion, String email, String telefono) {
        this.codigo = codigo;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
    }
}
