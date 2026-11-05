package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajesweb.columbia_springboot.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);

    List<Hotel> findAllByOrderByCiudadAscNombreAsc();
}
