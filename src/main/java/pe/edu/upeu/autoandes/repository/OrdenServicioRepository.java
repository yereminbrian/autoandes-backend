package pe.edu.upeu.autoandes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.autoandes.entity.EstadoOrden;
import pe.edu.upeu.autoandes.entity.OrdenServicio;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {
    boolean existsByVehiculoId(Long vehiculoId);
    Optional<OrdenServicio> findFirstByVehiculo_IdAndEstadoOrderByFechaDescIdDesc(Long vehiculoId, EstadoOrden estado);

    @Query("select o from OrdenServicio o join fetch o.vehiculo v join fetch v.cliente " +
           "where (:placa is null or upper(v.placa) = upper(:placa)) " +
           "and (:estado is null or o.estado = :estado) " +
           "and (:desde is null or o.fecha >= :desde) and (:hasta is null or o.fecha <= :hasta) " +
           "order by o.fecha desc, o.id desc")
    List<OrdenServicio> buscar(@Param("placa") String placa, @Param("estado") EstadoOrden estado,
                               @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    @Query(value = "SELECT seq_numero_orden.NEXTVAL FROM dual", nativeQuery = true)
    Long siguienteNumero();
}
