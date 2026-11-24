package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajesweb.columbia_springboot.entity.Vuelo;
import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import com.columbiaviajesweb.columbia_springboot.repository.VueloRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VueloService extends ServicioBase<Vuelo> {

    private final VueloRepository repo;

    public VueloService(VueloRepository repo) { this.repo = repo; }

    @Override protected JpaRepository<Vuelo, Long> repositorio() { return repo; }
    @Override protected String nombreEntidad() { return "el vuelo"; }

    @Override
    protected void validar(Vuelo v) {
        if (repo.existsByNumeroIgnoreCaseAndIdNot(v.getNumero(), idONinguno(v.getId()))) {
            throw new ReglaNegocioException("Ya existe un vuelo con el número " + v.getNumero());
        }
    }

    @Override
    public List<Vuelo> listar() { return repo.findAllByOrderByFechaHoraAsc(); }

    public List<Vuelo> proximos() {
        return repo.findTop5ByFechaHoraAfterOrderByFechaHoraAsc(LocalDateTime.now());
    }
}
