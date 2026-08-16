package com.swu2026.mydata_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConnectionErrorResponse {

    private String message;
    private boolean retryable;
}
