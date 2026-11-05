package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.dto.UsuarioForm;
import com.columbiaviajesweb.columbia_springboot.entity.Rol;
import com.columbiaviajesweb.columbia_springboot.entity.Usuario;
import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import com.columbiaviajesweb.columbia_springboot.service.SucursalService;
import com.columbiaviajesweb.columbia_springboot.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Administración de usuarios de todos los roles (solo administradores).
 */
@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

    private final UsuarioService usuarioService;
    private final SucursalService sucursalService;

    public AdminUsuariosController(UsuarioService usuarioService, SucursalService sucursalService) {
        this.usuarioService = usuarioService;
        this.sucursalService = sucursalService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        return "admin/usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        return formulario(new UsuarioForm(), "Nuevo usuario", model);
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        return formulario(UsuarioForm.desde(usuarioService.buscar(id)), "Editar usuario", model);
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") UsuarioForm form, BindingResult errores,
                          Model model, RedirectAttributes flash) {
        if (!errores.hasErrors()) {
            try {
                usuarioService.guardar(form);
                flash.addFlashAttribute("exito", "Usuario guardado correctamente");
                return "redirect:/admin/usuarios";
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return formulario(form, "Usuario", model);
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, @RequestAttribute("usuarioActual") Usuario actual,
                           RedirectAttributes flash) {
        try {
            usuarioService.eliminar(id, actual);
            flash.addFlashAttribute("exito", "Usuario eliminado");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    private String formulario(UsuarioForm form, String titulo, Model model) {
        model.addAttribute("form", form);
        model.addAttribute("titulo", titulo);
        model.addAttribute("roles", Rol.values());
        model.addAttribute("rolEditable", true);
        model.addAttribute("accion", "/admin/usuarios/guardar");
        model.addAttribute("volver", "/admin/usuarios");
        model.addAttribute("sucursales", sucursalService.listar());
        return "usuario-form";
    }
}
