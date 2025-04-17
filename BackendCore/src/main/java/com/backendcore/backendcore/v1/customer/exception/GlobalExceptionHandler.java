package com.backendcore.backendcore.v1.customer.exception;

import com.backendcore.backendcore.v1.customer.dto.response.Response;
import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private Response response = new Response();

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException ex) {
        String errorMessage = (ex.getMessage() == null || ex.getMessage().isEmpty())
                ? "Not found"
                : ex.getMessage();
        response.setError(errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGeneralException(Exception ex) {
        System.out.println(ex.getMessage());
        response.setError("An unexpected error occurred");
        Sentry.captureException(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
