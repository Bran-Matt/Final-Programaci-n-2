package com.columbiaviajesweb.columbia_springboot.config;

import com.columbiaviajesweb.columbia_springboot.dto.ReservaForm;
import com.columbiaviajesweb.columbia_springboot.entity.*;
import com.columbiaviajesweb.columbia_springboot.repository.*;
import com.columbiaviajesweb.columbia_springboot.service.ReservaService;
import com.columbiaviajesweb.columbia_springboot.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Carga datos de ejemplo la primera vez que arranca la aplicación (base vacía).
 * Todos los usuarios de prueba tienen la clave 1234.
 */
@Component
public class DatosIniciales implements CommandLineRunner {

    private final UsuarioService usuarios;
    private final ReservaService reservas;
    private final UsuarioRepository usuarioRepo;
    private final SucursalRepository sucursalRepo;
    private final HotelRepository hotelRepo;
    private final VueloRepository vueloRepo;

    public DatosIniciales(UsuarioService usuarios, ReservaService reservas, UsuarioRepository usuarioRepo,
                          SucursalRepository sucursalRepo, HotelRepository hotelRepo, VueloRepository vueloRepo) {
        this.usuarios = usuarios;
        this.reservas = reservas;
        this.usuarioRepo = usuarioRepo;
        this.sucursalRepo = sucursalRepo;
        this.hotelRepo = hotelRepo;
        this.vueloRepo = vueloRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepo.count() > 0) return;

        Sucursal centro = sucursalRepo.save(new Sucursal("SUC01", "Av. Corrientes 1234, CABA", "centro@columbiaviajes.com.ar", "011 4321-0001"));
        Sucursal belgrano = sucursalRepo.save(new Sucursal("SUC02", "Av. Cabildo 2050, CABA", "belgrano@columbiaviajes.com.ar", "011 4321-0002"));
        Sucursal cordoba = sucursalRepo.save(new Sucursal("SUC03", "Bv. San Juan 450, Córdoba", "cordoba@columbiaviajes.com.ar", "0351 422-0003"));

        Hotel h1 = hotelRepo.save(new Hotel("H001", "Llao Llao Resort", "Av. Bustillo km 25", "Bariloche", "0294 444-8530", 40, 180000.0));
        Hotel h2 = hotelRepo.save(new Hotel("H002", "Costa Galana", "Bv. Peralta Ramos 5725", "Mar del Plata", "0223 410-5000", 60, 95000.0));
        Hotel h3 = hotelRepo.save(new Hotel("H003", "Hotel Patios de San Telmo", "Chacabuco 752", "Buenos Aires", "011 4307-0480", 25, 70000.0));
        Hotel h4 = hotelRepo.save(new Hotel("H004", "Cataratas Lodge", "Ruta 12 km 4", "Puerto Iguazú", "03757 49-1800", 30, 120000.0));
        hotelRepo.save(new Hotel("H005", "Mendoza Wine Suites", "San Martín 1020", "Mendoza", "0261 425-1100", 20, 85000.0));

        LocalDateTime base = LocalDate.now().plusDays(10).atTime(8, 30);
        Vuelo v1 = vueloRepo.save(new Vuelo("CV101", base, "Buenos Aires", "Bariloche", 180, 160, 145000, 320000));
        Vuelo v2 = vueloRepo.save(new Vuelo("CV202", base.plusDays(3).withHour(14), "Buenos Aires", "Iguazú", 150, 135, 120000, 260000));
        Vuelo v3 = vueloRepo.save(new Vuelo("CV303", base.plusDays(7).withHour(19), "Córdoba", "Mendoza", 90, 80, 80000, 175000));
        vueloRepo.save(new Vuelo("CV404", base.plusDays(15).withHour(6), "Buenos Aires", "Mar del Plata", 120, 110, 60000, 130000));

        usuarios.crear(new Duenio(), "duenio", "1234", "Diego", "Columbia", "Av. Libertador 100", "duenio@columbiaviajes.com.ar", "011 5555-0000");
        usuarios.crear(new Administrador(), "admin", "1234", "Ana", "Gómez", "Florida 300", "admin@columbiaviajes.com.ar", "011 5555-0001");

        Vendedor ven1 = vendedor("vendedor1", "Lucía", "Fernández", centro);
        Vendedor ven2 = vendedor("vendedor2", "Martín", "López", belgrano);
        Vendedor ven3 = vendedor("vendedor3", "Sofía", "Ramírez", cordoba);

        Cliente c1 = cliente("cliente1", "Juan", "Pérez", centro);
        Cliente c2 = cliente("cliente2", "María", "González", belgrano);
        Cliente c3 = cliente("cliente3", "Carlos", "Díaz", cordoba);
        Cliente c4 = cliente("cliente4", "Valentina", "Torres", centro);

        LocalDate llegada = base.toLocalDate();
        vuelo(c1, ven1, v1, ClaseVuelo.PRIMERA);
        hotel(c1, ven1, h1, Regimen.PENSION_COMPLETA, llegada, llegada.plusDays(5));
        vuelo(c2, ven2, v2, ClaseVuelo.TURISTA);
        hotel(c2, ven2, h4, Regimen.MEDIA_PENSION, llegada.plusDays(3), llegada.plusDays(6));
        vuelo(c3, ven3, v3, ClaseVuelo.TURISTA);
        hotel(c4, ven1, h2, Regimen.MEDIA_PENSION, llegada.plusDays(15), llegada.plusDays(19));
        hotel(c3, ven2, h3, Regimen.MEDIA_PENSION, llegada.plusDays(20), llegada.plusDays(22));
    }

    private Vendedor vendedor(String codigo, String nombre, String apellidos, Sucursal s) {
        Vendedor v = new Vendedor();
        v.setSucursal(s);
        return usuarios.crear(v, codigo, "1234", nombre, apellidos, "Calle Falsa 123",
                codigo + "@columbiaviajes.com.ar", "011 6000-0000");
    }

    private Cliente cliente(String codigo, String nombre, String apellidos, Sucursal s) {
        Cliente c = new Cliente();
        c.setSucursal(s);
        return usuarios.crear(c, codigo, "1234", nombre, apellidos, "Av. Siempreviva 742",
                nombre.toLowerCase() + "@mail.com", "11 5000-0000");
    }

    private void vuelo(Cliente c, Vendedor v, Vuelo vuelo, ClaseVuelo clase) {
        ReservaForm f = new ReservaForm();
        f.setClienteId(c.getId());
        f.setVueloId(vuelo.getId());
        f.setClase(clase);
        reservas.guardarVuelo(f, v);
    }

    private void hotel(Cliente c, Vendedor v, Hotel h, Regimen r, LocalDate llegada, LocalDate partida) {
        ReservaForm f = new ReservaForm();
        f.setClienteId(c.getId());
        f.setHotelId(h.getId());
        f.setRegimen(r);
        f.setFechaLlegada(llegada);
        f.setFechaPartida(partida);
        reservas.guardarHotel(f, v);
    }
}
