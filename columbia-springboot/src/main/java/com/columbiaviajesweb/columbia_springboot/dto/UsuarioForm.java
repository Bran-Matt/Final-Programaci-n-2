package com.columbiaviajesweb.columbia_springboot.dto;

import com.columbiaviajesweb.columbia_springboot.entity.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Datos del formulario de alta/edición de un usuario de cualquier rol.
 */
@Getter
@Setter
public class UsuarioForm {

    private Long id;

    @NotNull(message = "Elegí un rol")
    private Rol rol = Rol.CLIENTE;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20)
    private String codigo;

    /** Vacía al editar significa "no cambiar la clave". */
    @Size(max = 60)
    private String clave;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    private String direccion;

    @Email(message = "E-mail inválido")
    private String email;

    private String telefono;

    private Long sucursalId;

    public static UsuarioForm desde(Usuario u) {
        UsuarioForm f = new UsuarioForm();
        f.id = u.getId();
        f.rol = u.getRol();
        f.codigo = u.getCodigo();
        f.nombre = u.getNombre();
        f.apellidos = u.getApellidos();
        f.direccion = u.getDireccion();
        f.email = u.getEmail();
        f.telefono = u.getTelefono();
        if (u instanceof Cliente c && c.getSucursal() != null) f.sucursalId = c.getSucursal().getId();
        if (u instanceof Vendedor v && v.getSucursal() != null) f.sucursalId = v.getSucursal().getId();
        return f;
    }
}
