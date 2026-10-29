package pe.edu.upeu.autoandes.mapper;

import pe.edu.upeu.autoandes.dto.VehiculoRequestDTO;
import pe.edu.upeu.autoandes.dto.VehiculoResponseDTO;
import pe.edu.upeu.autoandes.entity.Cliente;
import pe.edu.upeu.autoandes.entity.Vehiculo;

public final class VehiculoMapper {
    private VehiculoMapper() {}
    public static VehiculoResponseDTO toResponse(Vehiculo entity) {
        return new VehiculoResponseDTO(entity.getId(), entity.getPlaca(), entity.getMarca(), entity.getModelo(),
                entity.getAnio(), entity.getCliente().getId(), entity.getCliente().getNombres());
    }
    public static void update(Vehiculo entity, VehiculoRequestDTO dto, Cliente cliente) {
        entity.actualizar(dto.placa().trim().toUpperCase(), dto.marca().trim(), dto.modelo().trim(),
                dto.anio(), cliente);
    }
}
