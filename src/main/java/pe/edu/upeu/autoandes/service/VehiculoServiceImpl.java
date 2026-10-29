package pe.edu.upeu.autoandes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.VehiculoRequestDTO;
import pe.edu.upeu.autoandes.dto.VehiculoResponseDTO;
import pe.edu.upeu.autoandes.entity.Cliente;
import pe.edu.upeu.autoandes.entity.Vehiculo;
import pe.edu.upeu.autoandes.exception.ApiException;
import pe.edu.upeu.autoandes.mapper.VehiculoMapper;
import pe.edu.upeu.autoandes.repository.ClienteRepository;
import pe.edu.upeu.autoandes.repository.VehiculoRepository;
import pe.edu.upeu.autoandes.repository.OrdenServicioRepository;
import java.util.List;

@Service
@Transactional
public class VehiculoServiceImpl implements VehiculoService {
    private static final Logger log = LoggerFactory.getLogger(VehiculoServiceImpl.class);
    private final VehiculoRepository repository;
    private final ClienteRepository clienteRepository;
    private final OrdenServicioRepository ordenRepository;
    public VehiculoServiceImpl(VehiculoRepository repository, ClienteRepository clienteRepository, OrdenServicioRepository ordenRepository) {
        this.repository = repository; this.clienteRepository = clienteRepository; this.ordenRepository = ordenRepository;
    }
    @Override @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> listar(Long clienteId) {
        List<Vehiculo> vehiculos = clienteId == null ? repository.findAll() : repository.findByClienteIdOrderByPlacaAsc(clienteId);
        return vehiculos.stream().map(VehiculoMapper::toResponse).toList();
    }
    @Override @Transactional(readOnly = true)
    public VehiculoResponseDTO obtener(Long id) { return VehiculoMapper.toResponse(buscar(id)); }
    @Override
    public VehiculoResponseDTO crear(VehiculoRequestDTO request) {
        if (repository.existsByPlacaIgnoreCase(request.placa().trim())) throw ApiException.conflict("Ya existe un vehículo con esa placa.");
        Cliente cliente = cliente(request.clienteId());
        Vehiculo entity = new Vehiculo();
        VehiculoMapper.update(entity, request, cliente);
        Vehiculo saved = repository.save(entity);
        log.info("Vehículo creado: {}", saved.getPlaca());
        return VehiculoMapper.toResponse(saved);
    }
    @Override
    public void actualizar(Long id, VehiculoRequestDTO request) {
        Vehiculo entity = buscar(id);
        if (!entity.getPlaca().equalsIgnoreCase(request.placa().trim()) && repository.existsByPlacaIgnoreCase(request.placa().trim()))
            throw ApiException.conflict("Ya existe un vehículo con esa placa.");
        VehiculoMapper.update(entity, request, cliente(request.clienteId()));
        repository.save(entity);
        log.info("Vehículo actualizado: {}", entity.getPlaca());
    }
    @Override
    public void eliminar(Long id) {
        Vehiculo entity = buscar(id);
        if (ordenRepository.existsByVehiculoId(id)) throw ApiException.conflict("El vehículo tiene órdenes registradas y no se puede eliminar.");
        repository.delete(entity);
        log.info("Vehículo eliminado: {}", entity.getPlaca());
    }
    private Vehiculo buscar(Long id) { return repository.findById(id).orElseThrow(() -> ApiException.notFound("No existe el vehículo " + id + ".")); }
    private Cliente cliente(Long id) { return clienteRepository.findById(id).orElseThrow(() -> ApiException.notFound("No existe el cliente " + id + ".")); }
}
