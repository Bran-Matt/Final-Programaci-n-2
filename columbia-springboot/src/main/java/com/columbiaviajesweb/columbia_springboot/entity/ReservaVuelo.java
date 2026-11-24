package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

/**
 * Reserva de un pasaje en un vuelo, en clase turista o primera.
 */
@Entity
@DiscriminatorValue("VUELO")
@Getter
@Setter
public class ReservaVuelo extends Reserva {

    @ManyToOne
    @JoinColumn(name = "vuelo_id")
    private Vuelo vuelo;

    @Enumerated(EnumType.STRING)
    @Column(name = "clase", length = 10)
    private ClaseVuelo clase;

    @Override
    public double calcularImporte() {
        return vuelo.getPrecio(clase);
    }

    @Override public String getTipo() { return "Vuelo"; }
    @Override public String getIcono() { return "bi-airplane"; }

    @Override
    public String getDescripcion() {
        return "Vuelo " + vuelo.getNumero() + " · " + vuelo.getRuta();
    }

    @Override
    public String getDetalle() {
        return "Clase " + clase.getEtiqueta() + " · "
                + vuelo.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
