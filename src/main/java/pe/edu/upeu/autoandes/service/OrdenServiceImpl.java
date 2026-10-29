package pe.edu.upeu.autoandes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.exception.ApiException;
import pe.edu.upeu.autoandes.mapper.OrdenMapper;
import pe.edu.upeu.autoandes.repository.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Service
@Transactional
public class OrdenServiceImpl implements OrdenService {
    private static final Logger log = LoggerFactory.getLogger(OrdenServiceImpl.class);
    private static final ZoneId ZONA_NEGOCIO = ZoneId.of("America/Lima");
    private final OrdenServicioRepository ordenRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ServicioRepository servicioRepository;
    private final ReporteRepository reporteRepository;

    public OrdenServiceImpl(OrdenServicioRepository ordenRepository, VehiculoRepository vehiculoRepository,
                            ServicioRepository servicioRepository, ReporteRepository reporteRepository) {
        this.ordenRepository = ordenRepository; this.vehiculoRepository = vehiculoRepository;
        this.servicioRepository = servicioRepository; this.reporteRepository = reporteRepository;
    }

    @Override
    public OrdenResponseDTO registrar(OrdenRegistroRequestDTO request, String usuario) {
        if (request.detalles() == null || request.detalles().isEmpty() || request.detalles().size() > 10)
            throw ApiException.badRequest("La orden debe tener entre 1 y 10 detalles.");
        Vehiculo vehiculo = vehiculoRepository.findById(request.vehiculoId())
                .orElseThrow(() -> ApiException.notFound("No existe el vehículo " + request.vehiculoId() + "."));
        var ultima = ordenRepository.findFirstByVehiculo_IdAndEstadoOrderByFechaDescIdDesc(
                vehiculo.getId(), EstadoOrden.REGISTRADA);
        if (ultima.isPresent() && request.kilometraje() < ultima.get().getKilometraje())
            throw ApiException.conflict("El kilometraje no puede ser menor que el último registrado: " + ultima.get().getKilometraje() + " km.");

        Set<Long> ids = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;
        List<DetalleOrden> detalles = new ArrayList<>();
        for (DetalleOrdenRequestDTO item : request.detalles()) {
            if (!ids.add(item.servicioId())) throw ApiException.badRequest("No se puede repetir un servicio en la misma orden.");
            if (item.cantidad() == null || item.cantidad() < 1 || item.cantidad() > 5)
                throw ApiException.badRequest("La cantidad de cada servicio debe estar entre 1 y 5.");
            Servicio servicio = servicioRepository.findById(item.servicioId())
                    .orElseThrow(() -> ApiException.notFound("No existe el servicio " + item.servicioId() + "."));
            if (!Boolean.TRUE.equals(servicio.getActivo()))
                throw ApiException.conflict("El servicio " + servicio.getCodigo() + " está inactivo.");
            BigDecimal subtotal = servicio.getPrecio().multiply(BigDecimal.valueOf(item.cantidad())).setScale(2);
            total = total.add(subtotal);
            detalles.add(new DetalleOrden(servicio, item.cantidad(), servicio.getPrecio(), subtotal));
        }
        Long siguiente = ordenRepository.siguienteNumero();
        String numero = "OS-%06d".formatted(siguiente);
        OrdenServicio orden = new OrdenServicio(numero, LocalDateTime.now(ZONA_NEGOCIO), vehiculo,
                request.kilometraje(), total.setScale(2), usuario);
        detalles.forEach(orden::agregarDetalle);
        OrdenServicio guardada = ordenRepository.saveAndFlush(orden);
        log.info("Orden {} registrada por {} por un total de {}", guardada.getNumero(), usuario, guardada.getTotal());
        return OrdenMapper.toResponse(guardada);
    }

    @Override @Transactional(readOnly = true)
    public List<OrdenResponseDTO> buscar(String placa, EstadoOrden estado, LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta))
            throw ApiException.badRequest("La fecha inicial no puede ser posterior a la fecha final.");
        LocalDateTime inicio = desde == null ? null : desde.atStartOfDay();
        LocalDateTime finExclusivo = hasta == null ? null : hasta.plusDays(1).atStartOfDay().minusNanos(1);
        return ordenRepository.buscar(blankToNull(placa), estado, inicio, finExclusivo).stream()
                .map(OrdenMapper::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public OrdenResponseDTO obtener(Long id) { return OrdenMapper.toResponse(buscarOrden(id)); }

    @Override
    public OrdenResponseDTO anular(Long id, AnularOrdenRequestDTO request, String usuario) {
        OrdenServicio orden = buscarOrden(id);
        if (orden.getEstado() != EstadoOrden.REGISTRADA)
            throw ApiException.conflict("Solo se puede anular una orden REGISTRADA.");
        if (!orden.getFecha().toLocalDate().equals(LocalDate.now(ZONA_NEGOCIO)))
            throw ApiException.conflict("La orden solo puede anularse el mismo día de su registro.");
        String motivo = request.motivo() == null ? "" : request.motivo().trim();
        if (motivo.length() < 10 || motivo.length() > 200)
            throw ApiException.badRequest("El motivo debe tener entre 10 y 200 caracteres.");
        orden.anular(motivo);
        OrdenServicio actualizada = ordenRepository.save(orden);
        log.info("Orden {} anulada por {}", actualizada.getNumero(), usuario);
        return OrdenMapper.toResponse(actualizada);
    }

    @Override @Transactional(readOnly = true)
    public List<ReporteIngresoServicioDTO> reporte(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) throw ApiException.badRequest("Debe indicar las fechas desde y hasta.");
        if (desde.isAfter(hasta)) throw ApiException.badRequest("La fecha inicial no puede ser posterior a la fecha final.");
        return reporteRepository.ingresosPorServicio(desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay());
    }

    private OrdenServicio buscarOrden(Long id) {
        return ordenRepository.findById(id).orElseThrow(() -> ApiException.notFound("No existe la orden " + id + "."));
    }
    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
