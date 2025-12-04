package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajes.entity.Usuario;
import com.columbiaviajes.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario
 * Spring Data JPA implementa automáticamente los métodos básicos CRUD
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca usuario por código y clave (para login)
     * @param codigo Código del usuario
     * @param clave Clave del usuario
     * @return Optional con el usuario si existe
     */
    @Query("SELECT u FROM Usuario u WHERE u.codigoUsuario = :codigo AND u.clave = :clave")
    Optional<Usuario> findByCodigoAndClave(@Param("codigo") String codigo, @Param("clave") String clave);
    
    /**
     * Busca usuario por código único
     */
    Optional<Usuario> findByCodigoUsuario(String codigoUsuario);
    
    /**
     * Lista usuarios por rol específico
     */
    List<Usuario> findByRol(String rol);
    
    /**
     * Verifica si existe un usuario con el código
     */
    boolean existsByCodigoUsuario(String codigoUsuario);
    
    /**
     * Obtiene vendedores ordenados por facturación descendente
     */
    @Query("SELECT v FROM Vendedor v ORDER BY v.facturacion DESC")
    List<Vendedor> findVendedoresOrderByFacturacionDesc();
}