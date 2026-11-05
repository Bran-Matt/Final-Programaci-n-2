package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Servicio genérico con las operaciones CRUD comunes a todas las entidades (abstracción).
 * Cada servicio concreto hereda de esta clase y redefine {@link #validar(Object)}
 * con sus propias reglas (polimorfismo).
 *
 * @param <T> tipo de entidad
 */
public abstract class ServicioBase<T> {

    protected abstract JpaRepository<T, Long> repositorio();

    /** Nombre de la entidad para mensajes de error ("el hotel", "la sucursal"...). */
    protected abstract String nombreEntidad();

    /** Reglas de negocio a validar antes de guardar. Por defecto no hay ninguna. */
    protected void validar(T entidad) { }

    public List<T> listar() {
        return repositorio().findAll();
    }

    public T buscar(Long id) {
        return repositorio().findById(id)
                .orElseThrow(() -> new ReglaNegocioException("No se encontró " + nombreEntidad()));
    }

    public T guardar(T entidad) {
        validar(entidad);
        return repositorio().save(entidad);
    }

    public void eliminar(Long id) {
        try {
            repositorio().deleteById(id);
            repositorio().flush();
        } catch (DataIntegrityViolationException e) {
            throw new ReglaNegocioException("No se puede eliminar " + nombreEntidad()
                    + " porque tiene reservas o usuarios asociados");
        }
    }

    public long contar() {
        return repositorio().count();
    }

    /** Convierte un id nulo en uno imposible, para las consultas "distinto de este id". */
    protected static Long idONinguno(Long id) {
        return id == null ? -1L : id;
    }
}
