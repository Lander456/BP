package com.example.bp.exception;

import lombok.Getter;

@Getter
public class ApiErrorResponse {
    private final int status;
    private final String message;
    private final long timestamp;

    public ApiErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

}
