package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajesweb.columbia_springboot.entity.Hotel;
import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import com.columbiaviajesweb.columbia_springboot.repository.HotelRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HotelService extends ServicioBase<Hotel> {

    private final HotelRepository repo;

    public HotelService(HotelRepository repo) { this.repo = repo; }

    @Override protected JpaRepository<Hotel, Long> repositorio() { return repo; }
    @Override protected String nombreEntidad() { return "el hotel"; }

    @Override
    protected void validar(Hotel h) {
        if (repo.existsByCodigoIgnoreCaseAndIdNot(h.getCodigo(), idONinguno(h.getId()))) {
            throw new ReglaNegocioException("Ya existe un hotel con el código " + h.getCodigo());
        }
    }

    @Override
    public List<Hotel> listar() { return repo.findAllByOrderByCiudadAscNombreAsc(); }
}
