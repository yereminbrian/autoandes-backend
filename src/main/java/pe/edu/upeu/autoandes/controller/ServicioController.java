package pe.edu.upeu.autoandes.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.service.ServicioService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/servicios")
public class ServicioController {
    private final ServicioService service;
    public ServicioController(ServicioService service) { this.service = service; }
    @GetMapping @PreAuthorize("hasAuthority('SERVICIO_LEER')")
    public List<ServicioResponseDTO> listar() { return service.listar(); }
    @GetMapping("/{id}") @PreAuthorize("hasAuthority('SERVICIO_LEER')")
    public ServicioResponseDTO obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')")
    public ServicioResponseDTO crear(@Valid @RequestBody ServicioRequestDTO request) { return service.crear(request); }
    @PutMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')")
    public void actualizar(@PathVariable Long id, @Valid @RequestBody ServicioRequestDTO request) { service.actualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')")
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
    @PatchMapping("/{id}/estado") @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')")
    public ServicioResponseDTO cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambiarEstadoServicioDTO request) { return service.cambiarEstado(id, request); }
}
