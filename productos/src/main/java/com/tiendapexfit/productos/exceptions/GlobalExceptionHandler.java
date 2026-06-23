package com.tiendapexfit.productos.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice //Intercepta excepciones globales
@Hidden
public class GlobalExceptionHandler {

    //1. Atrapar la excepción personalizada (Resource Not Found - 404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarResourceNotFound(ResourceNotFoundException ex, WebRequest request){

        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Recurso no Encontrado",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Interceptor para capturar errores de datos basura (Validation @Valid)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> manejarErroresValidacion(
            org.springframework.web.bind.MethodArgumentNotValidException ex, 
            org.springframework.web.context.request.WebRequest request) {
        
        java.util.Map<String, Object> respuesta = new java.util.HashMap<>();
        respuesta.put("timestamp", java.time.LocalDateTime.now());
        respuesta.put("status", org.springframework.http.HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Bad Request");
        
        // Extraemos solo los mensajes limpios que escribimos en el DTO
        java.util.Map<String, String> detallesErrores = new java.util.HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            detallesErrores.put(error.getField(), error.getDefaultMessage())
        );
        
        respuesta.put("messages", detallesErrores);
        respuesta.put("path", request.getDescription(false).replace("uri=", ""));
        
        return new org.springframework.http.ResponseEntity<>(respuesta, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

    //2. Atrapar cualquier otro error inesperado del sistema (Error Interno - 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErroresGlobales(Exception ex, WebRequest request){

        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ocurrió un error inesperado en el servidor: " + ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
