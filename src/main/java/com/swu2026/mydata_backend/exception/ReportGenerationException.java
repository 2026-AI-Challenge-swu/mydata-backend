package com.swu2026.mydata_backend.exception;

import lombok.Getter;

@Getter
public class ReportGenerationException extends RuntimeException {

    private final boolean retryable;

    public ReportGenerationException(String message, boolean retryable) {
        super(message);
        this.retryable = retryable;
    }
}
