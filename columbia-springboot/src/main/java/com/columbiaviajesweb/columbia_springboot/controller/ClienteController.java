package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajes.entity.Cliente;
import com.columbiaviajes.entity.Usuario;
import com.columbiaviajes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;

/**
 * Controlador para las funcionalidades del Cliente
 * Solo puede consultar su información
 */
@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final UsuarioService usuarioService;

    /**
     * Dashboard principal del cliente
     * Muestra toda la información asignada al cliente
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.getRol().equals("CLIENTE")) {
            return "redirect:/auth/login";
        }

        // Obtener el cliente completo con sus asignaciones
        Cliente cliente = (Cliente) usuarioService.buscarPorCodigo(usuario.getCodigoUsuario())
                .orElse(null);
                
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Cliente - Mi Información");
        return "cliente/dashboard";
    }
}