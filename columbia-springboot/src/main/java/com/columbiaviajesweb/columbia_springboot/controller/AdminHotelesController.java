package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.entity.Hotel;
import com.columbiaviajesweb.columbia_springboot.service.HotelService;
import com.columbiaviajesweb.columbia_springboot.service.ServicioBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** Alta, baja y modificación de hoteles (solo administradores). */
@Controller
@RequestMapping("/admin/hoteles")
public class AdminHotelesController extends CrudController<Hotel> {

    private final HotelService hotelService;

    public AdminHotelesController(HotelService hotelService) { this.hotelService = hotelService; }

    @Override protected ServicioBase<Hotel> servicio() { return hotelService; }
    @Override protected Hotel nueva() { return new Hotel(); }
    @Override protected String nombre() { return "Hotel"; }
    @Override protected String vistaFormulario() { return "admin/hotel-form"; }
    @Override protected String urlListado() { return "/hoteles"; }
}
