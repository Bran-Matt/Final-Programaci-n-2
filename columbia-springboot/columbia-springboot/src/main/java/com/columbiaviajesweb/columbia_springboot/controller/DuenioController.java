package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajes.entity.*;
import com.columbiaviajes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Controlador para las funcionalidades del Dueño
 * El dueño tiene acceso completo al sistema
 */
@Controller
@RequestMapping("/duenio")
@RequiredArgsConstructor
public class DuenioController {

    private final UsuarioService usuarioService;
    private final SucursalService sucursalService;
    private final VueloService vueloService;
    private final HotelService hotelService;

    /**
     * Dashboard principal del dueño
     * Muestra el menú con todas las opciones disponibles
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.getRol().equals("DUENIO")) {
            return "redirect:/auth/login";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Dueño - Dashboard");
        return "duenio/dashboard";
    }

    // ==================== GESTIÓN DE SUCURSALES ====================
    
    /**
     * Muestra listado de sucursales
     */
    @GetMapping("/sucursales")
    public String listarSucursales(Model model, HttpSession session) {
        verificarSesionDuenio(session);
        model.addAttribute("sucursales", sucursalService.obtenerTodas());
        model.addAttribute("sucursal", new Sucursal());
        model.addAttribute("titulo", "Gestión de Sucursales");
        return "duenio/sucursales";
    }

    /**
     * Agrega nueva sucursal
     */
    @PostMapping("/sucursales")
    public String agregarSucursal(@ModelAttribute Sucursal sucursal, HttpSession session) {
        verificarSesionDuenio(session);
        sucursalService.guardarSucursal(sucursal);
        return "redirect:/duenio/sucursales?exito=true";
    }

    // ==================== GESTIÓN DE VUELOS ====================
    
    @GetMapping("/vuelos")
    public String listarVuelos(Model model, HttpSession session) {
        verificarSesionDuenio(session);
        model.addAttribute("vuelos", vueloService.obtenerTodos());
        model.addAttribute("vuelo", new Vuelo());
        model.addAttribute("titulo", "Gestión de Vuelos");
        return "duenio/vuelos";
    }

    @PostMapping("/vuelos")
    public String agregarVuelo(@ModelAttribute Vuelo vuelo, HttpSession session) {
        verificarSesionDuenio(session);
        vueloService.guardarVuelo(vuelo);
        return "redirect:/duenio/vuelos?exito=true";
    }

    // ==================== GESTIÓN DE HOTELES ====================
    
    @GetMapping("/hoteles")
    public String listarHoteles(Model model, HttpSession session) {
        verificarSesionDuenio(session);
        model.addAttribute("hoteles", hotelService.obtenerTodos());
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("titulo", "Gestión de Hoteles");
        return "duenio/hoteles";
    }

    @PostMapping("/hoteles")
    public String agregarHotel(@ModelAttribute Hotel hotel, HttpSession session) {
        verificarSesionDuenio(session);
        hotelService.guardarHotel(hotel);
        return "redirect:/duenio/hoteles?exito=true";
    }

    // ==================== GESTIÓN DE VENDEDORES ====================
    
    /**
     * Muestra vendedores ordenados por facturación
     */
    @GetMapping("/vendedores")
    public String listarVendedores(Model model, HttpSession session) {
        verificarSesionDuenio(session);
        List<Vendedor> vendedores = usuarioService.obtenerVendedoresOrdenados();
        model.addAttribute("vendedores", vendedores);
        model.addAttribute("titulo", "Vendedores - Ordenados por Facturación");
        return "duenio/vendedores";
    }

    // ==================== GESTIÓN DE ADMINISTRADORES ====================
    
    @GetMapping("/administradores")
    public String listarAdministradores(Model model, HttpSession session) {
        verificarSesionDuenio(session);
        List<Usuario> administradores = usuarioService.obtenerUsuariosPorRol("ADMINISTRADOR");
        model.addAttribute("administradores", administradores);
        model.addAttribute("titulo", "Gestión de Administradores");
        return "duenio/administradores";
    }

    /**
     * Formulario para agregar nuevo administrador
     */
    @GetMapping("/administradores/nuevo")
    public String mostrarFormularioAdministrador(Model model, HttpSession session) {
        verificarSesionDuenio(session);
        model.addAttribute("administrador", new Administrador());
        model.addAttribute("titulo", "Nuevo Administrador");
        return "duenio/form-administrador";
    }

    @PostMapping("/administradores")
    public String agregarAdministrador(@ModelAttribute Administrador administrador, HttpSession session) {
        verificarSesionDuenio(session);
        usuarioService.crearAdministrador(
            administrador.getNombre(),
            administrador.getApellido(),
            administrador.getCodigoUsuario(),
            administrador.getClave(),
            administrador.getDireccion(),
            administrador.getEmail(),
            administrador.getTelefono()
        );
        return "redirect:/duenio/administradores?exito=true";
    }

    // ==================== MÉTODOS AUXILIARES ====================
    
    /**
     * Verifica que el usuario en sesión sea un dueño
     */
    private void verificarSesionDuenio(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.getRol().equals("DUENIO")) {
            throw new SecurityException("Acceso denegado");
        }
    }
}