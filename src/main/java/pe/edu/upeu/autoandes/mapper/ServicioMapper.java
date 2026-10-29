package pe.edu.upeu.autoandes.mapper;

import pe.edu.upeu.autoandes.dto.ServicioRequestDTO;
import pe.edu.upeu.autoandes.dto.ServicioResponseDTO;
import pe.edu.upeu.autoandes.entity.Servicio;

public final class ServicioMapper {
    private ServicioMapper() {}
    public static ServicioResponseDTO toResponse(Servicio entity) {
        return new ServicioResponseDTO(entity.getId(), entity.getCodigo(), entity.getNombre(),
                entity.getPrecio(), entity.getDuracionMin(), entity.getActivo());
    }
    public static void update(Servicio entity, ServicioRequestDTO dto) {
        entity.actualizar(dto.codigo().trim().toUpperCase(), dto.nombre().trim(), dto.precio(), dto.duracionMin());
    }
}
