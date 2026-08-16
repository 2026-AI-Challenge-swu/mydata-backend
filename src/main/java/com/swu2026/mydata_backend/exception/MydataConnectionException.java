package com.swu2026.mydata_backend.exception;

import lombok.Getter;

@Getter
public class MydataConnectionException extends RuntimeException {

    private final boolean retryable;

    public MydataConnectionException(String message, boolean retryable) {
        super(message);
        this.retryable = retryable;
    }
}
