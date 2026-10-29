package pe.edu.upeu.autoandes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.autoandes.dto.ReporteIngresoServicioDTO;
import pe.edu.upeu.autoandes.entity.DetalleOrden;
import java.time.LocalDateTime;
import java.util.List;

public interface ReporteRepository extends JpaRepository<DetalleOrden, Long> {
    @Query("select d.servicio.codigo as codigo, d.servicio.nombre as servicio, " +
           "sum(d.cantidad) as cantidadTotal, sum(d.subtotal) as importeTotal " +
           "from DetalleOrden d join d.orden o where o.estado = pe.edu.upeu.autoandes.entity.EstadoOrden.REGISTRADA " +
           "and o.fecha >= :desde and o.fecha < :hasta " +
           "group by d.servicio.codigo, d.servicio.nombre order by sum(d.subtotal) desc")
    List<ReporteIngresoServicioDTO> ingresosPorServicio(@Param("desde") LocalDateTime desde,
                                                        @Param("hasta") LocalDateTime hasta);
}
