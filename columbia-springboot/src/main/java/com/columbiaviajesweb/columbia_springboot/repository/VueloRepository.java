package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajesweb.columbia_springboot.entity.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    boolean existsByNumeroIgnoreCaseAndIdNot(String numero, Long id);

    List<Vuelo> findAllByOrderByFechaHoraAsc();

    List<Vuelo> findTop5ByFechaHoraAfterOrderByFechaHoraAsc(LocalDateTime desde);
}
