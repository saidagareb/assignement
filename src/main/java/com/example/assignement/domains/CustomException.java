package com.example.assignement.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final String errorCode;
    private final String detail;

    public CustomException(String message, String errorCode, String detail) {
        super(message);
        this.errorCode = errorCode;
        this.detail = detail;
    }
}