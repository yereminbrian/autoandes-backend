package pe.edu.upeu.autoandes.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.entity.Usuario;
import pe.edu.upeu.autoandes.exception.ApiException;
import pe.edu.upeu.autoandes.repository.UsuarioRepository;
import pe.edu.upeu.autoandes.security.JwtService;
import pe.edu.upeu.autoandes.security.PermisosPorRol;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public AuthServiceImpl(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository; this.passwordEncoder = passwordEncoder; this.jwtService = jwtService;
    }
    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = repository.findByUsernameIgnoreCase(request.username().trim()).orElse(null);
        boolean passwordOk = usuario != null && usuario.getPassword().startsWith("$2")
                && passwordEncoder.matches(request.password(), usuario.getPassword());
        if (!passwordOk || !Boolean.TRUE.equals(usuario.getActivo()))
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña inválidos.");
        return new LoginResponseDTO(jwtService.generar(usuario), "Bearer", jwtService.expiraEnSegundos(),
                usuario.getNombreCompleto(), PermisosPorRol.rolesDe(usuario), PermisosPorRol.permisosDe(usuario));
    }
    @Override
    public UsuarioActualDTO actual(String username) {
        Usuario usuario = repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> ApiException.notFound("No existe el usuario autenticado."));
        return new UsuarioActualDTO(usuario.getUsername(), usuario.getNombreCompleto(),
                PermisosPorRol.rolesDe(usuario), PermisosPorRol.permisosDe(usuario));
    }
}
