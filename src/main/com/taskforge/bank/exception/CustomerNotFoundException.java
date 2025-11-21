package com.taskforge.bank.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(long customerId) {
        super("Customer with ID " + customerId + " not found king 👑");
    }
}