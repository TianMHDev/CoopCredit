package com.coopcredit.application.infrastructure.adapter.in.web.exception;

import com.coopcredit.application.domain.exception.*;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.error.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(AfiliadoNotFoundException.class)
        public ResponseEntity<ProblemDetail> handleAfiliadoNotFoundException(AfiliadoNotFoundException ex,
                        WebRequest request) {
                return buildResponse(HttpStatus.NOT_FOUND, "Afiliado Not Found", ex.getMessage(), request);
        }

        @ExceptionHandler(SolicitudNotFoundException.class)
        public ResponseEntity<ProblemDetail> handleSolicitudNotFoundException(SolicitudNotFoundException ex,
                        WebRequest request) {
                return buildResponse(HttpStatus.NOT_FOUND, "Solicitud Not Found", ex.getMessage(), request);
        }

        @ExceptionHandler(AfiliadoAlreadyExistsException.class)
        public ResponseEntity<ProblemDetail> handleAfiliadoAlreadyExistsException(AfiliadoAlreadyExistsException ex,
                        WebRequest request) {
                return buildResponse(HttpStatus.CONFLICT, "Afiliado Already Exists", ex.getMessage(), request);
        }

        @ExceptionHandler(AfiliadoInactivoException.class)
        public ResponseEntity<ProblemDetail> handleAfiliadoInactivoException(AfiliadoInactivoException ex,
                        WebRequest request) {
                return buildResponse(HttpStatus.BAD_REQUEST, "Afiliado Inactivo", ex.getMessage(), request);
        }

        @ExceptionHandler(InvalidSalaryException.class)
        public ResponseEntity<ProblemDetail> handleInvalidSalaryException(InvalidSalaryException ex,
                        WebRequest request) {
                return buildResponse(HttpStatus.BAD_REQUEST, "Invalid Salary", ex.getMessage(), request);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
                return buildResponse(HttpStatus.FORBIDDEN, "Access Denied", ex.getMessage(), request);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ProblemDetail> handleAuthenticationException(AuthenticationException ex,
                        WebRequest request) {
                return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), request);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex,
                        WebRequest request) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach((error) -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });

                ProblemDetail problem = ProblemDetail.builder()
                                .type(URI.create("https://coopcredit.com/errors/validation-error"))
                                .title("Validation Error")
                                .status(HttpStatus.BAD_REQUEST.value())
                                .detail("Validation failed for one or more fields: " + errors.toString())
                                .instance(URI.create(request.getDescription(false).replace("uri=", "")))
                                .timestamp(Instant.now())
                                .traceId(UUID.randomUUID().toString())
                                .build();
                return new ResponseEntity<>(problem, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ProblemDetail> handleGlobalException(Exception ex, WebRequest request) {
                return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(),
                                request);
        }

        private ResponseEntity<ProblemDetail> buildResponse(HttpStatus status, String title, String detail,
                        WebRequest request) {
                ProblemDetail problem = ProblemDetail.builder()
                                .type(URI.create("https://coopcredit.com/errors/"
                                                + title.toLowerCase().replace(" ", "-")))
                                .title(title)
                                .status(status.value())
                                .detail(detail)
                                .instance(URI.create(request.getDescription(false).replace("uri=", "")))
                                .timestamp(Instant.now())
                                .traceId(UUID.randomUUID().toString())
                                .build();
                return new ResponseEntity<>(problem, status);
        }
}
