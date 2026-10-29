package pe.edu.upeu.autoandes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record OrdenRegistroRequestDTO(
        @NotNull(message = "El vehículo es obligatorio.") @Positive(message = "El vehículo debe ser válido.")
        Long vehiculoId,
        @NotNull(message = "El kilometraje es obligatorio.") @PositiveOrZero(message = "El kilometraje no puede ser negativo.")
        Integer kilometraje,
        @NotEmpty(message = "La orden debe tener al menos un detalle.")
        @Size(max = 10, message = "La orden admite como máximo 10 detalles.")
        List<@Valid DetalleOrdenRequestDTO> detalles
) {}
