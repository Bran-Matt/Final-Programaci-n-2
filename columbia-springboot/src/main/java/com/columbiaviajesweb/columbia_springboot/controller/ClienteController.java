package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.entity.Cliente;
import com.columbiaviajesweb.columbia_springboot.entity.Usuario;
import com.columbiaviajesweb.columbia_springboot.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Sección del cliente: solo consulta sus datos y sus reservas.
 */
@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final UsuarioService usuarioService;

    public ClienteController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/mis-reservas")
    public String misReservas(@RequestAttribute("usuarioActual") Usuario actual, Model model) {
        Cliente cliente = usuarioService.buscarCliente(actual.getId());
        model.addAttribute("cliente", cliente);
        return "cliente/mis-reservas";
    }
}
