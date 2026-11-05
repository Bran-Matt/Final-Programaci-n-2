package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.entity.Vuelo;
import com.columbiaviajesweb.columbia_springboot.service.VueloService;
import com.columbiaviajesweb.columbia_springboot.service.ServicioBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** Alta, baja y modificación de vuelos (solo administradores). */
@Controller
@RequestMapping("/admin/vuelos")
public class AdminVuelosController extends CrudController<Vuelo> {

    private final VueloService vueloService;

    public AdminVuelosController(VueloService vueloService) { this.vueloService = vueloService; }

    @Override protected ServicioBase<Vuelo> servicio() { return vueloService; }
    @Override protected Vuelo nueva() { return new Vuelo(); }
    @Override protected String nombre() { return "Vuelo"; }
    @Override protected String vistaFormulario() { return "admin/vuelo-form"; }
    @Override protected String urlListado() { return "/vuelos"; }
}
