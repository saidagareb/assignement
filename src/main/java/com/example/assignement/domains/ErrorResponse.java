package com.example.assignement.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String errorCode;
    private String path;

    public ErrorResponse(String message, String errorCode, String path) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.errorCode = errorCode;
        this.path = path;
    }

}

