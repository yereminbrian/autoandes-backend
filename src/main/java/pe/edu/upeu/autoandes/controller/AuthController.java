package pe.edu.upeu.autoandes.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service = service; }
    @PostMapping("/login") public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) { return service.login(request); }
    @GetMapping("/me") public UsuarioActualDTO actual(Authentication authentication) { return service.actual(authentication.getName()); }
}
