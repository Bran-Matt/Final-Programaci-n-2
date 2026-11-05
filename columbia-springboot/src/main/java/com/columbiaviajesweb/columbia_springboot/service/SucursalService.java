package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajesweb.columbia_springboot.entity.Sucursal;
import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import com.columbiaviajesweb.columbia_springboot.repository.SucursalRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SucursalService extends ServicioBase<Sucursal> {

    private final SucursalRepository repo;

    public SucursalService(SucursalRepository repo) { this.repo = repo; }

    @Override protected JpaRepository<Sucursal, Long> repositorio() { return repo; }
    @Override protected String nombreEntidad() { return "la sucursal"; }

    @Override
    protected void validar(Sucursal s) {
        if (repo.existsByCodigoIgnoreCaseAndIdNot(s.getCodigo(), idONinguno(s.getId()))) {
            throw new ReglaNegocioException("Ya existe una sucursal con el código " + s.getCodigo());
        }
    }

    @Override
    public List<Sucursal> listar() { return repo.findAllByOrderByCodigoAsc(); }
}
