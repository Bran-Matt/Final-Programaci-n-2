package com.columbiaviajesweb.columbia_springboot.config;

import com.columbiaviajesweb.columbia_springboot.entity.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Pone al usuario logueado a disposición de todas las vistas.
 */
@ControllerAdvice
public class ModeloGlobal {

    @ModelAttribute("usuarioActual")
    public Usuario usuarioActual(HttpServletRequest request) {
        return (Usuario) request.getAttribute(SesionInterceptor.ATRIBUTO_USUARIO);
    }

    @ModelAttribute("rutaActual")
    public String rutaActual(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /** Muestra una página amable cuando una regla de negocio falla fuera de un formulario. */
    @ExceptionHandler(ReglaNegocioException.class)
    public String reglaNegocio(ReglaNegocioException e, HttpServletRequest request, Model model) {
        model.addAttribute("usuarioActual", usuarioActual(request));
        model.addAttribute("rutaActual", rutaActual(request));
        model.addAttribute("mensaje", e.getMessage());
        return "acceso-denegado";
    }
}
