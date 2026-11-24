package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Turista que contrata los servicios de la cadena. Solo puede consultar su información.
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
public class Cliente extends Usuario {

    /** Sucursal que contrató el turista. */
    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.REMOVE)
    @OrderBy("fechaReserva DESC")
    private List<Reserva> reservas = new ArrayList<>();

    @Override public Rol getRol() { return Rol.CLIENTE; }
    @Override public String getPaginaInicio() { return "/cliente/mis-reservas"; }
    @Override public boolean puedeVerMisReservas() { return true; }

    /** Total gastado por el turista en todas sus reservas. */
    public double getTotalGastado() {
        return reservas.stream().mapToDouble(Reserva::getImporte).sum();
    }
}
