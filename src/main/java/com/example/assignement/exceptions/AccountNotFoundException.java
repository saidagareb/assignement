package com.example.assignement.exceptions;

import com.example.assignement.domains.CustomException;

public class AccountNotFoundException extends CustomException {
    public AccountNotFoundException(String detail) {
        super("Account not found", "ACCOUNT_NOT_FOUND", detail);
    }

}