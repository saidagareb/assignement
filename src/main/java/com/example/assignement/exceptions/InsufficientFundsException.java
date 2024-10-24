package com.example.assignement.exceptions;

import com.example.assignement.domains.CustomException;

public class InsufficientFundsException extends CustomException {
    public InsufficientFundsException() {
        super("Insufficient funds for transfer", "INSUFFICIENT_FUNDS");
    }

}