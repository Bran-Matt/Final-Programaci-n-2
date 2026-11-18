package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajesweb.columbia_springboot.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);

    List<Sucursal> findAllByOrderByCodigoAsc();
}
