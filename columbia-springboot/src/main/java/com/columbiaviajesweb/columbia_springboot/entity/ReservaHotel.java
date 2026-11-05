package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Reserva de hospedaje en un hotel, con régimen y fechas de llegada y partida.
 */
@Entity
@DiscriminatorValue("HOTEL")
@Getter
@Setter
public class ReservaHotel extends Reserva {

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(name = "regimen", length = 20)
    private Regimen regimen;

    @Column(name = "fecha_llegada")
    private LocalDate fechaLlegada;

    @Column(name = "fecha_partida")
    private LocalDate fechaPartida;

    public long getNoches() {
        return ChronoUnit.DAYS.between(fechaLlegada, fechaPartida);
    }

    @Override
    public double calcularImporte() {
        return getNoches() * hotel.getPrecioNoche() * regimen.getFactorPrecio();
    }

    @Override public String getTipo() { return "Hotel"; }
    @Override public String getIcono() { return "bi-building"; }

    @Override
    public String getDescripcion() {
        return hotel.getNombre() + " · " + hotel.getCiudad();
    }

    @Override
    public String getDetalle() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return regimen.getEtiqueta() + " · " + fechaLlegada.format(f) + " al " + fechaPartida.format(f)
                + " (" + getNoches() + " noches)";
    }
}
