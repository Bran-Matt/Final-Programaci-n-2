package com.columbiaviajesweb.columbia_springboot.exception;

/**
 * Se lanza cuando una operación viola una regla del negocio
 * (por ejemplo, no quedan plazas en un vuelo).
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
