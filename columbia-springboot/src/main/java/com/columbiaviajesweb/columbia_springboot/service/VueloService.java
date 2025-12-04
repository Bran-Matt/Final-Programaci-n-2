package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajes.entity.Vuelo;
import com.columbiaviajes.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VueloService {
    
    private final VueloRepository vueloRepository;
    
    public List<Vuelo> obtenerTodos() {
        return vueloRepository.findAll();
    }
    
    public Optional<Vuelo> buscarPorNumero(Integer numeroVuelo) {
        return vueloRepository.findByNumeroVuelo(numeroVuelo);
    }
    
    public Vuelo guardarVuelo(Vuelo vuelo) {
        return vueloRepository.save(vuelo);
    }
    
    public boolean existeVuelo(Integer numeroVuelo) {
        return vueloRepository.existsByNumeroVuelo(numeroVuelo);
    }
}