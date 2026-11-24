package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajesweb.columbia_springboot.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findAllByOrderByApellidosAscNombreAsc();
}
