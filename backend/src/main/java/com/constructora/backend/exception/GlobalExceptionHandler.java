package com.constructora.backend.exception;

import com.constructora.backend.controller.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // Manejo de Resource Not Found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    // Manejo de Bad Request (400)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleBadRequestException(
            BadRequestException ex,
            WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // Manejo de Conflict (409)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleConflictException(
            ConflictException ex,
            WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    
    // Manejo de Unauthorized (401)
    @ExceptionHandler({UnauthorizedException.class, BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponseDTO<Object>> handleUnauthorizedException(
            RuntimeException ex,
            WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    // Manejo de Forbidden (403)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleForbiddenException(
            ForbiddenException ex,
            WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    
    // Manejo de errores de validación (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponseDTO<Map<String, String>> response = ApiResponseDTO.<Map<String, String>>builder()
                .success(false)
                .message("Error de validación")
                .data(errors)
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // Manejo de errores de almacenamiento de archivos
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleFileStorageException(
            FileStorageException ex,
            WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message("Error al almacenar el archivo: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Manejo de archivos demasiado grandes
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleMaxSizeException(
            MaxUploadSizeExceededException ex,
            WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message("El archivo es demasiado grande. Tamaño máximo permitido: 10MB")
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }
    
    // Manejo de errores de envío de correo
    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleEmailSendingException(
            EmailSendingException ex,
            WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message("Error al enviar el correo: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Manejo de excepciones generales (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleGlobalException(
            Exception ex,
            WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message("Error interno del servidor: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}