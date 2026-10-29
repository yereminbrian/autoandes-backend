package pe.edu.upeu.autoandes.security;

import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pe.edu.upeu.autoandes.dto.ErrorResponseDTO;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final JsonMapper mapper;
    public RestAuthenticationEntryPoint(JsonMapper mapper) { this.mapper = mapper; }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(response.getOutputStream(), new ErrorResponseDTO(LocalDateTime.now(), 401,
                "Unauthorized", request.getAttribute("jwt_invalido") == null ? "Se requiere autenticación." : "Token inválido o vencido.",
                request.getRequestURI(), null));
    }
}
