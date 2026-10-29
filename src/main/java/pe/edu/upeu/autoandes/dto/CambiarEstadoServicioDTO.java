package pe.edu.upeu.autoandes.dto;

import jakarta.validation.constraints.NotNull;

public record CambiarEstadoServicioDTO(@NotNull Boolean activo) {}
