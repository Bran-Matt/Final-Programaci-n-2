package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.service.HotelService;
import com.columbiaviajesweb.columbia_springboot.service.SucursalService;
import com.columbiaviajesweb.columbia_springboot.service.VueloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Consulta de vuelos, hoteles y sucursales (disponible para todos los usuarios).
 */
@Controller
public class CatalogoController {

    private final VueloService vueloService;
    private final HotelService hotelService;
    private final SucursalService sucursalService;

    public CatalogoController(VueloService vueloService, HotelService hotelService, SucursalService sucursalService) {
        this.vueloService = vueloService;
        this.hotelService = hotelService;
        this.sucursalService = sucursalService;
    }

    @GetMapping("/vuelos")
    public String vuelos(Model model) {
        model.addAttribute("vuelos", vueloService.listar());
        return "catalogo/vuelos";
    }

    @GetMapping("/hoteles")
    public String hoteles(Model model) {
        model.addAttribute("hoteles", hotelService.listar());
        return "catalogo/hoteles";
    }

    @GetMapping("/sucursales")
    public String sucursales(Model model) {
        model.addAttribute("sucursales", sucursalService.listar());
        return "catalogo/sucursales";
    }
}
