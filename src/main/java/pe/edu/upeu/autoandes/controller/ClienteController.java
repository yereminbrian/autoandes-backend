package pe.edu.upeu.autoandes.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.autoandes.dto.ClienteResponseDTO;
import pe.edu.upeu.autoandes.service.ClienteService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service = service; }
    @GetMapping @PreAuthorize("hasAuthority('VEHICULO_LEER')")
    public List<ClienteResponseDTO> listar() { return service.listar(); }
}
