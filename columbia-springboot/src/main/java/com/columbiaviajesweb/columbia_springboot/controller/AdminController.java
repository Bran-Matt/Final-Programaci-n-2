package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajes.entity.Usuario;
import com.columbiaviajes.entity.Vendedor;
import com.columbiaviajes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Controlador para las funcionalidades del Administrador
 * Puede gestionar vendedores y clientes
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;

    /**
     * Dashboard principal del administrador
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.getRol().equals("ADMINISTRADOR")) {
            return "redirect:/auth/login";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Administrador - Dashboard");
        return "admin/dashboard";
    }

    // ==================== GESTIÓN DE VENDEDORES ====================
    
    @GetMapping("/vendedores")
    public String listarVendedores(Model model, HttpSession session) {
        verificarSesionAdmin(session);
        List<Usuario> vendedores = usuarioService.obtenerUsuariosPorRol("VENDEDOR");
        model.addAttribute("vendedores", vendedores);
        model.addAttribute("titulo", "Gestión de Vendedores");
        return "admin/vendedores";
    }

    /**
     * Formulario para agregar nuevo vendedor
     */
    @GetMapping("/vendedores/nuevo")
    public String mostrarFormularioVendedor(Model model, HttpSession session) {
        verificarSesionAdmin(session);
        model.addAttribute("vendedor", new Vendedor());
        model.addAttribute("titulo", "Nuevo Vendedor");
        return "admin/form-vendedor";
    }

    @PostMapping("/vendedores")
    public String agregarVendedor(@ModelAttribute Vendedor vendedor, HttpSession session) {
        verificarSesionAdmin(session);
        usuarioService.crearVendedor(
            vendedor.getNombre(),
            vendedor.getApellido(),
            vendedor.getCodigoUsuario(),
            vendedor.getClave(),
            vendedor.getDireccion(),
            vendedor.getEmail(),
            vendedor.getTelefono()
        );
        return "redirect:/admin/vendedores?exito=true";
    }

    // ==================== GESTIÓN DE CLIENTES ====================
    
    @GetMapping("/clientes")
    public String listarClientes(Model model, HttpSession session) {
        verificarSesionAdmin(session);
        List<Usuario> clientes = usuarioService.obtenerUsuariosPorRol("CLIENTE");
        model.addAttribute("clientes", clientes);
        model.addAttribute("titulo", "Gestión de Clientes");
        return "admin/clientes";
    }

    // ==================== MÉTODOS AUXILIARES ====================
    
    private void verificarSesionAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.getRol().equals("ADMINISTRADOR")) {
            throw new SecurityException("Acceso denegado");
        }
    }
}