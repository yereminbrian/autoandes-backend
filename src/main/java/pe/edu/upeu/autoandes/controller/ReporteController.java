package pe.edu.upeu.autoandes.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.autoandes.dto.ReporteIngresoServicioDTO;
import pe.edu.upeu.autoandes.service.OrdenService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {
    private final OrdenService service;
    public ReporteController(OrdenService service) { this.service = service; }
    @GetMapping("/ingresos-por-servicio") @PreAuthorize("hasAuthority('REPORTE_VER')")
    public List<ReporteIngresoServicioDTO> ingresos(@RequestParam LocalDate desde, @RequestParam LocalDate hasta) {
        return service.reporte(desde, hasta);
    }
}
