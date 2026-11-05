package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.config.SesionInterceptor;
import com.columbiaviajesweb.columbia_springboot.entity.Usuario;
import com.columbiaviajesweb.columbia_springboot.repository.UsuarioRepository;
import com.columbiaviajesweb.columbia_springboot.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Inicio y cierre de sesión.
 */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String inicio(HttpSession session) {
        Long id = (Long) session.getAttribute(SesionInterceptor.ATRIBUTO_SESION);
        if (id != null) {
            Optional<Usuario> u = usuarioRepository.findById(id);
            if (u.isPresent()) return "redirect:" + u.get().getPaginaInicio();
        }
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String codigo, @RequestParam String clave,
                        HttpSession session, Model model) {
        Optional<Usuario> usuario = usuarioService.autenticar(codigo, clave);
        if (usuario.isEmpty()) {
            model.addAttribute("error", "Código o clave incorrectos");
            model.addAttribute("codigo", codigo);
            return "index";
        }
        session.setAttribute(SesionInterceptor.ATRIBUTO_SESION, usuario.get().getId());
        // Polimorfismo: cada tipo de usuario sabe cuál es su página de inicio
        return "redirect:" + usuario.get().getPaginaInicio();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }
}
