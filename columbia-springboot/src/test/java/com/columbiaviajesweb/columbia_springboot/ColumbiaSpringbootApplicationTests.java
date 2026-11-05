package com.columbiaviajesweb.columbia_springboot;

import com.columbiaviajesweb.columbia_springboot.dto.RankingVendedor;
import com.columbiaviajesweb.columbia_springboot.dto.ReservaForm;
import com.columbiaviajesweb.columbia_springboot.entity.*;
import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import com.columbiaviajesweb.columbia_springboot.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración sobre una base H2 en memoria con los datos iniciales.
 */
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:pruebas;DB_CLOSE_DELAY=-1")
class ColumbiaSpringbootApplicationTests {

    @Autowired UsuarioService usuarioService;
    @Autowired ReservaService reservaService;
    @Autowired VueloService vueloService;
    @Autowired HotelService hotelService;

    @Test
    void cargaDatosInicialesYAutentica() {
        assertTrue(usuarioService.autenticar("admin", "1234").isPresent());
        assertTrue(usuarioService.autenticar("admin", "mal").isEmpty());
        Usuario duenio = usuarioService.autenticar("duenio", "1234").orElseThrow();
        assertInstanceOf(Duenio.class, duenio);
        assertEquals("/duenio/ranking", duenio.getPaginaInicio());
    }

    @Test
    void permisosPolimorficos() {
        assertTrue(new Cliente().puedeVerMisReservas());
        assertFalse(new Cliente().puedeGestionarReservas());
        assertTrue(new Vendedor().puedeGestionarReservas());
        assertFalse(new Vendedor().puedeAdministrar());
        assertTrue(new Administrador().puedeAdministrar());
        assertTrue(new Duenio().puedeVerRanking());
        assertFalse(new Duenio().puedeGestionarReservas());
        for (Rol r : Rol.values()) assertEquals(r, r.crearUsuario().getRol());
    }

    @Test
    void importePolimorficoDeReservas() {
        Vuelo vuelo = new Vuelo("X1", LocalDateTime.now(), "A", "B", 10, 8, 100, 300);
        ReservaVuelo rv = new ReservaVuelo();
        rv.setVuelo(vuelo);
        rv.setClase(ClaseVuelo.PRIMERA);

        Hotel hotel = new Hotel("HX", "H", "D", "C", "T", 5, 1000.0);
        ReservaHotel rh = new ReservaHotel();
        rh.setHotel(hotel);
        rh.setRegimen(Regimen.PENSION_COMPLETA);
        rh.setFechaLlegada(LocalDate.of(2026, 1, 1));
        rh.setFechaPartida(LocalDate.of(2026, 1, 4));

        List<Reserva> reservas = List.of(rv, rh);
        assertEquals(300, reservas.get(0).calcularImporte(), 0.001);
        assertEquals(3 * 1000 * 1.35, reservas.get(1).calcularImporte(), 0.001);
        assertEquals(2, vuelo.getPlazasPrimera());
    }

    @Test
    void rankingOrdenadoPorFacturacion() {
        List<RankingVendedor> ranking = usuarioService.rankingVendedores();
        assertFalse(ranking.isEmpty());
        for (int i = 1; i < ranking.size(); i++) {
            assertTrue(ranking.get(i - 1).facturado() >= ranking.get(i).facturado());
        }
    }

    @Test
    void noPermiteSobrevenderNiFechasInvalidas() {
        Vuelo chico = vueloService.guardar(new Vuelo("T01", LocalDateTime.now().plusDays(1), "A", "B", 2, 1, 10, 20));
        Cliente cliente = usuarioService.listarClientes().get(0);
        Vendedor vendedor = usuarioService.listarVendedores().get(0);

        ReservaForm f = new ReservaForm();
        f.setClienteId(cliente.getId());
        f.setVueloId(chico.getId());
        f.setClase(ClaseVuelo.TURISTA);
        reservaService.guardarVuelo(f, vendedor);
        assertThrows(ReglaNegocioException.class, () -> reservaService.guardarVuelo(f, vendedor));

        Hotel hotel = hotelService.listar().get(0);
        ReservaForm h = new ReservaForm();
        h.setClienteId(cliente.getId());
        h.setHotelId(hotel.getId());
        h.setFechaLlegada(LocalDate.now().plusDays(5));
        h.setFechaPartida(LocalDate.now().plusDays(5));
        assertThrows(ReglaNegocioException.class, () -> reservaService.guardarHotel(h, vendedor));
    }
}
