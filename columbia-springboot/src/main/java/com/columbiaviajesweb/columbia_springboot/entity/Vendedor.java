package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Vendedor de una sucursal. Administra clientes y reservas, y factura por cada reserva que hace.
 */
@Entity
@Table(name = "vendedores")
@Getter
@Setter
public class Vendedor extends Usuario {

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @OneToMany(mappedBy = "vendedor")
    private List<Reserva> reservas = new ArrayList<>();

    @Override public Rol getRol() { return Rol.VENDEDOR; }
    @Override public String getPaginaInicio() { return "/panel"; }
    @Override public boolean puedeGestionarReservas() { return true; }

    /** Total facturado: suma polimórfica del importe de cada reserva vendida. */
    public double getTotalFacturado() {
        return reservas.stream().mapToDouble(Reserva::getImporte).sum();
    }

    public int getCantidadReservas() {
        return reservas.size();
    }
}
