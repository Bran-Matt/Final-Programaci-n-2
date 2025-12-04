package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajes.entity.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    Optional<Vuelo> findByNumeroVuelo(Integer numeroVuelo);
    boolean existsByNumeroVuelo(Integer numeroVuelo);
}