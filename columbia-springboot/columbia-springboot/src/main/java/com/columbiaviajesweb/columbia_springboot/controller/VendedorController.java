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
 * Controlador para las funcionalidades del Vendedor
 * Puede gestionar clientes y realizar ventas
 */
@Controller
@RequestMapping("/vendedor")
@RequiredArgsConstructor
public class VendedorController {

    private final UsuarioService usuarioService;
    private final SucursalService sucursalService;
    private final HotelService hotelService;
    private final VueloService vueloService;

    /**
     * Dashboard principal del vendedor
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.getRol().equals("VENDEDOR")) {
            return "redirect:/auth/login";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Vendedor - Dashboard");
        return "vendedor/dashboard";
    }

    // ==================== GESTIÓN DE CLIENTES ====================
    
    @GetMapping("/clientes")
    public String listarClientes(Model model, HttpSession session) {
        verificarSesionVendedor(session);
        List<Usuario> clientes = usuarioService.obtenerUsuariosPorRol("CLIENTE");
        model.addAttribute("clientes", clientes);
        model.addAttribute("titulo", "Gestión de Clientes");
        return "vendedor/clientes";
    }

    /**
     * Formulario para agregar nuevo cliente
     */
    @GetMapping("/clientes/nuevo")
    public String mostrarFormularioCliente(Model model, HttpSession session) {
        verificarSesionVendedor(session);
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("sucursales", sucursalService.obtenerTodas());
        model.addAttribute("hoteles", hotelService.obtenerTodos());
        model.addAttribute("vuelos", vueloService.obtenerTodos());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "vendedor/form-cliente";
    }

    @PostMapping("/clientes")
    public String agregarCliente(@ModelAttribute Cliente cliente, 
                                @RequestParam Long sucursalId,
                                @RequestParam Long hotelId,
                                @RequestParam Long vueloId,
                                HttpSession session) {
        verificarSesionVendedor(session);
        
        Sucursal sucursal = sucursalService.obtenerTodas().stream()
                .filter(s -> s.getId().equals(sucursalId))
                .findFirst()
                .orElse(null);
                
        Hotel hotel = hotelService.obtenerTodos().stream()
                .filter(h -> h.getId().equals(hotelId))
                .findFirst()
                .orElse(null);
                
        Vuelo vuelo = vueloService.obtenerTodos().stream()
                .filter(v -> v.getId().equals(vueloId))
                .findFirst()
                .orElse(null);

        if (sucursal != null && hotel != null && vuelo != null) {
            usuarioService.crearCliente(
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getCodigoUsuario(),
                cliente.getClave(),
                cliente.getDireccion(),
                cliente.getEmail(),
                cliente.getTelefono(),
                sucursal,
                hotel,
                vuelo
            );
            
            // Actualizar facturación del vendedor
            Vendedor vendedor = (Vendedor) session.getAttribute("usuario");
            Float facturacion = vuelo.getPrecio() + (hotel.getPrecio() / 3);
            vendedor.agregarFacturacion(facturacion);
            usuarioService.guardarUsuario(vendedor);
            session.setAttribute("usuario", vendedor);
        }
        
        return "redirect:/vendedor/clientes?exito=true";
    }

    // ==================== MÉTODOS AUXILIARES ====================
    
    private void verificarSesionVendedor(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.getRol().equals("VENDEDOR")) {
            throw new SecurityException("Acceso denegado");
        }
    }
}