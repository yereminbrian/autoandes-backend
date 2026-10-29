package pe.edu.upeu.autoandes.dto;

import java.math.BigDecimal;

public interface ReporteIngresoServicioDTO {
    String getCodigo();
    String getServicio();
    Long getCantidadTotal();
    BigDecimal getImporteTotal();
}
