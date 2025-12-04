package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajes.entity.Usuario;
import com.columbiaviajes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

/**
 * Controlador para autenticación y login
 * Maneja el inicio y cierre de sesión
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UsuarioService usuarioService;
    
    /**
     * Muestra la página de login
     */
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("titulo", "Columbia Viajes - Login");
        return "auth/login";
    }
    
    /**
     * Procesa el login del usuario
     */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String codigo, 
                               @RequestParam String clave,
                               HttpSession session,
                               Model model) {
        
        Usuario usuario = usuarioService.autenticarUsuario(codigo, clave);
        
        if (usuario != null) {
            session.setAttribute("usuario", usuario);
            
            // Redirigir según el rol
            return switch (usuario.getRol()) {
                case "DUENIO" -> "redirect:/duenio/dashboard";
                case "ADMINISTRADOR" -> "redirect:/admin/dashboard";
                case "VENDEDOR" -> "redirect:/vendedor/dashboard";
                case "CLIENTE" -> "redirect:/cliente/dashboard";
                default -> "redirect:/auth/login?error=true";
            };
        } else {
            model.addAttribute("error", "Código o clave incorrectos");
            return "auth/login";
        }
    }
    
    /**
     * Cierra la sesión del usuario
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}