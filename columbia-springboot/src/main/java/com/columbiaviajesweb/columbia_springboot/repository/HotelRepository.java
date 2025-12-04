package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajes.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}