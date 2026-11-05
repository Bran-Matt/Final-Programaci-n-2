package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import com.columbiaviajesweb.columbia_springboot.service.ServicioBase;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador genérico de alta, modificación y baja (abstracción + herencia).
 * Los controladores de sucursales, hoteles y vuelos heredan de acá
 * y solo indican su servicio, sus nombres y cómo crear una entidad vacía.
 *
 * @param <T> tipo de entidad que administra
 */
public abstract class CrudController<T> {

    protected abstract ServicioBase<T> servicio();

    /** Crea una entidad vacía para el formulario de alta. */
    protected abstract T nueva();

    /** Nombre de la entidad para títulos y mensajes ("Hotel"). */
    protected abstract String nombre();

    /** Plantilla del formulario ("admin/hotel-form"). */
    protected abstract String vistaFormulario();

    /** URL del listado al que se vuelve después de guardar ("/hoteles"). */
    protected abstract String urlListado();

    /** Indica si el nombre es femenino, para concordar los mensajes ("guardada"/"guardado"). */
    protected boolean femenino() { return false; }

    private String o() { return femenino() ? "a" : "o"; }

    /** Datos extra que necesite el formulario (listas para selects, etc.). */
    protected void completarFormulario(Model model) { }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("entidad", nueva());
        model.addAttribute("titulo", "Nuev" + o() + " " + nombre().toLowerCase());
        completarFormulario(model);
        return vistaFormulario();
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("entidad", servicio().buscar(id));
        model.addAttribute("titulo", "Editar " + nombre().toLowerCase());
        completarFormulario(model);
        return vistaFormulario();
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("entidad") T entidad, BindingResult errores,
                          Model model, RedirectAttributes flash) {
        if (!errores.hasErrors()) {
            try {
                servicio().guardar(entidad);
                flash.addFlashAttribute("exito", nombre() + " guardad" + o() + " correctamente");
                return "redirect:" + urlListado();
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        model.addAttribute("titulo", nombre());
        completarFormulario(model);
        return vistaFormulario();
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            servicio().eliminar(id);
            flash.addFlashAttribute("exito", nombre() + " eliminad" + o());
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + urlListado();
    }
}
