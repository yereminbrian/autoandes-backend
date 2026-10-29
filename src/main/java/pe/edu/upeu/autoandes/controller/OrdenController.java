package pe.edu.upeu.autoandes.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.entity.EstadoOrden;
import pe.edu.upeu.autoandes.service.OrdenService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {
    private final OrdenService service;
    public OrdenController(OrdenService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasAuthority('ORDEN_REGISTRAR')")
    public OrdenResponseDTO registrar(@Valid @RequestBody OrdenRegistroRequestDTO request, Authentication authentication) {
        return service.registrar(request, authentication.getName());
    }
    @GetMapping @PreAuthorize("hasAuthority('ORDEN_LEER')")
    public List<OrdenResponseDTO> buscar(@RequestParam(required = false) String placa,
            @RequestParam(required = false) EstadoOrden estado, @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta) {
        return service.buscar(placa, estado, desde, hasta);
    }
    @GetMapping("/{id}") @PreAuthorize("hasAuthority('ORDEN_LEER')")
    public OrdenResponseDTO obtener(@PathVariable Long id) { return service.obtener(id); }
    @PatchMapping("/{id}/anular") @PreAuthorize("hasAuthority('ORDEN_ANULAR')")
    public OrdenResponseDTO anular(@PathVariable Long id, @Valid @RequestBody AnularOrdenRequestDTO request, Authentication authentication) {
        return service.anular(id, request, authentication.getName());
    }
}
