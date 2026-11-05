package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Hotel contratado en exclusiva por la cadena.
 */
@Entity
@Table(name = "hoteles")
@Getter
@Setter
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 10)
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false, length = 150)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false, length = 80)
    private String ciudad;

    @Column(length = 30)
    private String telefono;

    /** Cantidad de plazas que el hotel ofrece a la cadena por noche. */
    @NotNull(message = "Indicá las plazas")
    @Min(value = 1, message = "Debe tener al menos 1 plaza")
    @Column(nullable = false)
    private Integer plazasDisponibles;

    @NotNull(message = "Indicá el precio")
    @PositiveOrZero
    @Column(nullable = false)
    private Double precioNoche;

    public Hotel() { }

    public Hotel(String codigo, String nombre, String direccion, String ciudad, String telefono,
                 Integer plazasDisponibles, Double precioNoche) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.plazasDisponibles = plazasDisponibles;
        this.precioNoche = precioNoche;
    }
}
