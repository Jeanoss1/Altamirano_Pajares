package com.altamirano_pajares.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

// Centraliza el manejo de errores: los controladores no necesitan try/catch
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFound(ModelNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), List.of(), req);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<CustomErrorResponse> handleBusinessRule(BusinessRuleException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of(), req);
    }

    // Falla de @Valid sobre el DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Datos de entrada no validos", details, req);
    }

    // JSON mal formado, valor de enum inexistente o fecha con formato incorrecto
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST,
                "El cuerpo de la peticion no es valido (revise el JSON, los enums y las fechas yyyy-MM-dd)", List.of(), req);
    }

    // Ej.: GET /v1/teachers/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST,
                "El parametro " + ex.getName() + " tiene un valor no valido: " + ex.getValue(), List.of(), req);
    }

    // Ultima barrera: restriccion unique o check de la BD
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Se violo una restriccion de la base de datos", List.of(), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), List.of(), req);
    }

    private ResponseEntity<CustomErrorResponse> build(HttpStatus status, String message, List<String> details,
                                                      HttpServletRequest req) {
        CustomErrorResponse body = new CustomErrorResponse(LocalDateTime.now(), status.value(), message, details,
                req.getRequestURI());
        return new ResponseEntity<>(body, status);
    }
}
