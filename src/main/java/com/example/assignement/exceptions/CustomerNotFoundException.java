package com.example.assignement.exceptions;

import com.example.assignement.domains.CustomException;


public class CustomerNotFoundException extends CustomException {
    public CustomerNotFoundException(String detail) {
        super("Customer not found", "CUSTOMER_NOT_FOUND", detail);
    }

}
