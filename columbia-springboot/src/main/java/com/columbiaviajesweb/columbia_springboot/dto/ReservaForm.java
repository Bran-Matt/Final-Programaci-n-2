package com.columbiaviajesweb.columbia_springboot.dto;

import com.columbiaviajesweb.columbia_springboot.entity.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Datos del formulario de reserva. Sirve para los dos tipos (vuelo y hotel).
 */
@Getter
@Setter
public class ReservaForm {

    private Long id;
    private Long clienteId;
    private Long vendedorId;

    // Vuelo
    private Long vueloId;
    private ClaseVuelo clase = ClaseVuelo.TURISTA;

    // Hotel
    private Long hotelId;
    private Regimen regimen = Regimen.MEDIA_PENSION;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaLlegada;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaPartida;

    public static ReservaForm desde(Reserva r) {
        ReservaForm f = new ReservaForm();
        f.id = r.getId();
        f.clienteId = r.getCliente().getId();
        f.vendedorId = r.getVendedor() == null ? null : r.getVendedor().getId();
        if (r instanceof ReservaVuelo rv) {
            f.vueloId = rv.getVuelo().getId();
            f.clase = rv.getClase();
        } else if (r instanceof ReservaHotel rh) {
            f.hotelId = rh.getHotel().getId();
            f.regimen = rh.getRegimen();
            f.fechaLlegada = rh.getFechaLlegada();
            f.fechaPartida = rh.getFechaPartida();
        }
        return f;
    }
}
