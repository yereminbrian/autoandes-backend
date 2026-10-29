package pe.edu.upeu.autoandes.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.repository.UsuarioRepository;

import java.util.Locale;
import java.util.Set;

@Component
public class AutoAndesDataInitializer implements CommandLineRunner {
    private static final Set<String> PASSWORD_MARKERS = Set.of("!PENDIENTE_BCRYPT!", "!ROTAR_BCRYPT!");
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AutoAndesDataInitializer(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        repository.findAll().stream()
                .filter(usuario -> PASSWORD_MARKERS.contains(usuario.getPassword()))
                .forEach(usuario -> {
                    String password = usuario.getUsername().toLowerCase(Locale.ROOT) + "123";
                    usuario.setPassword(passwordEncoder.encode(password));
                });
        repository.flush();
    }
}