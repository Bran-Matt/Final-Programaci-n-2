package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajes.entity.Sucursal;
import com.columbiaviajes.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SucursalService {
    
    private final SucursalRepository sucursalRepository;
    
    public List<Sucursal> obtenerTodas() {
        return sucursalRepository.findAll();
    }
    
    public Optional<Sucursal> buscarPorCodigo(String codigo) {
        return sucursalRepository.findByCodigo(codigo);
    }
    
    public Sucursal guardarSucursal(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }
    
    public boolean existeSucursal(String codigo) {
        return sucursalRepository.existsByCodigo(codigo);
    }
}