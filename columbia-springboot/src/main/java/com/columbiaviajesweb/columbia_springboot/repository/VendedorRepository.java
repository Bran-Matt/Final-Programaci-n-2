package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajesweb.columbia_springboot.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    List<Vendedor> findAllByOrderByApellidosAscNombreAsc();
}
