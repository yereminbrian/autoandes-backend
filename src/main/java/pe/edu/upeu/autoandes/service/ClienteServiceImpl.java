package pe.edu.upeu.autoandes.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.ClienteResponseDTO;
import pe.edu.upeu.autoandes.repository.ClienteRepository;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository repository;
    public ClienteServiceImpl(ClienteRepository repository) { this.repository = repository; }
    @Override public List<ClienteResponseDTO> listar() {
        return repository.findAll().stream().map(c -> new ClienteResponseDTO(
                c.getId(), c.getDocumento(), c.getNombres(), c.getTelefono())).toList();
    }
}
