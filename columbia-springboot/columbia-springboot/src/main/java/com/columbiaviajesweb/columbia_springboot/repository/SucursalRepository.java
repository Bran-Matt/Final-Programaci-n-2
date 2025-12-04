package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajes.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    Optional<Sucursal> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}