package com.swu2026.mydata_backend.exception;

import com.swu2026.mydata_backend.dto.ConnectionErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MydataConnectionException.class)
    public ResponseEntity<ConnectionErrorResponse> handleMydataConnectionException(
        MydataConnectionException exception
    ) {
        ConnectionErrorResponse body = ConnectionErrorResponse.builder()
            .message(exception.getMessage())
            .retryable(exception.isRetryable())
            .build();

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }

    @ExceptionHandler(ReportGenerationException.class)
    public ResponseEntity<ConnectionErrorResponse> handleReportGenerationException(
        ReportGenerationException exception
    ) {
        ConnectionErrorResponse body = ConnectionErrorResponse.builder()
            .message(exception.getMessage())
            .retryable(exception.isRetryable())
            .build();

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }
}
