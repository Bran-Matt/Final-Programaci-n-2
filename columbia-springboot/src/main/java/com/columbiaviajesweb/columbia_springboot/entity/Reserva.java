package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Reserva abstracta que un vendedor hace para un turista.
 * Cada tipo concreto (vuelo u hotel) calcula su importe de forma polimórfica.
 * Se persiste en una sola tabla con una columna "tipo" que distingue la subclase.
 */
@Entity
@Table(name = "reservas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Getter
@Setter
public abstract class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /** Vendedor que hizo la venta (a quien se le factura). */
    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @Column(nullable = false)
    private LocalDateTime fechaReserva;

    /** Importe congelado al momento de guardar la reserva. */
    @Column(nullable = false)
    private double importe;

    /** Calcula el importe según el tipo de reserva. */
    public abstract double calcularImporte();

    /** Nombre del tipo de reserva para mostrar. */
    public abstract String getTipo();

    /** Ícono Bootstrap del tipo de reserva. */
    public abstract String getIcono();

    /** Descripción corta de lo reservado. */
    public abstract String getDescripcion();

    /** Detalle adicional (clase, régimen, fechas...). */
    public abstract String getDetalle();

    @PrePersist
    @PreUpdate
    void antesDeGuardar() {
        if (fechaReserva == null) {
            fechaReserva = LocalDateTime.now();
        }
        importe = calcularImporte();
    }

    public String getCodigo() {
        return String.format("R-%05d", id == null ? 0 : id);
    }
}
