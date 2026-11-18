package com.columbiaviajesweb.columbia_springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Vuelo regular contratado en exclusiva por la cadena.
 * Las plazas de primera clase son las plazas totales menos las de clase turista.
 */
@Entity
@Table(name = "vuelos")
@Getter
@Setter
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de vuelo es obligatorio")
    @Size(max = 10)
    @Column(nullable = false, unique = true, length = 10)
    private String numero;

    @NotNull(message = "Indicá fecha y hora")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @NotBlank(message = "El origen es obligatorio")
    @Column(nullable = false, length = 80)
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Column(nullable = false, length = 80)
    private String destino;

    @NotNull(message = "Indicá las plazas totales")
    @Min(value = 1, message = "Debe tener al menos 1 plaza")
    @Column(nullable = false)
    private Integer plazasTotales;

    @NotNull(message = "Indicá las plazas de clase turista")
    @PositiveOrZero
    @Column(nullable = false)
    private Integer plazasTurista;

    @NotNull(message = "Indicá el precio")
    @PositiveOrZero
    @Column(nullable = false)
    private Double precioTurista;

    @NotNull(message = "Indicá el precio")
    @PositiveOrZero
    @Column(nullable = false)
    private Double precioPrimera;

    public Vuelo() { }

    public Vuelo(String numero, LocalDateTime fechaHora, String origen, String destino,
                 int plazasTotales, int plazasTurista, double precioTurista, double precioPrimera) {
        this.numero = numero;
        this.fechaHora = fechaHora;
        this.origen = origen;
        this.destino = destino;
        this.plazasTotales = plazasTotales;
        this.plazasTurista = plazasTurista;
        this.precioTurista = precioTurista;
        this.precioPrimera = precioPrimera;
    }

    public int getPlazasPrimera() {
        if (plazasTotales == null || plazasTurista == null) return 0;
        return plazasTotales - plazasTurista;
    }

    /** Plazas que tiene el vuelo en la clase indicada. */
    public int getPlazas(ClaseVuelo clase) {
        return clase == ClaseVuelo.TURISTA ? plazasTurista : getPlazasPrimera();
    }

    /** Precio del pasaje en la clase indicada. */
    public double getPrecio(ClaseVuelo clase) {
        return clase == ClaseVuelo.TURISTA ? precioTurista : precioPrimera;
    }

    @AssertTrue(message = "Las plazas de clase turista no pueden superar las plazas totales")
    public boolean isPlazasValidas() {
        return plazasTotales == null || plazasTurista == null || plazasTurista <= plazasTotales;
    }

    public String getRuta() {
        return origen + " → " + destino;
    }
}
