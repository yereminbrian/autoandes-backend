package pe.edu.upeu.autoandes.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import pe.edu.upeu.autoandes.config.JwtProperties;
import pe.edu.upeu.autoandes.entity.Usuario;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private final JwtProperties properties;
    private final SecretKey key;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generar(Usuario usuario) {
        Instant ahora = Instant.now();
        List<String> roles = PermisosPorRol.rolesDe(usuario);
        List<String> permisos = PermisosPorRol.permisosDe(usuario);
        return Jwts.builder().issuer("AutoAndes").subject(usuario.getUsername())
                .claim("nombreCompleto", usuario.getNombreCompleto())
                .claim("roles", roles).claim("permisos", permisos)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusSeconds(properties.expirationSeconds())))
                .signWith(key, Jwts.SIG.HS256).compact();
    }

    public Jws<Claims> validar(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public long expiraEnSegundos() { return properties.expirationSeconds(); }
}
