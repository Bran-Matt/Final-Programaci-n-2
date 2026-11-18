package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.entity.Sucursal;
import com.columbiaviajesweb.columbia_springboot.service.SucursalService;
import com.columbiaviajesweb.columbia_springboot.service.ServicioBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** Alta, baja y modificación de sucursales (solo administradores). */
@Controller
@RequestMapping("/admin/sucursales")
public class AdminSucursalesController extends CrudController<Sucursal> {

    private final SucursalService sucursalService;

    public AdminSucursalesController(SucursalService sucursalService) { this.sucursalService = sucursalService; }

    @Override protected ServicioBase<Sucursal> servicio() { return sucursalService; }
    @Override protected Sucursal nueva() { return new Sucursal(); }
    @Override protected boolean femenino() { return true; }
    @Override protected String nombre() { return "Sucursal"; }
    @Override protected String vistaFormulario() { return "admin/sucursal-form"; }
    @Override protected String urlListado() { return "/sucursales"; }
}
