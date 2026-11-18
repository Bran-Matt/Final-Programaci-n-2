package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.dto.ReservaForm;
import com.columbiaviajesweb.columbia_springboot.dto.UsuarioForm;
import com.columbiaviajesweb.columbia_springboot.entity.*;
import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import com.columbiaviajesweb.columbia_springboot.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Gestión de clientes y reservas (vendedores y administradores).
 */
@Controller
@RequestMapping("/gestion")
public class GestionController {

    private final UsuarioService usuarioService;
    private final ReservaService reservaService;
    private final SucursalService sucursalService;
    private final VueloService vueloService;
    private final HotelService hotelService;

    public GestionController(UsuarioService usuarioService, ReservaService reservaService,
                             SucursalService sucursalService, VueloService vueloService, HotelService hotelService) {
        this.usuarioService = usuarioService;
        this.reservaService = reservaService;
        this.sucursalService = sucursalService;
        this.vueloService = vueloService;
        this.hotelService = hotelService;
    }

    // ==================== CLIENTES ====================

    @GetMapping("/clientes")
    public String clientes(Model model) {
        model.addAttribute("clientes", usuarioService.listarClientes());
        return "gestion/clientes";
    }

    @GetMapping("/clientes/nuevo")
    public String nuevoCliente(Model model) {
        return formularioCliente(new UsuarioForm(), "Nuevo cliente", model);
    }

    @GetMapping("/clientes/{id}/editar")
    public String editarCliente(@PathVariable Long id, Model model) {
        return formularioCliente(UsuarioForm.desde(usuarioService.buscarCliente(id)), "Editar cliente", model);
    }

    @PostMapping("/clientes/guardar")
    public String guardarCliente(@Valid @ModelAttribute("form") UsuarioForm form, BindingResult errores,
                                 Model model, RedirectAttributes flash) {
        form.setRol(Rol.CLIENTE); // desde acá solo se administran clientes
        if (!errores.hasErrors()) {
            try {
                usuarioService.guardar(form);
                flash.addFlashAttribute("exito", "Cliente guardado correctamente");
                return "redirect:/gestion/clientes";
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return formularioCliente(form, "Cliente", model);
    }

    @PostMapping("/clientes/{id}/eliminar")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes flash) {
        try {
            usuarioService.buscarCliente(id);
            usuarioService.eliminar(id);
            flash.addFlashAttribute("exito", "Cliente eliminado junto con sus reservas");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/gestion/clientes";
    }

    private String formularioCliente(UsuarioForm form, String titulo, Model model) {
        form.setRol(Rol.CLIENTE);
        model.addAttribute("form", form);
        model.addAttribute("titulo", titulo);
        model.addAttribute("rolEditable", false);
        model.addAttribute("accion", "/gestion/clientes/guardar");
        model.addAttribute("volver", "/gestion/clientes");
        model.addAttribute("sucursales", sucursalService.listar());
        return "usuario-form";
    }

    // ==================== RESERVAS ====================

    @GetMapping("/reservas")
    public String reservas(Model model) {
        model.addAttribute("reservas", reservaService.listar());
        return "gestion/reservas";
    }

    @GetMapping("/reservas/vuelo/nueva")
    public String nuevaReservaVuelo(@RequestParam(required = false) Long clienteId, Model model) {
        ReservaForm f = new ReservaForm();
        f.setClienteId(clienteId);
        return formularioVuelo(f, "Nueva reserva de vuelo", model);
    }

    @GetMapping("/reservas/hotel/nueva")
    public String nuevaReservaHotel(@RequestParam(required = false) Long clienteId, Model model) {
        ReservaForm f = new ReservaForm();
        f.setClienteId(clienteId);
        return formularioHotel(f, "Nueva reserva de hotel", model);
    }

    @GetMapping("/reservas/{id}/editar")
    public String editarReserva(@PathVariable Long id, Model model) {
        Reserva r = reservaService.buscar(id);
        ReservaForm f = ReservaForm.desde(r);
        return r instanceof ReservaVuelo
                ? formularioVuelo(f, "Editar reserva " + r.getCodigo(), model)
                : formularioHotel(f, "Editar reserva " + r.getCodigo(), model);
    }

    @PostMapping("/reservas/vuelo")
    public String guardarReservaVuelo(@ModelAttribute("form") ReservaForm form,
                                      @RequestAttribute("usuarioActual") Usuario actual,
                                      Model model, RedirectAttributes flash) {
        try {
            Reserva r = reservaService.guardarVuelo(form, actual);
            flash.addFlashAttribute("exito", "Reserva " + r.getCodigo() + " guardada");
            return "redirect:/gestion/reservas";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            return formularioVuelo(form, "Reserva de vuelo", model);
        }
    }

    @PostMapping("/reservas/hotel")
    public String guardarReservaHotel(@ModelAttribute("form") ReservaForm form,
                                      @RequestAttribute("usuarioActual") Usuario actual,
                                      Model model, RedirectAttributes flash) {
        try {
            Reserva r = reservaService.guardarHotel(form, actual);
            flash.addFlashAttribute("exito", "Reserva " + r.getCodigo() + " guardada");
            return "redirect:/gestion/reservas";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            return formularioHotel(form, "Reserva de hotel", model);
        }
    }

    @PostMapping("/reservas/{id}/eliminar")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes flash) {
        reservaService.eliminar(id);
        flash.addFlashAttribute("exito", "Reserva eliminada");
        return "redirect:/gestion/reservas";
    }

    private void comunes(ReservaForm form, String titulo, Model model) {
        model.addAttribute("form", form);
        model.addAttribute("titulo", titulo);
        model.addAttribute("clientes", usuarioService.listarClientes());
        model.addAttribute("vendedores", usuarioService.listarVendedores());
    }

    private String formularioVuelo(ReservaForm form, String titulo, Model model) {
        comunes(form, titulo, model);
        model.addAttribute("vuelos", vueloService.listar());
        model.addAttribute("clases", ClaseVuelo.values());
        return "gestion/reserva-vuelo-form";
    }

    private String formularioHotel(ReservaForm form, String titulo, Model model) {
        comunes(form, titulo, model);
        model.addAttribute("hoteles", hotelService.listar());
        model.addAttribute("regimenes", Regimen.values());
        return "gestion/reserva-hotel-form";
    }
}
