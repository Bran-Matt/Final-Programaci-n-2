package com.columbiaviajesweb.columbia_springboot.repository;

import com.columbiaviajesweb.columbia_springboot.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAllByOrderByFechaReservaDesc();

    /** Deja sin vendedor las ventas de un vendedor (sin recalcular importes). */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Reserva r set r.vendedor = null where r.vendedor = :vendedor")
    int quitarVendedor(@Param("vendedor") Vendedor vendedor);

    List<Reserva> findTop5ByOrderByFechaReservaDesc();

    @Query("select coalesce(sum(r.importe), 0) from Reserva r")
    double sumarImportes();

    /** Pasajes ya vendidos de un vuelo en una clase, sin contar la reserva que se está editando. */
    @Query("""
           select count(r) from ReservaVuelo r
           where r.vuelo = :vuelo and r.clase = :clase and r.id <> :excluirId
           """)
    long contarPasajes(@Param("vuelo") Vuelo vuelo, @Param("clase") ClaseVuelo clase,
                       @Param("excluirId") Long excluirId);

    /** Reservas de hotel que se superponen con el rango [llegada, partida). */
    @Query("""
           select count(r) from ReservaHotel r
           where r.hotel = :hotel and r.fechaLlegada < :partida and r.fechaPartida > :llegada
             and r.id <> :excluirId
           """)
    long contarOcupacion(@Param("hotel") Hotel hotel, @Param("llegada") LocalDate llegada,
                         @Param("partida") LocalDate partida, @Param("excluirId") Long excluirId);
}
