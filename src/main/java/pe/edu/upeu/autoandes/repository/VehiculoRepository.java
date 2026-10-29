package pe.edu.upeu.autoandes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.autoandes.entity.Vehiculo;
import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    boolean existsByPlacaIgnoreCase(String placa);
    List<Vehiculo> findByClienteIdOrderByPlacaAsc(Long clienteId);
}
