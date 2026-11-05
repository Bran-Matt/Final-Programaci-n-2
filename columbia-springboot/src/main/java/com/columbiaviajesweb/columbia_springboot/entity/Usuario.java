package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase abstracta base de todos los usuarios del sistema (abstracción + herencia).
 * Cada subclase define polimórficamente su rol, su página de inicio y sus permisos.
 * Se persiste con la estrategia JOINED: una tabla "usuarios" y una tabla por subclase.
 */
@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código con el que el usuario inicia sesión (para un turista, su código de turista). */
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    /** Clave guardada como hash BCrypt, nunca en texto plano. */
    @Column(nullable = false, length = 100)
    private String clave;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellidos;

    @Column(length = 150)
    private String direccion;

    @Column(length = 100)
    private String email;

    @Column(length = 30)
    private String telefono;

    /** Rol del usuario. Lo resuelve cada subclase. */
    public abstract Rol getRol();

    /** Página a la que se lo redirige al iniciar sesión. */
    public abstract String getPaginaInicio();

    /** Puede ver sus propias reservas (solo consulta). */
    public boolean puedeVerMisReservas() { return false; }

    /** Puede administrar clientes y reservas. */
    public boolean puedeGestionarReservas() { return false; }

    /** Puede administrar sucursales, hoteles, vuelos y usuarios. */
    public boolean puedeAdministrar() { return false; }

    /** Puede ver el ranking de vendedores por facturación. */
    public boolean puedeVerRanking() { return false; }

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    public String getIniciales() {
        String a = nombre == null || nombre.isEmpty() ? "" : nombre.substring(0, 1);
        String b = apellidos == null || apellidos.isEmpty() ? "" : apellidos.substring(0, 1);
        return (a + b).toUpperCase();
    }
}
