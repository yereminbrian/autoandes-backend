package pe.edu.upeu.autoandes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.entity.Servicio;
import pe.edu.upeu.autoandes.exception.ApiException;
import pe.edu.upeu.autoandes.mapper.ServicioMapper;
import pe.edu.upeu.autoandes.repository.ServicioRepository;
import pe.edu.upeu.autoandes.repository.DetalleOrdenRepository;
import java.util.List;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {
    private static final Logger log = LoggerFactory.getLogger(ServicioServiceImpl.class);
    private final ServicioRepository repository;
    private final DetalleOrdenRepository detalleRepository;
    public ServicioServiceImpl(ServicioRepository repository, DetalleOrdenRepository detalleRepository) {
        this.repository = repository; this.detalleRepository = detalleRepository;
    }
    @Override @Transactional(readOnly = true)
    public List<ServicioResponseDTO> listar() { return repository.findAll().stream().map(ServicioMapper::toResponse).toList(); }
    @Override @Transactional(readOnly = true)
    public ServicioResponseDTO obtener(Long id) { return ServicioMapper.toResponse(buscar(id)); }
    @Override
    public ServicioResponseDTO crear(ServicioRequestDTO request) {
        if (repository.existsByCodigoIgnoreCase(request.codigo().trim())) throw ApiException.conflict("Ya existe un servicio con ese código.");
        Servicio entity = new Servicio();
        ServicioMapper.update(entity, request);
        Servicio saved = repository.save(entity);
        log.info("Servicio creado: {}", saved.getCodigo());
        return ServicioMapper.toResponse(saved);
    }
    @Override
    public void actualizar(Long id, ServicioRequestDTO request) {
        Servicio entity = buscar(id);
        if (!entity.getCodigo().equalsIgnoreCase(request.codigo().trim()) && repository.existsByCodigoIgnoreCase(request.codigo().trim()))
            throw ApiException.conflict("Ya existe un servicio con ese código.");
        ServicioMapper.update(entity, request);
        repository.save(entity);
        log.info("Servicio actualizado: {}", entity.getCodigo());
    }
    @Override
    public void eliminar(Long id) {
        Servicio entity = buscar(id);
        if (detalleRepository.existsByServicioId(id))
            throw ApiException.conflict("El servicio fue utilizado en una orden; cambie su estado a inactivo.");
        repository.delete(entity);
        log.info("Servicio eliminado: {}", entity.getCodigo());
    }
    @Override
    public ServicioResponseDTO cambiarEstado(Long id, CambiarEstadoServicioDTO request) {
        Servicio entity = buscar(id);
        entity.cambiarEstado(request.activo());
        Servicio actualizado = repository.save(entity);
        log.info("Estado del servicio {} cambiado a {}", actualizado.getCodigo(), actualizado.getActivo());
        return ServicioMapper.toResponse(actualizado);
    }
    private Servicio buscar(Long id) { return repository.findById(id).orElseThrow(() -> ApiException.notFound("No existe el servicio " + id + ".")); }
}
