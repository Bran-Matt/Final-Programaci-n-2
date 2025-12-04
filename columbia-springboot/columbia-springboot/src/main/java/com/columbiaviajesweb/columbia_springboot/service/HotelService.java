package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajes.entity.Hotel;
import com.columbiaviajes.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService {
    
    private final HotelRepository hotelRepository;
    
    public List<Hotel> obtenerTodos() {
        return hotelRepository.findAll();
    }
    
    public Optional<Hotel> buscarPorNombre(String nombre) {
        return hotelRepository.findByNombre(nombre);
    }
    
    public Hotel guardarHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }
    
    public boolean existeHotel(String nombre) {
        return hotelRepository.existsByNombre(nombre);
    }
}