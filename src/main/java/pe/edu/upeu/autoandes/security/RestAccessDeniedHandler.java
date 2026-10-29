package pe.edu.upeu.autoandes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pe.edu.upeu.autoandes.dto.ErrorResponseDTO;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;
    public RestAccessDeniedHandler(ObjectMapper mapper) { this.mapper = mapper; }
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(response.getOutputStream(), new ErrorResponseDTO(LocalDateTime.now(), 403,
                "Forbidden", "No tienes permiso para realizar esta operación.", request.getRequestURI(), null));
    }
}
