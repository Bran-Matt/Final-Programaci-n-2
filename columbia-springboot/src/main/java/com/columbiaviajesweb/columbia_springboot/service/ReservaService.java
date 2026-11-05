package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajesweb.columbia_springboot.dto.ReservaForm;
import com.columbiaviajesweb.columbia_springboot.entity.*;
import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import com.columbiaviajesweb.columbia_springboot.repository.ReservaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Gestión de reservas de vuelos y hoteles, con control de plazas disponibles.
 */
@Service
@Transactional
public class ReservaService extends ServicioBase<Reserva> {

    private final ReservaRepository repo;
    private final UsuarioService usuarioService;
    private final VueloService vueloService;
    private final HotelService hotelService;

    public ReservaService(ReservaRepository repo, UsuarioService usuarioService,
                          VueloService vueloService, HotelService hotelService) {
        this.repo = repo;
        this.usuarioService = usuarioService;
        this.vueloService = vueloService;
        this.hotelService = hotelService;
    }

    @Override protected JpaRepository<Reserva, Long> repositorio() { return repo; }
    @Override protected String nombreEntidad() { return "la reserva"; }

    @Override
    public List<Reserva> listar() { return repo.findAllByOrderByFechaReservaDesc(); }

    public List<Reserva> ultimas() { return repo.findTop5ByOrderByFechaReservaDesc(); }

    public double facturacionTotal() { return repo.sumarImportes(); }

    /** Valida las reglas de cada tipo de reserva (polimorfismo con pattern matching). */
    @Override
    protected void validar(Reserva r) {
        if (r.getCliente() == null) throw new ReglaNegocioException("Elegí un cliente");
        if (r instanceof ReservaVuelo rv) {
            validarVuelo(rv);
        } else if (r instanceof ReservaHotel rh) {
            validarHotel(rh);
        }
    }

    private void validarVuelo(ReservaVuelo rv) {
        if (rv.getVuelo() == null || rv.getClase() == null) {
            throw new ReglaNegocioException("Elegí el vuelo y la clase");
        }
        if (plazasLibres(rv.getVuelo(), rv.getClase(), rv.getId()) <= 0) {
            throw new ReglaNegocioException("No quedan plazas en clase " + rv.getClase().getEtiqueta()
                    + " en el vuelo " + rv.getVuelo().getNumero());
        }
    }

    private void validarHotel(ReservaHotel rh) {
        if (rh.getHotel() == null || rh.getRegimen() == null) {
            throw new ReglaNegocioException("Elegí el hotel y el régimen");
        }
        if (rh.getFechaLlegada() == null || rh.getFechaPartida() == null) {
            throw new ReglaNegocioException("Indicá la fecha de llegada y de partida");
        }
        if (!rh.getFechaPartida().isAfter(rh.getFechaLlegada())) {
            throw new ReglaNegocioException("La fecha de partida debe ser posterior a la de llegada");
        }
        long ocupadas = repo.contarOcupacion(rh.getHotel(), rh.getFechaLlegada(), rh.getFechaPartida(),
                idONinguno(rh.getId()));
        if (ocupadas >= rh.getHotel().getPlazasDisponibles()) {
            throw new ReglaNegocioException("El hotel " + rh.getHotel().getNombre()
                    + " no tiene plazas libres para esas fechas");
        }
    }

    /** Plazas que quedan libres en un vuelo para una clase. */
    @Transactional(readOnly = true)
    public long plazasLibres(Vuelo vuelo, ClaseVuelo clase, Long excluirId) {
        return vuelo.getPlazas(clase) - repo.contarPasajes(vuelo, clase, idONinguno(excluirId));
    }

    @Transactional(readOnly = true)
    public long plazasLibres(Vuelo vuelo, String clase) {
        return plazasLibres(vuelo, ClaseVuelo.valueOf(clase), null);
    }

    /** Plazas del hotel libres para la noche de hoy. */
    @Transactional(readOnly = true)
    public long plazasLibresHoy(Hotel hotel) {
        LocalDate hoy = LocalDate.now();
        return hotel.getPlazasDisponibles() - repo.contarOcupacion(hotel, hoy, hoy.plusDays(1), -1L);
    }

    /**
     * Crea o modifica una reserva de vuelo.
     * @param actual usuario que realiza la operación; si es vendedor, se le asigna la venta
     */
    public Reserva guardarVuelo(ReservaForm f, Usuario actual) {
        ReservaVuelo r = f.getId() == null ? new ReservaVuelo() : (ReservaVuelo) buscar(f.getId());
        completarComunes(r, f, actual);
        r.setVuelo(f.getVueloId() == null ? null : vueloService.buscar(f.getVueloId()));
        r.setClase(f.getClase());
        return guardar(r);
    }

    /** Crea o modifica una reserva de hotel. */
    public Reserva guardarHotel(ReservaForm f, Usuario actual) {
        ReservaHotel r = f.getId() == null ? new ReservaHotel() : (ReservaHotel) buscar(f.getId());
        completarComunes(r, f, actual);
        r.setHotel(f.getHotelId() == null ? null : hotelService.buscar(f.getHotelId()));
        r.setRegimen(f.getRegimen());
        r.setFechaLlegada(f.getFechaLlegada());
        r.setFechaPartida(f.getFechaPartida());
        return guardar(r);
    }

    private void completarComunes(Reserva r, ReservaForm f, Usuario actual) {
        r.setCliente(f.getClienteId() == null ? null : usuarioService.buscarCliente(f.getClienteId()));
        if (actual instanceof Vendedor v) {
            if (r.getVendedor() == null) r.setVendedor(v);
        } else {
            r.setVendedor(f.getVendedorId() == null ? null : usuarioService.buscarVendedor(f.getVendedorId()));
        }
    }
}
