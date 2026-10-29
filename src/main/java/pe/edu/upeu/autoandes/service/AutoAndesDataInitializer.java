package pe.edu.upeu.autoandes.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.repository.UsuarioRepository;

@Component
public class AutoAndesDataInitializer implements CommandLineRunner {
    private static final String PASSWORD_DEMO = "Auto2026*";
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    public AutoAndesDataInitializer(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository; this.passwordEncoder = passwordEncoder;
    }
    @Override @Transactional
    public void run(String... args) {
        repository.findAll().stream().filter(u -> u.getPassword().startsWith("!PENDIENTE_BCRYPT!"))
                .forEach(u -> u.setPassword(passwordEncoder.encode(PASSWORD_DEMO)));
        repository.flush();
    }
}
