package com.columbiaviajesweb.columbia_springboot.config;

import com.columbiaviajesweb.columbia_springboot.entity.Usuario;
import com.columbiaviajesweb.columbia_springboot.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.function.Predicate;

/**
 * Controla el acceso: exige sesión iniciada y verifica, según la sección,
 * el permiso correspondiente del usuario (que cada subclase resuelve polimórficamente).
 */
@Component
public class SesionInterceptor implements HandlerInterceptor {

    public static final String ATRIBUTO_SESION = "usuarioId";
    public static final String ATRIBUTO_USUARIO = "usuarioActual";

    private final UsuarioRepository usuarioRepository;

    public SesionInterceptor(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        Long id = session == null ? null : (Long) session.getAttribute(ATRIBUTO_SESION);
        Usuario usuario = id == null ? null : usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
        request.setAttribute(ATRIBUTO_USUARIO, usuario);

        if (!permisoPara(request.getRequestURI().substring(request.getContextPath().length())).test(usuario)) {
            response.sendRedirect(request.getContextPath() + "/acceso-denegado");
            return false;
        }
        return true;
    }

    private Predicate<Usuario> permisoPara(String ruta) {
        if (ruta.startsWith("/cliente")) return Usuario::puedeVerMisReservas;
        if (ruta.startsWith("/gestion") || ruta.startsWith("/panel")) return Usuario::puedeGestionarReservas;
        if (ruta.startsWith("/admin")) return Usuario::puedeAdministrar;
        if (ruta.startsWith("/duenio")) return Usuario::puedeVerRanking;
        return u -> true;
    }
}
