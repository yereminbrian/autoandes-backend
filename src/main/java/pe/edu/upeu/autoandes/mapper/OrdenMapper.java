package pe.edu.upeu.autoandes.mapper;

import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.entity.*;

public final class OrdenMapper {
    private OrdenMapper() {}
    public static OrdenResponseDTO toResponse(OrdenServicio orden) {
        var detalles = orden.getDetalles().stream().map(d -> new DetalleOrdenResponseDTO(
                d.getServicio().getId(), d.getServicio().getCodigo(), d.getServicio().getNombre(),
                d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal())).toList();
        return new OrdenResponseDTO(orden.getId(), orden.getNumero(), orden.getFecha(),
                orden.getVehiculo().getId(), orden.getVehiculo().getPlaca(),
                orden.getVehiculo().getCliente().getNombres(), orden.getKilometraje(),
                orden.getEstado(), orden.getTotal(), orden.getUsuarioRegistro(),
                orden.getMotivoAnulacion(), detalles);
    }
}
