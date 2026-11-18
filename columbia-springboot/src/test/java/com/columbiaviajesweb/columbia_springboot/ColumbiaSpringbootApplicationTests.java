package com.columbiaviajesweb.columbia_springboot;

import com.columbiaviajesweb.columbia_springboot.dto.RankingVendedor;
import com.columbiaviajesweb.columbia_springboot.dto.ReservaForm;
import com.columbiaviajesweb.columbia_springboot.dto.UsuarioForm;
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
    void cambioDeRolDeUnUsuario() {
        UsuarioForm f = new UsuarioForm();
        f.setRol(Rol.ADMINISTRADOR);
        f.setCodigo("temporal");
        f.setClave("abcd");
        f.setNombre("Tomi");
        f.setApellidos("Prueba");
        Usuario creado = usuarioService.guardar(f);

        // Sin reservas: pasa a ser vendedor y conserva la clave
        UsuarioForm edicion = UsuarioForm.desde(creado);
        edicion.setRol(Rol.VENDEDOR);
        Usuario cambiado = usuarioService.guardar(edicion, null);
        assertInstanceOf(Vendedor.class, cambiado);
        assertInstanceOf(Vendedor.class, usuarioService.autenticar("temporal", "abcd").orElseThrow());

        // Con reservas no se permite
        Usuario cliente = usuarioService.autenticar("cliente1", "1234").orElseThrow();
        UsuarioForm conReservas = UsuarioForm.desde(cliente);
        conReservas.setRol(Rol.VENDEDOR);
        assertThrows(ReglaNegocioException.class, () -> usuarioService.guardar(conReservas, null));

        // Un vendedor con ventas pasa a administrador; sus reservas quedan sin vendedor
        double facturacionAntes = reservaService.facturacionTotal();
        Usuario vendedor = usuarioService.autenticar("vendedor3", "1234").orElseThrow();
        UsuarioForm ascenso = UsuarioForm.desde(vendedor);
        ascenso.setRol(Rol.ADMINISTRADOR);
        assertInstanceOf(Administrador.class, usuarioService.guardar(ascenso, null));
        assertInstanceOf(Administrador.class, usuarioService.autenticar("vendedor3", "1234").orElseThrow());
        assertEquals(facturacionAntes, reservaService.facturacionTotal(), 0.01);

        // Nadie puede cambiar su propio rol
        UsuarioForm propio = UsuarioForm.desde(cambiado);
        propio.setRol(Rol.CLIENTE);
        assertThrows(ReglaNegocioException.class, () -> usuarioService.guardar(propio, cambiado));
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
