package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Panel principal con indicadores, para vendedores y administradores.
 */
@Controller
public class PanelController {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;
    private final VueloService vueloService;
    private final HotelService hotelService;

    public PanelController(ReservaService reservaService, UsuarioService usuarioService,
                           VueloService vueloService, HotelService hotelService) {
        this.reservaService = reservaService;
        this.usuarioService = usuarioService;
        this.vueloService = vueloService;
        this.hotelService = hotelService;
    }

    @GetMapping("/panel")
    public String panel(Model model) {
        model.addAttribute("totalReservas", reservaService.contar());
        model.addAttribute("totalClientes", usuarioService.listarClientes().size());
        model.addAttribute("totalVuelos", vueloService.contar());
        model.addAttribute("totalHoteles", hotelService.contar());
        model.addAttribute("facturacion", reservaService.facturacionTotal());
        model.addAttribute("ultimasReservas", reservaService.ultimas());
        model.addAttribute("proximosVuelos", vueloService.proximos());
        return "panel";
    }
}
