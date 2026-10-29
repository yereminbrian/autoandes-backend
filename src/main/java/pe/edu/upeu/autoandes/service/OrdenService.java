package pe.edu.upeu.autoandes.service;

import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.entity.EstadoOrden;
import java.time.LocalDate;
import java.util.List;

public interface OrdenService {
    OrdenResponseDTO registrar(OrdenRegistroRequestDTO request, String usuario);
    List<OrdenResponseDTO> buscar(String placa, EstadoOrden estado, LocalDate desde, LocalDate hasta);
    OrdenResponseDTO obtener(Long id);
    OrdenResponseDTO anular(Long id, AnularOrdenRequestDTO request, String usuario);
    List<ReporteIngresoServicioDTO> reporte(LocalDate desde, LocalDate hasta);
}
