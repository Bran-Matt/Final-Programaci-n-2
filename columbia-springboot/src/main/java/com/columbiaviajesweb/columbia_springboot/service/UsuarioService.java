package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajes.entity.*;
import com.columbiaviajes.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones de usuarios
 * Anotado con @Service para ser detectado como bean de Spring
 * @RequiredArgsConstructor de Lombok crea el constructor con dependencias
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    
    /**
     * Autentica un usuario mediante código y clave
     * @param codigo Código del usuario
     * @param clave Clave del usuario
     * @return Usuario autenticado o null si no es válido
     */
    public Usuario autenticarUsuario(String codigo, String clave) {
        return usuarioRepository.findByCodigoAndClave(codigo, clave)
                .orElse(null);
    }
    
    /**
     * Busca un usuario por su código único
     */
    public Optional<Usuario> buscarPorCodigo(String codigo) {
        return usuarioRepository.findByCodigoUsuario(codigo);
    }
    
    /**
     * Valida si un código de usuario ya existe
     */
    public boolean validarCodigoExistente(String codigo) {
        return usuarioRepository.existsByCodigoUsuario(codigo);
    }
    
    /**
     * Obtiene todos los usuarios de un rol específico
     */
    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }
    
    /**
     * Guarda un usuario en la base de datos
     */
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Obtiene vendedores ordenados por facturación (para dueño)
     */
    public List<Vendedor> obtenerVendedoresOrdenados() {
        return usuarioRepository.findVendedoresOrderByFacturacionDesc();
    }
    
    /**
     * Crea y guarda un nuevo dueño
     */
    public Duenio crearDuenio(String nombre, String apellido, String codigo, String clave, String direccion, String email, String telefono) {
        Duenio duenio = new Duenio(nombre, apellido, codigo, clave, direccion, email, telefono);
        return usuarioRepository.save(duenio);
    }
    
    /**
     * Crea y guarda un nuevo administrador
     */
    public Administrador crearAdministrador(String nombre, String apellido, String codigo, String clave, String direccion, String email, String telefono) {
        Administrador admin = new Administrador(nombre, apellido, codigo, clave, direccion, email, telefono);
        return usuarioRepository.save(admin);
    }
    
    /**
     * Crea y guarda un nuevo vendedor
     */
    public Vendedor crearVendedor(String nombre, String apellido, String codigo, String clave, String direccion, String email, String telefono) {
        Vendedor vendedor = new Vendedor(nombre, apellido, codigo, clave, direccion, email, telefono);
        return usuarioRepository.save(vendedor);
    }
    
    /**
     * Crea y guarda un nuevo cliente
     */
    public Cliente crearCliente(String nombre, String apellido, String codigo, String clave, String direccion, String email, String telefono, Sucursal sucursal, Hotel hotel, Vuelo vuelo) {
        Cliente cliente = new Cliente(nombre, apellido, codigo, clave, direccion, email, telefono, sucursal, hotel, vuelo);
        return usuarioRepository.save(cliente);
    }
}