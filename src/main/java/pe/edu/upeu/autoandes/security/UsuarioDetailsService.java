package pe.edu.upeu.autoandes.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import pe.edu.upeu.autoandes.entity.Usuario;
import pe.edu.upeu.autoandes.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository repository;
    public UsuarioDetailsService(UsuarioRepository repository) { this.repository = repository; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsernameIgnoreCase(username)
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales inválidas."));
        var authorities = PermisosPorRol.permisosDe(usuario).stream().map(SimpleGrantedAuthority::new).toList();
        return User.withUsername(usuario.getUsername()).password(usuario.getPassword()).authorities(authorities).build();
    }
}
