package pe.edu.upeu.autoandes.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.autoandes.dto.VehiculoRequestDTO;
import pe.edu.upeu.autoandes.dto.VehiculoResponseDTO;
import pe.edu.upeu.autoandes.service.VehiculoService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
public class VehiculoController {
    private final VehiculoService service;
    public VehiculoController(VehiculoService service) { this.service = service; }
    @GetMapping @PreAuthorize("hasAuthority('VEHICULO_LEER')")
    public List<VehiculoResponseDTO> listar(@RequestParam(required = false) Long clienteId) { return service.listar(clienteId); }
    @GetMapping("/{id}") @PreAuthorize("hasAuthority('VEHICULO_LEER')")
    public VehiculoResponseDTO obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasAuthority('VEHICULO_ESCRIBIR')")
    public VehiculoResponseDTO crear(@Valid @RequestBody VehiculoRequestDTO request) { return service.crear(request); }
    @PutMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasAuthority('VEHICULO_ESCRIBIR')")
    public void actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoRequestDTO request) { service.actualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasAuthority('VEHICULO_ESCRIBIR')")
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
