package pe.edu.upeu.autoandes.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.upeu.autoandes.dto.ErrorResponseDTO;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleApi(ApiException ex, HttpServletRequest request) {
        log.warn("Regla rechazada [{}]: {}", ex.getStatus().value(), ex.getMessage());
        return response(ex.getStatus(), ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) fields.putIfAbsent(error.getField(), error.getDefaultMessage());
        return response(HttpStatus.BAD_REQUEST, "Datos inválidos", request, fields);
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleNotFound(Exception ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "No existe la ruta solicitada.", request, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbidden(AccessDeniedException ex, HttpServletRequest request) {
        return response(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta operación.", request, null);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(Exception ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "La solicitud contiene datos inválidos o incompletos.", request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraint(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Conflicto de integridad de datos en {}", request.getRequestURI());
        return response(HttpStatus.CONFLICT, "El registro entra en conflicto con datos existentes.", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en {}", request.getRequestURI(), ex);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno.", request, null);
    }

    private ResponseEntity<ErrorResponseDTO> response(HttpStatus status, String message, HttpServletRequest request, Map<String, String> fields) {
        return ResponseEntity.status(status).body(new ErrorResponseDTO(LocalDateTime.now(), status.value(),
                status.getReasonPhrase(), message, request.getRequestURI(), fields));
    }
}
